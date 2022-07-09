package me.huntifi.castlesiege.commands.info.leaderboard;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.SpectateCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Shows the player the leaderboard for this match
 */
public class TopMatch implements CommandExecutor {

    /**
     * Print the score leaderboard to the player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Run asynchronously because some operations might take some time
        new BukkitRunnable() {
            @Override
            public void run() {
                // Player must be in a team to use TopTeam
                boolean isTeam = cmd.getName().equalsIgnoreCase("TopTeam");
                if (isTeam && !(sender instanceof Player)) {
                    Messenger.sendError("Console is not in a team!", sender);
                    return;
                } else if (isTeam && SpectateCommand.spectators.contains(((Player) sender).getUniqueId())) {
                    Messenger.sendError("You are not in a team!", sender);
                    return;
                }
                Team team = isTeam ? MapController.getCurrentMap().getTeam(((Player) sender).getUniqueId()) : null;

                // Assign the command parameters to variables
                String category = args.length >= 1 ? args[0] : "score";
                String posString = args.length >= 2 ? args[1] : "-1";
                int position;
                try {
                    position = Integer.parseInt(posString);
                } catch (NumberFormatException e) {
                    Messenger.sendError("Searching by player is currently not supported!", sender);
                    return;
                }

                // Print the requested leaderboard to the player
                List<Tuple<UUID, PlayerData>> stats = getStats(isTeam, team);
                display(sender, stats, category, position);
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Get the MVP stats that should be shown to the player
     * @param isTeam Whether only players from the same team should be considered
     * @param team The player's team
     * @return All player data that is allowed to be shown to the player and the corresponding UUID
     */
    private List<Tuple<UUID, PlayerData>> getStats(boolean isTeam, @Nullable Team team) {
        List<Tuple<UUID, PlayerData>> stats = new ArrayList<>();

        MVPStats.getStats().forEach(((uuid, playerData) -> {
            // Filter out players from other teams when required
            if (isTeam && !Objects.equals(team, MapController.getCurrentMap().getTeam(uuid))) {
                return;
            }
            stats.add(new Tuple<>(uuid, playerData));
        }));

        return stats;
    }

    /**
     * Print the leaderboard to the player, sorted by the supplied category
     * @param sender Source of the command
     * @param stats The stats to be considered
     * @param category The category to sort the stats on
     * @param requested The requested position
     */
    private void display(CommandSender sender, List<Tuple<UUID, PlayerData>> stats, String category, int requested) {
        // Sort if a valid category was supplied
        if (!sortStats(sender, stats, category))
            return;
        Collections.reverse(stats);

        // Create the message header
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.AQUA).append("#. Player ")
                .append(ChatColor.WHITE).append("S").append(ChatColor.AQUA).append("core ")
                .append(ChatColor.GREEN).append("K").append(ChatColor.AQUA).append("ills ")
                .append(ChatColor.RED).append("D").append(ChatColor.AQUA).append("eaths ")
                .append(ChatColor.YELLOW).append("K").append(ChatColor.AQUA).append("DR ")
                .append(ChatColor.DARK_GREEN).append("A").append(ChatColor.AQUA).append("ssists ")
                .append(ChatColor.GRAY).append("C").append(ChatColor.AQUA).append("aptures ")
                .append(ChatColor.LIGHT_PURPLE).append("H").append(ChatColor.AQUA).append("eals ")
                .append(ChatColor.DARK_PURPLE).append("S").append(ChatColor.AQUA).append("upports");

        // Add the stat entries
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");
        int first = requested < 6 ? 0 : requested - 5;
        for (int pos = first; pos < first + 10 && pos < stats.size(); pos++) {
            String name = Bukkit.getOfflinePlayer(stats.get(pos).getFirst()).getName();
            PlayerData data = stats.get(pos).getSecond();
            ChatColor color = pos == requested ? ChatColor.AQUA : ChatColor.DARK_AQUA;
            message.append("\n").append(ChatColor.GRAY).append(pos + 1).append(". ")
                    .append(color).append(name).append(" ")
                    .append(ChatColor.WHITE).append(num.format(data.getScore())).append(" ")
                    .append(ChatColor.GREEN).append(num.format(data.getKills())).append(" ")
                    .append(ChatColor.RED).append(num.format(data.getDeaths())).append(" ")
                    .append(ChatColor.YELLOW).append(dec.format(data.getKills() / data.getDeaths())).append(" ")
                    .append(ChatColor.DARK_GREEN).append(num.format(data.getAssists())).append(" ")
                    .append(ChatColor.GRAY).append(num.format(data.getCaptures())).append(" ")
                    .append(ChatColor.LIGHT_PURPLE).append(num.format(data.getHeals())).append(" ")
                    .append(ChatColor.DARK_PURPLE).append(num.format(data.getSupports()));
        }

        // Send the message
        sender.sendMessage(message.toString());
    }

    /**
     * Sort the supplied stats on the supplied category
     * @param sender Source of the command
     * @param stats The stats to be considered
     * @param category The category to sort the stats on
     * @return Whether a valid category was supplied
     */
    private boolean sortStats(CommandSender sender, List<Tuple<UUID, PlayerData>> stats, String category) {
        switch (category.toLowerCase()) {
            case "score":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getScore()));
                return true;
            case "captures":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getCaptures()));
                return true;
            case "deaths":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getDeaths()));
                return true;
            case "kills":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getKills()));
                return true;
            case "assists":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getAssists()));
                return true;
            case "heals":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getHeals()));
                return true;
            case "supports":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getSupports()));
                return true;
            case "kdr":
                stats.sort(Comparator.comparingDouble(t -> t.getSecond().getKills() / t.getSecond().getDeaths()));
                return true;
            default:
                Messenger.sendError(String.format("The category %s does not exist!", category), sender);
                return false;
        }
    }
}
