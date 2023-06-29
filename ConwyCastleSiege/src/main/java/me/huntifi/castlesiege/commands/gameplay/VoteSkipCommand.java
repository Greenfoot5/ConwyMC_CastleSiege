package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

/**
 * Allows regular players to skip a map
 */
public class VoteSkipCommand implements CommandExecutor {

    public static final double requiredVotePercentage = 0.6;
    private static final HashSet<UUID> votedPlayers = new HashSet<>();

    /**
     * Vote to skip the current map.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (sender instanceof ConsoleCommandSender) {
                Messenger.sendError("Console cannot vote to skip a map! Use /nextmap to force it instead.", sender);
                return;
            }

            Player player = (Player) sender;
            if (MapController.isSpectator(player.getUniqueId())) {
                Messenger.sendError("Spectators cannot vote to skip a map!", sender);
                return;
            }

            if (MapController.hasMapEnded()) {
                Messenger.sendError("The current map has already ended!", sender);
                return;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("cancel")) {
                cancelVote(player);
            } else {
                vote(player);
            }
        });

        return true;
    }

    /**
     * Add a player's vote.
     * @param player The player
     */
    private void vote(Player player) {
        if (votedPlayers.add(player.getUniqueId())) {
            int requiredVotes = getRequiredVotes();
            Messenger.broadcastInfo(String.format("%s has voted to skip the current map! %s(%d/%d)",
                    player.getName(), ChatColor.YELLOW, votedPlayers.size(), requiredVotes));
            if (votedPlayers.size() >= requiredVotes)
                MapController.endMap();
            else
                Messenger.sendInfo("Changed your mind? You can cancel your vote with /voteskip cancel", player);
        } else {
            Messenger.sendError("You have already voted! To cancel, use /voteskip cancel", player);
        }
    }

    /**
     * Cancel a player's vote.
     * @param player The player
     */
    private void cancelVote(Player player) {
        if (votedPlayers.remove(player.getUniqueId())) {
            Messenger.sendInfo("Your vote has been cancelled!", player);
        } else {
            Messenger.sendError("There was no vote to cancel!", player);
        }
    }

    /**
     * Clear all votes.
     */
    public static void clearVotes() {
        votedPlayers.clear();
    }

    /**
     * Remove a player's vote and recalculate the votes.
     * @param uuid The unique ID of the player
     */
    public static void removePlayer(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            votedPlayers.remove(uuid);

            int requiredVotes = getRequiredVotes();
            if (requiredVotes > 0 && votedPlayers.size() >= requiredVotes) {
                Messenger.broadcastInfo("Due to a shift in required votes, the current map is skipped!");
                MapController.endMap();
            }
        });
    }

    /**
     * Get the amount of required votes.
     * @return The amount of required votes
     */
    private static int getRequiredVotes() {
        int playerCount = 0;

        for (Team team : MapController.getCurrentMap().teams) {
            playerCount += team.getTeamSize();
        }

        return (int) Math.ceil(playerCount * requiredVotePercentage);
    }
}
