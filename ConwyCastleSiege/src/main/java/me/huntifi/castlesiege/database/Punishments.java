package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;

import java.net.InetAddress;
import java.sql.*;
import java.util.UUID;

/**
 * Handles all punishment-data related actions
 */
public class Punishments {

    /**
     * Get a player's current ban from the database
     * @param uuid The unique ID of the player whose data to get
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getBan(UUID uuid) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT reason, end FROM punishments WHERE uuid = ? AND type = 'ban' AND end > ?"
                        + " ORDER BY end DESC LIMIT 1");
        ps.setString(1, uuid.toString());
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get an IP's current ban from the database
     * @param ip The IP whose data to get
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getIPBan(InetAddress ip) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT reason, end FROM punishments WHERE ip = ? AND type = 'ban' AND end > ?"
                        + " ORDER BY end DESC LIMIT 1");
        ps.setString(1, ip.toString());
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Add a player's punishment to the database
     * @param name The name of the player
     * @param uuid The unique ID of the player
     * @param ip The ip address of the player, null if offline
     * @param type The type of punishment
     * @param reason The reason for the punishment
     * @param duration The duration of the punishment in milliseconds
     * @throws SQLException If something goes wrong executing the insert
     */
    public static void add(String name, UUID uuid, InetAddress ip, String type, String reason, long duration) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "INSERT INTO punishments VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, uuid.toString());
        ps.setString(3, ip == null ? "" : ip.toString());
        ps.setString(4, type);
        ps.setString(5, reason);
        ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        ps.setTimestamp(7, new Timestamp(System.currentTimeMillis() + duration));

        ps.executeUpdate();
        ps.close();
    }

    /**
     * Unban a player
     * @param name The name of the player
     * @throws SQLException If something goes wrong executing the insert
     */
    public static void unban(String name) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE punishments SET end = ? WHERE end > ? AND name = ? ");
        ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
        ps.setString(3, name);

        ps.executeUpdate();
        ps.close();
    }
}
