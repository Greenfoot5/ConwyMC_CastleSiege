package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.data_types.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the active coin multiplier
 */
public class SetCoinMultiplier implements CommandExecutor {

    /**
     * Set the active coin multiplier, if a positive number is supplied
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
            PlayerData.setCoinMultiplier(multiplier);
            sender.sendMessage(ChatColor.GREEN + "The coin multiplier has been set to: " + ChatColor.YELLOW + args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "The argument " + ChatColor.RED + args[0]
                    + ChatColor.DARK_RED + " is not a positive number!");
        }

        return true;
    }
}
