package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Changes whether players are allowed to switch teams
 */
public class ToggleAllKitsFreeCommand implements CommandExecutor {

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
     * Enable/disable all kits free if a correct amount of arguments is supplied
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void toggle(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Messenger.sendError("Use: /togglenolimits [true/false]", sender);
            return;
        }

        if (args.length == 1)
            MapController.allKitsFree = Boolean.parseBoolean(args[0]);
        else
            MapController.allKitsFree = !MapController.allKitsFree;

        Messenger.broadcastInfo("All donator & team kits are " + (MapController.allKitsFree ? "now temporarily free!" : "no longer free."));
    }
}
