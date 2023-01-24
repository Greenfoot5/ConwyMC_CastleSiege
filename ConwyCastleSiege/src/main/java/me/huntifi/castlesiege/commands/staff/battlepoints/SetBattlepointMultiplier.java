package me.huntifi.castlesiege.commands.staff.battlepoints;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the active coin multiplier
 */
public class SetBattlepointMultiplier implements CommandExecutor {

    /**
     * Set the active battlepoint multiplier, if a positive number is supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether a single argument is supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }

        try {
            double multiplier = Double.parseDouble(args[0]);
            if (multiplier < 0) {
                throw new NumberFormatException();
            }
            PlayerData.setBattlepointMultiplier(multiplier);
            Messenger.broadcastInfo("The battlepoint multiplier has been set to: " + ChatColor.BLUE + args[0]);
        } catch (NumberFormatException e) {
            Messenger.sendError("The argument " + ChatColor.RED + args[0]
                    + ChatColor.DARK_RED + " is not a positive number!", sender);
        }

        return true;
    }
}
