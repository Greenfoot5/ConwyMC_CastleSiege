package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Gives a player vote rewards
 */
public class GiveVoteCommand implements TabExecutor {

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
            Messenger.sendError("Could not find player: <red>" + args[0], sender);
            return true;
        }

        // Set the player's vote
        if (setVote(p.getUniqueId(), args[1])) {
            Messenger.sendSuccess("Vote <yellow>" + args[1] + "</yellow> was given to <yellow>" + p.getName(), sender);
        } else {
            Messenger.sendError("Vote <red>" + args[1] + "</red> is invalid", sender);
            return true;
        }

        // Don't update database if VotingPlugin sent the command
        if (args.length != 3 || !args[2].equalsIgnoreCase("ignore")) {
            StoreData.updateVotes(p.getUniqueId());
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        // Command needs a player and a message
        if (args.length < 2) {
            return null;
        }

        if (args.length == 2) {
            List<String> values = new ArrayList<>();
            values.add("all");
            values.add("sword");
            values.add("boots");
            values.add("ladders");
            values.add("kits");
            values.add("remove");
            StringUtil.copyPartialMatches(args[1], values, options);
        }

        return options;
    }

    /**
     * Sets the specified vote for a player
     * @param uuid The unique ID of the player
     * @param vote The vote to set
     */
    private boolean setVote(UUID uuid, String vote) {
        CSPlayerData data = CSActiveData.getData(uuid);

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
            case "boot":
                data.setVote("boots");
                break;
            case "3":
            case "ladders":
            case "ladder":
                data.setVote("ladders");
                break;
            case "4":
            case "kits":
            case "kit":
                data.setVote("kits");
                break;
            default:
                return false;
        }

        return true;
    }
}
