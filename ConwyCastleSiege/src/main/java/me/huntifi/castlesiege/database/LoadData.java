package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Load a player's data when they join the game
 * These methods should only be called asynchronously
 */
public class LoadData {

    /**
     * Load a player's stats and rank data
     * @param uuid The unique ID of the player
     */
    public static PlayerData load(UUID uuid) {
        try {
            // Mute data
            Tuple<PreparedStatement, ResultSet> prMute = Punishments.getActive(uuid, "mute");

            // Stats data
            createEntry(uuid, "player_stats");
            Tuple<PreparedStatement, ResultSet> prStats = getData(uuid, "player_stats");

            // Rank data
            createEntry(uuid, "player_rank");
            Tuple<PreparedStatement, ResultSet> prRank = getData(uuid, "player_rank");

            // Votes data
            createEntry(uuid, "VotingPlugin_Users");
            HashMap<String, Long> votes = getVotes(uuid);

            // Collect data and release resources
            PlayerData data = new PlayerData(prMute.getSecond(), prStats.getSecond(), prRank.getSecond(), votes);
            prMute.getFirst().close();
            prStats.getFirst().close();
            prRank.getFirst().close();

            return data;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a user's data from a table in the database
     * @param uuid The unique id of the player whose data to get
     * @param table The table to get the data from
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    private static Tuple<PreparedStatement, ResultSet> getData(UUID uuid, String table) throws SQLException {
        // Get player stats from the database
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM " + table + " WHERE uuid=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Return result set with pointer on first (and only) row
        rs.next();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the vote data from the database
     * @param uuid The unique id of the player whose data to get
     * @return The votes string saved in the database
     * @throws SQLException If something goes wrong executing the query
     */
    private static HashMap<String, Long> getVotes(UUID uuid) throws SQLException {
        // Get votes from the database
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT LastVotes FROM VotingPlugin_Users WHERE uuid=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Get votes from the query result
        HashMap<String, Long> votes = new HashMap<>();
        if (rs.next() && rs.getString(1) != null) {
            String[] voteArray = rs.getString(1).split("%line%");
            for (String vote : voteArray) {
                if (Long.parseLong(vote.split("//")[1]) > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
                    votes.put(vote.split("//")[0], Long.parseLong(vote.split("//")[1]));
                }
            }
        }

        ps.close();
        return votes;
    }

    /**
     * Create an entry in the specified table, does nothing if entry already exists
     * @param uuid The player for whom to create an entry
     * @param table The table to create an entry in
     * @throws SQLException If something goes wrong executing the insert
     */
    private static void createEntry(UUID uuid, String table) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "INSERT IGNORE INTO " + table + " (uuid) VALUES (?)");
        ps.setString(1, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Get the top players from the database
     * @param order The category to order by
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getTop(String order) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM player_stats ORDER BY " + order + " DESC LIMIT 10");

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }

    /**
     * Get the UUID of a player from our database
     * @param name The name of the player
     * @return The player's UUID, or null if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static UUID getUUID(String name) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT uuid FROM player_rank WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        UUID uuid;
        if (rs.next()) {
            uuid = UUID.fromString(rs.getString(1));
        } else {
            uuid = null;
        }

        ps.close();
        return uuid;
    }

    /**
     * Get the rank points of a player from our database
     * @param name The name of the player
     * @return The player's rank points, or -1 if the name is not in the database
     * @throws SQLException If something goes wrong executing the query
     */
    public static double getRankPoints(String name) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT rank_points FROM player_rank WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        double rankPoints;
        if (rs.next()) {
            rankPoints = rs.getDouble(1);
        } else {
            rankPoints = -1;
        }

        ps.close();
        return rankPoints;
    }

    /**
     * Get the top 10 donators
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    public static Tuple<PreparedStatement, ResultSet> getTopDonators() throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM player_rank ORDER BY rank_points DESC LIMIT 10");

        ResultSet rs = ps.executeQuery();
        return new Tuple<>(ps, rs);
    }
}
