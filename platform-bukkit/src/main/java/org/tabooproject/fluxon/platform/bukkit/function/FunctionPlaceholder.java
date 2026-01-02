package org.tabooproject.fluxon.platform.bukkit.function;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.tabooproject.fluxon.runtime.Environment;
import org.tabooproject.fluxon.runtime.FluxonRuntime;
import org.tabooproject.fluxon.runtime.NativeFunction;

import java.util.Arrays;

/**
 * Fluxon
 * org.tabooproject.fluxon.extension.bukkit.function.game.compat.FunctionScoreboard
 *
 * @author mical
 * @since 2025/7/21 18:18
 */
public class FunctionPlaceholder {

    public FunctionPlaceholder(final FluxonRuntime runtime) {
        final NativeFunction.NativeCallable<?> function = context -> {
            final Environment env = context.getEnvironment();
            final Object[] args = context.getArguments();
            final Player player;
            final String content;
            if (context.getArguments().length > 1) {
                player = (Player) args[0];
                content = args[1].toString();
            } else {
                content = args[0].toString();
                Object find = env.getRootVariables().get("player");
                if (find instanceof Player) {
                    player = (Player) find;
                } else {
                    throw new IllegalStateException("No player found in environment");
                }
            }
            return PlaceholderAPI.setPlaceholders(player, content);
        };
        runtime.registerFunction("papi", Arrays.asList(1, 2), function);
        runtime.registerFunction("placeholder", Arrays.asList(1, 2), function);
    }
}