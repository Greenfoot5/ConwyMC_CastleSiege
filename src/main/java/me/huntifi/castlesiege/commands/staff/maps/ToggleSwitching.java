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
public class ToggleSwitching implements CommandExecutor {

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> toggleSwitching(sender, args));
        return true;
    }

    /**
     * Enable/disable switching if a correct amount of arguments is supplied
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void toggleSwitching(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Messenger.sendError("Use: /toggleswitching [true/false]", sender);
            return;
        }

        if (args.length == 1)
            MapController.disableSwitching = Boolean.parseBoolean(args[0]);
        else
            MapController.disableSwitching = !MapController.disableSwitching;

        Messenger.broadcastInfo("Switching is now " + (MapController.disableSwitching ? "disabled." : "enabled."));
    }
}
