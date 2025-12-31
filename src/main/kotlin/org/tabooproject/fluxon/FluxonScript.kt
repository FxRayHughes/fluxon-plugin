package org.tabooproject.fluxon

import org.tabooproject.fluxon.runtime.FluxonRuntime
import org.tabooproject.fluxon.runtime.FunctionContext
import org.tabooproject.fluxon.runtime.RuntimeScriptBase
import org.tabooproject.fluxon.runtime.error.FluxonRuntimeError
import org.tabooproject.fluxon.util.exceptFluxonCompletableFutureError
import org.tabooproject.fluxon.util.printError
import taboolib.common.platform.function.info
import taboolib.common.platform.function.warning
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Fluxon 脚本封装类
 */
class FluxonScript(
    val scriptId: String,
    val scriptFile: File,
    var instance: RuntimeScriptBase
) {
    
    // 脚本创建时间戳
    var timestamp: Long = System.currentTimeMillis()
        private set
    
    // 可释放的资源列表
    val resources = ConcurrentHashMap<String, AutoCloseable>()
    
    /**
     * 执行脚本
     *
     * @param vars 脚本变量
     * @return 执行结果
     */
    fun invoke(vars: Map<String, Any> = emptyMap()): Any? {
        val clonedScript = instance.clone()
        val environment = FluxonRuntime.getInstance().newEnvironment()
        environment.defineRootVariable("__script__", this)
        vars.forEach { (key, value) -> environment.defineRootVariable(key, value) }
        return try {
            clonedScript.eval(environment)?.exceptFluxonCompletableFutureError()
        } catch (ex: FluxonRuntimeError) {
            ex.printError()
            null
        }
    }

    /**
     * 重载脚本
     * 会先卸载当前脚本（释放资源），然后重新编译和加载
     *
     * @return 是否重载成功
     */
    fun reload(): Boolean {
        info("正在重载脚本: $scriptId")
        // 卸载当前脚本
        unload()
        // 重新编译脚本
        return try {
            val compiled = FluxonLibrary.compileFile(scriptFile, FluxonRuntime.getInstance().newEnvironment())
            if (compiled == null) {
                warning("重载脚本 $scriptId 失败: 编译结果为空")
                return false
            }
            // 更新实例
            this.instance = compiled.value.instance
            this.timestamp = System.currentTimeMillis()
            // 更新库中的引用
            FluxonLibrary.scripts[scriptId] = compiled.value
            info("脚本 $scriptId 重载成功")
            true
            
        } catch (ex: Exception) {
            warning("重载脚本 $scriptId 时发生异常: ${ex.message}")
            ex.printStackTrace()
            false
        }
    }

    /**
     * 卸载脚本
     * 会调用脚本的 release 函数并释放所有注册的资源
     */
    fun unload() {
        // 调用脚本的 release 函数
        val release = instance.environment.getFunctionOrNull("release")
        if (release != null) {
            try {
                release.call(FunctionContext(release, null, emptyArray(), FluxonRuntime.getInstance().newEnvironment()))
            } catch (ex: FluxonRuntimeError) {
                ex.printError()
            }
        }
        // 释放所有注册的资源
        releaseAllResources()
    }

    /**
     * 释放指定资源
     *
     * @param resourceId 资源 ID
     */
    fun releaseResource(resourceId: String) {
        val resource = resources.remove(resourceId)
        if (resource != null) {
            try {
                resource.close()
            } catch (ex: Exception) {
                warning("释放资源 $resourceId 时发生异常: ${ex.message}")
            }
        }
    }

    /**
     * 释放所有资源
     */
    fun releaseAllResources() {
        resources.forEach { (resourceId, resource) ->
            try {
                resource.close()
            } catch (ex: Exception) {
                warning("释放资源 $resourceId 时发生异常: ${ex.message}")
            }
        }
        resources.clear()
    }
}
