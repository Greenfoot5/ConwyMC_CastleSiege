package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.Permissions;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Gives a player a donator rank
 */
public class GiveRank implements CommandExecutor {

    /**
     * Give the specified donator rank to the specified player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return false;
        }

        // Get the target player
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
            return true;
        }

        // Set the player's donator rank
        if (Permissions.setDonatorPermission(p.getUniqueId(), args[1].toLowerCase())) {
            ActiveData.getData(p.getUniqueId()).setRank(args[1].toLowerCase());
            NameTag.give(p);
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Rank " + ChatColor.RED + args[1].toLowerCase() +
                    ChatColor.DARK_RED + " is invalid!");
        }
        return true;
    }
}
