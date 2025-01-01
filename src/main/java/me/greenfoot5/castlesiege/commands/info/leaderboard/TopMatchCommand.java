package me.greenfoot5.castlesiege.commands.info.leaderboard;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSStats;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.greenfoot5.castlesiege.commands.info.leaderboard.LeaderboardCommand.gradient;

/**
 * Shows the player the leaderboard for this match
 */
public class TopMatchCommand implements CommandExecutor {

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
                } else if (isTeam && MapController.isSpectator(((Player) sender).getUniqueId())) {
                    Messenger.sendError("You are not in a team!", sender);
                    return;
                }
                Team team = isTeam ? TeamController.getTeam(((Player) sender).getUniqueId()) : null;

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
                List<Tuple<UUID, CSStats>> stats = getStats(isTeam, team);
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
    private List<Tuple<UUID, CSStats>> getStats(boolean isTeam, @Nullable Team team) {
        List<Tuple<UUID, CSStats>> stats = new ArrayList<>();

        MVPStats.getStats().forEach(((uuid, playerData) -> {
            // Filter out players from other teams when required
            if (isTeam && !Objects.equals(team, TeamController.getTeam(uuid))) {
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
    private void display(CommandSender sender, List<Tuple<UUID, CSStats>> stats, String category, int requested) {
        // Sort if a valid category was supplied
        if (!sortStats(sender, stats, category))
            return;
        Collections.reverse(stats);

        // Create the message header
        Component message = Messenger.mm.deserialize(
                "<color:#CCCCCC>#. Player " +
                        "<transition:" + gradient + ":0>S</transition>core " +
                        "<transition:" + gradient + ":0.15>K</transition>ills " +
                        "<transition:" + gradient + ":0.4>D</transition>eaths " +
                        "<transition:" + gradient + ":0.6>K</transition>DR " +
                        "<transition:" + gradient + ":0.7>A</transition>ssits " +
                        "<transition:" + gradient + ":0.8>C</transition>aptures " +
                        "<transition:" + gradient + ":0.9>H</transition>eals " +
                        "<transition:" + gradient + ":1>S</transition>upports"
        );

        // Add the stat entries
        int first = requested < 6 ? 0 : requested - 5;
        for (int pos = first; pos < first + 10 && pos < stats.size(); pos++) {
            String name = Bukkit.getOfflinePlayer(stats.get(pos).getFirst()).getName();
            CSStats data = stats.get(pos).getSecond();
            String color = pos == requested ? "aqua>" : "dark_aqua>";
            message = message.append(addPlayer(name, data, pos, color));
        }

        // Send the message
        Messenger.send(message, sender);
    }

    private Component addPlayer(String name, CSStats d, int pos, String color) {
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");

        return Messenger.mm.deserialize("<br>" +
                "<gray>" + (pos + 1) + ". <" + color + name + " </" + color
                + "<transition:#13DB5D:#05B6D9:#F907FC:0>" + num.format(d.getScore()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.15>" + num.format(d.getKills()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.4>" + num.format(d.getDeaths()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.6>" + dec.format(d.getKills() / d.getDeaths()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.7>" + num.format(d.getAssists()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.8>" + num.format(d.getCaptures()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:0.9>" + num.format(d.getHeals()) + "</transition> "
                + "<transition:#13DB5D:#05B6D9:#F907FC:1>" + num.format(d.getSupports()) + "</transition>");
    }

    /**
     * Sort the supplied stats on the supplied category
     * @param sender Source of the command
     * @param stats The stats to be considered
     * @param category The category to sort the stats on
     * @return Whether a valid category was supplied
     */
    private boolean sortStats(CommandSender sender, List<Tuple<UUID, CSStats>> stats, String category) {
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
