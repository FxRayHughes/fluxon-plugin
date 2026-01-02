package org.tabooproject.fluxon.platform.bukkit.function;

import org.bukkit.command.CommandSender;
import org.tabooproject.fluxon.runtime.Environment;
import org.tabooproject.fluxon.runtime.FluxonRuntime;

import java.util.Arrays;

/**
 * Fluxon
 * org.tabooproject.fluxon.extension.bukkit.function.game.Functions
 *
 * @author mical
 * @since 2025/7/21 20:48
 */
public class Functions {

    public Functions(final FluxonRuntime runtime) {
        runtime.registerFunction("tell", Arrays.asList(1, 2), context -> {
            final Environment env = context.getEnvironment();
            final Object[] args = context.getArguments();
            final CommandSender sender;
            final String message;
            if (context.getArguments().length > 1) {
                sender = (CommandSender) args[0];
                message = args[1].toString();
            } else {
                message = args[0].toString();
                Object find = env.getRootVariables().get("sender");
                if (find instanceof CommandSender) {
                    sender = (CommandSender) find;
                } else {
                    throw new IllegalStateException("No sender found in environment");
                }
            }
            sender.sendMessage(message);
            return null;
        });
    }
}