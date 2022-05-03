package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.database.UpdateStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Gives a player vote rewards
 */
public class GiveVoteCommand implements CommandExecutor {

    /**
     * Give the specified votes to the specified player
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

        // Set the player's vote
        if (setVote(p.getUniqueId(), args[1])) {
            sender.sendMessage(ChatColor.GREEN + "Vote " + ChatColor.YELLOW + args[1] +
                    ChatColor.GREEN + " was given to " + ChatColor.YELLOW + p.getName());
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Vote " + ChatColor.RED + args[1] +
                    ChatColor.DARK_RED + " is invalid!");
            return true;
        }

        // Don't update database if VotingPlugin sent the command
        if (args.length != 3 || !args[2].equalsIgnoreCase("ignore")) {
            StoreData.updateVotes(p.getUniqueId());
        }

        return true;
    }

    /**
     * Sets the specified vote for a player
     * @param uuid The unique ID of the player
     * @param vote The vote to set
     */
    private boolean setVote(UUID uuid, String vote) {
        PlayerData data = ActiveData.getData(uuid);

        switch (vote.toLowerCase()) {
            case "-1":
            case "remove":
                data.resetVotes();
                break;
            case "0":
            case "all":
                data.setVote("sword");
                data.setVote("boots");
                data.setVote("ladders");
                data.setVote("kits");
                break;
            case "1":
            case "sword":
                data.setVote("sword");
                break;
            case "2":
            case "boots":
                data.setVote("boots");
                break;
            case "3":
            case "ladders":
                data.setVote("ladders");
                break;
            case "4":
            case "kits":
                data.setVote("kits");
                break;
            default:
                return false;
        }

        return true;
    }
}
