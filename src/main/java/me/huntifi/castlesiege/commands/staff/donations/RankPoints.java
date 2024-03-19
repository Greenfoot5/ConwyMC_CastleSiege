package me.huntifi.castlesiege.commands.staff.donations;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.Permissions;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Manages a player's rank points
 */
public class RankPoints implements CommandExecutor {

    /**
     * Set, add to, or remove from the player's rank points
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return whether valid arguments were supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            return false;
        }

        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    // Get the player's stored rank points
                    double rp = LoadData.getRankPoints(args[0]);
                    if (rp < 0) {
                        Messenger.sendError("Could not find player: <red>" + args[0], sender);
                        return;
                    }

                    // Apply the provided operation with the corresponding amount
                    switch (args[1].toLowerCase()) {
                        case "set":
                            setRankPoints(sender, args[0], Math.max(Double.parseDouble(args[2]), 0));
                            break;
                        case "add":
                            setRankPoints(sender, args[0], Math.max(rp + Double.parseDouble(args[2]), 0));
                            break;
                        case "remove":
                            setRankPoints(sender, args[0], Math.max(rp - Double.parseDouble(args[2]), 0));
                            break;
                        default:
                            Messenger.sendError("The operation <red>" + args[1] +
                                    "</red> is not supported!<br>" +
                                    "Please use one of the following: <red>set, add, remove</red>", sender);
                            break;
                    }

                } catch (NumberFormatException e) {
                    Messenger.sendError("The argument <red>" + args[2]
                            + "</red> is not a number!", sender);
                } catch (SQLException e) {
                    Messenger.sendError("An error occurred while trying to perform your command!", sender);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Update the online and offline donator data
     * @param s Source of the command
     * @param name The name of the player
     * @param rp The rank points
     */
    private void setRankPoints(CommandSender s, String name, double rp) {
        StoreData.updateRank(name, rp);
        updateOnline(name, rp);
        Messenger.sendSuccess("<green>" + name + "</green> now has <yellow>" + rp + "</yellow> rank points.", s);
    }

    /**
     * Update a player's rank if they are online
     * @param name The name of the player
     * @param rp The rank points
     */
    public static void updateOnline(String name, double rp) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Ensure the player is online
                Player p = Bukkit.getPlayer(name);
                if (p == null) {
                    return;
                }

                // Ensure the player is actively being tracked
                UUID uuid = p.getUniqueId();
                PlayerData data = ActiveData.getData(uuid);
                if (data == null) {
                    return;
                }

                // Get the player's top donator rank
                String rank = "";
                if (rp > 0) {
                    rank = getTopRank(uuid);
                }

                // Apply the player's rank change
                data.setRankPoints(rp);
                if (rank.isEmpty()) {
                    data.setRank(getRank(rp));
                    Permissions.setDonatorPermission(uuid, getRank(rp));
                } else {
                    data.setRank(rank);
                    Permissions.setDonatorPermission(uuid, rank);
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Get the player's donator top rank
     * @param uuid The unique ID of the player
     * @return The player's donator top rank, empty string if not applicable
     */
    public static String getTopRank(UUID uuid) {
        try {
            Tuple<PreparedStatement, ResultSet> top = LoadData.getTopDonators();
            int i = 1;

            while (top.getSecond().next() && i <= 10) {
                if (top.getSecond().getString("uuid").equalsIgnoreCase(uuid.toString())) {
                    top.getFirst().close();
                    return getTopRank(i);
                }
                i++;
            }

            top.getFirst().close();
            return "";

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Get the donator rank that corresponds to the position
     * @param position The position on the donator leaderboard
     * @return The corresponding non-pretty donator rank
     */
    public static String getTopRank(int position) {
        if (position == 1) {
            return "high_king";
        } else if (position <= 3) {
            return "king";
        } else if (position <= 10) {
            return "viceroy";
        } else {
            return "";
        }
    }

    /**
     * Get the donator rank that corresponds to the rank points
     * @param rp The amount of rank points
     * @return The corresponding non-pretty donator rank
     */
    public static String getRank(double rp) {
        if (rp > 140) {
            return "duke";
        } else if (rp > 100) {
            return "count";
        } else if (rp > 60) {
            return "baron";
        } else if (rp > 20) {
            return "noble";
        } else if (rp >= 1) {
            return "esquire";
        } else {
            return "";
        }
    }
}
