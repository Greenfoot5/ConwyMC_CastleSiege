package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the player limit per team for a kit
 */
public class SetKitLimitCommand implements CommandExecutor {

    /**
     * Set the player limit per team for a kit asynchronously
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        setKitLimit(sender, args);
        return true;
    }

    /**
     * Set the player limit per team for a kit if valid arguments were provided
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    private void setKitLimit(CommandSender sender, String[] args) {
        if (hasInvalidArguments(sender, args))
            return;

        Kit kit = Kit.getKit(args[0]);
        int limit = Integer.parseInt(args[1]);
        kit.setLimit(limit);

        if (limit < 0)
            Messenger.sendInfo(kit.name + "'s limit has been disabled", sender);
        else
            Messenger.sendInfo(kit.name + "'s limit has been set to " + limit, sender);
    }

    /**
     * Check if invalid arguments were supplied
     * @param sender Source of the command
     * @param args Passed command arguments
     * @return Whether invalid arguments were supplied
     */
    private boolean hasInvalidArguments(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messenger.sendError("Use: /setkitlimit <kit> <limit>", sender);
            return true;
        }

        if (Kit.getKit(args[0]) == null) {
            Messenger.sendError("Could not find the kit " + args[0], sender);
            return true;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            Messenger.sendError("Could not convert " + args[1] + " to a number.", sender);
            return true;
        }

        return false;
    }
}
