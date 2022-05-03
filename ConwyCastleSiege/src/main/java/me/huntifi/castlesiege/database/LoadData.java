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
 */
public class LoadData {

    /**
     * Load a player's stats and rank data
     * @param uuid The unique ID of the player
     */
    public static PlayerData load(UUID uuid) {
        try {
            // Stats data
            createEntry(uuid, "player_stats");
            Tuple<PreparedStatement, ResultSet> prStats = getData(uuid, "player_stats");

            // Rank data
            createEntry(uuid, "player_rank");
            Tuple<PreparedStatement, ResultSet> prRank = getData(uuid, "player_rank");

            // Collect data and release resources
            PlayerData data = new PlayerData(prStats.getSecond(), prRank.getSecond(), getVotes(uuid));
            prStats.getFirst().close();
            prRank.getFirst().close();

            return data;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data a table in the database
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
        if (rs.next()) {
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
     * Create an entry in the player_stats table, does nothing if entry already exists
     * @param uuid The player for whom to create an entry
     * @param table The table to create an entry in
     * @throws SQLException If something goes wrong executing the insert
     */
    private static void createEntry(UUID uuid, String table) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "INSERT IGNORE INTO " + table + " (name, uuid) VALUES (?, ?)");
        ps.setString(1, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
        ps.setString(2, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }
}
