package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.Permissions;
import me.huntifi.castlesiege.database.StoreData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                String player = args[0];
                String option = args[1];

                try {
                    // Get the player's stored rank points
                    double rp = LoadData.getRankPoints(args[0]);
                    if (rp < 0) {
                        sender.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
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
                            sender.sendMessage(ChatColor.DARK_RED + "The operation " + ChatColor.RED + args[1]
                                    + ChatColor.DARK_RED + " is not supported!");
                            sender.sendMessage(ChatColor.DARK_RED + "Please use one of the following: "
                                    + ChatColor.RED + "set, add, remove");
                            break;
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "The argument " + ChatColor.RED + args[2]
                            + ChatColor.DARK_RED + " is not a number!");
                } catch (SQLException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "An error occurred while trying to perform your command!");
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
        s.sendMessage(ChatColor.GREEN + name + " now has " + ChatColor.YELLOW + rp + ChatColor.GREEN + " rank points.");
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
                    if (i == 1) {
                        top.getFirst().close();
                        return  "High_King";
                    } else if (i <= 3) {
                        top.getFirst().close();
                        return  "King";
                    } else {
                        top.getFirst().close();
                        return  "Viceroy";
                    }
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
     * Get the donator rank that corresponds to the rank points
     * @param rp The amount of rank points
     * @return The corresponding donator rank
     */
    public static String getRank(double rp) {
        if (rp > 80) {
            return "Duke";
        } else if (rp > 60) {
            return "Count";
        } else if (rp > 40) {
            return "Baron";
        } else if (rp > 20) {
            return "Noble";
        } else if (rp > 0) {
            return "Esquire";
        } else {
            return "";
        }
    }
}
