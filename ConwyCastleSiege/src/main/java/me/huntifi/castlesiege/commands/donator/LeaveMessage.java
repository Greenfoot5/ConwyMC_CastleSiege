package me.huntifi.castlesiege.commands.donator;

import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages custom leave messages
 */
public class LeaveMessage implements CommandExecutor {

    /**
     * Change the player's custom leave message if valid arguments are supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether valid arguments are supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot set their leave message!");
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("reset")) {
            setMessage((Player) sender, "");
            return true;
        } else if (args.length < 2) {
            return false;
        } else if (args[0].equalsIgnoreCase("set")) {
            setMessage((Player) sender, String.join(" ", String.join(" ", args).split(" ", 2)[1]));
            return true;
        }

        return false;
    }

    /**
     * Set the player's custom leave message
     * @param p The player
     * @param message The message
     */
    private void setMessage(Player p, String message) {
        if (message.length() > 128) {
            p.sendMessage(ChatColor.DARK_RED + "Your message cannot be longer than 128 characters!");
            return;
        }

        ActiveData.getData(p.getUniqueId()).setLeaveMessage(message);
        if (message.isEmpty()) {
            p.sendMessage(ChatColor.GREEN + "Your leave message has been reset.");
        } else {
            p.sendMessage(ChatColor.GREEN + "Your leave message has been set to: " + ChatColor.YELLOW + message);
        }
    }
}
