package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Allows regular players to skip a map
 */
public class VoteSkipCommand implements TabExecutor {

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
            if (!MapController.getPlayers().contains(player.getUniqueId())) {
                Messenger.sendError("You must be playing to vote to skip!", sender);
                return;
            }

            if (MapController.getCurrentMap().hasMapEnded()) {
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            List<String> values = new ArrayList<>();
            values.add("cancel");
            StringUtil.copyPartialMatches(args[0], values, options);
            return options;
        }

        return null;
    }

    /**
     * Add a player's vote.
     * @param player The player
     */
    private void vote(Player player) {
        if (votedPlayers.add(player.getUniqueId())) {
            int requiredVotes = getRequiredVotes();
            Messenger.broadcastInfo(String.format("%s has voted to skip the current map! <yellow>(%d/%d)</yellow>" +
                            "<br>Use <yellow><click:run_command:/voteskip>/voteskip</click></yellow> to cast your vote",
                    player.getName(), votedPlayers.size(), requiredVotes));
            if (votedPlayers.size() >= requiredVotes)
                MapController.endMap();
            else
                Messenger.sendInfo("Changed your mind? You can cancel your vote with <yellow><click:run_command:/voteskip cancel>/voteskip cancel</click>", player);
        } else {
            Messenger.sendError("You have already voted! To cancel, use <yellow><click:run_command:/voteskip cancel>/voteckip cancel</click>", player);
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
        return (int) Math.ceil(MapController.getPlayers().size() * requiredVotePercentage);
    }
}
