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
 * Gives a player a staff rank
 */
public class SetStaffRank implements CommandExecutor {

    /**
     * Give the specified staff rank to the specified player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        // Get the target player
        Player p = Bukkit.getPlayer(args[0]);
        if (p == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
            return true;
        }

        // Set the player's staff rank
        String rank = args.length == 1 || args[1].equalsIgnoreCase("none") ? "" : args[1].toLowerCase();
        if (Permissions.setStaffPermission(p.getUniqueId(), rank)) {
            ActiveData.getData(p.getUniqueId()).setStaffRank(rank);
            NameTag.give(p);
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Rank " + ChatColor.RED + rank +
                    ChatColor.DARK_RED + " is invalid!");
        }
        return true;
    }
}
