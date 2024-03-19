package me.huntifi.castlesiege.commands.staff.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Changes whether players are allowed to switch teams
 */
public class ToggleForcedRandom implements CommandExecutor {

    /**
     * Toggle regular switching between teams
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> toggle(sender, args));
        return true;
    }

    /**
     * Enable/disable randomDeath for all players if a correct amount of arguments is supplied
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void toggle(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Messenger.sendError("Use: /toggleforcedrandom [true/false]", sender);
            return;
        }

        if (args.length == 1)
            MapController.forcedRandom = Boolean.parseBoolean(args[0]);
        else
            MapController.forcedRandom = !MapController.forcedRandom;

        Messenger.broadcastInfo("/random on death is now " + (MapController.forcedRandom ? "enabled" : "disabled") + " for all players.");
    }
}
