package me.huntifi.castlesiege.commands;

import me.huntifi.castlesiege.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Adds the option to reload the plugin (does not work properly)
 */
public class ReloadCommand implements CommandExecutor {

    /**
     * Invokes the reload command in Main
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Main.instance.reload();
        return true;
    }
}
