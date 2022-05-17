package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.RankPoints;
import me.huntifi.castlesiege.data_types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Store a player's data when they leave the game
 */
public class StoreData {

    /**
     * Store the player's data in the database
     * @param uuid The unique ID of the player
     * @throws SQLException If something goes wrong executing the update
     */
    public static void store(UUID uuid) throws SQLException {
        PlayerData data = ActiveData.getData(uuid);

        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE player_stats SET score = ?, kills = ?, deaths = ?, assists = ?, captures = ?, heals = ?, "
                        + "supports = ?, coins = ?, mvps = ?, secrets = ?, level = ?, kill_streak = ?, kit = ? WHERE uuid = ?");
        ps.setDouble(1, data.getScore());
        ps.setDouble(2, data.getKills());
        ps.setDouble(3, data.getDeaths());
        ps.setDouble(4, data.getAssists());
        ps.setDouble(5, data.getCaptures());
        ps.setDouble(6, data.getHeals());
        ps.setDouble(7, data.getSupports());
        ps.setDouble(8, data.getCoins());
        ps.setInt(9, data.getMVPs());
        ps.setInt(10, data.getSecrets());
        ps.setInt(11, data.getLevel());
        ps.setInt(12, data.getMaxKillStreak());
        ps.setString(13, data.getKit());
        ps.setString(14, uuid.toString());
        ps.executeUpdate();
        ps.close();

        ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE player_rank SET staff_rank = ?, rank_points = ?, join_message = ?, leave_message = ? WHERE uuid = ?");
        ps.setString(1, data.getStaffRank());
        ps.setDouble(2, data.getRankPoints());
        ps.setString(3, data.getJoinMessage());
        ps.setString(4, data.getLeaveMessage());
        ps.setString(5, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Store the data of all players who are in active storage
     * Should only be called when shutting down the server
     */
    public static void storeAll() {
        try {
            Collection<UUID> players = ActiveData.getPlayers();
            for (UUID uuid : players) {
                store(uuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the player name saved in the database
     * @param uuid The unique ID of the player
     * @param table The table to update
     */
    public static void updateName(UUID uuid, String table) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                            "UPDATE " + table + " SET name = ? WHERE uuid = ?");
                    ps.setString(1, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Update the player's donator rank saved in the database
     * @param name The name of the player
     */
    public static void updateRank(String name, double rp) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                            "UPDATE player_rank SET rank_points = ? WHERE name = ?");
                    ps.setDouble(1, rp);
                    ps.setString(2, name);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Update the player's votes saved in the database
     * @param uuid The unique ID of the player
     */
    public static void updateVotes(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Create the votes string
                    HashMap<String, Long> votes = ActiveData.getData(uuid).getVotes();
                    StringBuilder sb = new StringBuilder();
                    votes.forEach((key, value) -> {
                        if (sb.length() > 0) {
                            sb.append("%line%");
                        }
                        sb.append(key).append("//").append(value);
                    });

                    // Update the votes in the database
                    PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                            "UPDATE VotingPlugin_Users SET LastVotes = ? WHERE uuid = ?");
                    if (sb.length() > 0) {
                        ps.setString(1, sb.toString());
                    } else {
                        ps.setNull(1, Types.VARCHAR);
                    }
                    ps.setString(2, uuid.toString());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
