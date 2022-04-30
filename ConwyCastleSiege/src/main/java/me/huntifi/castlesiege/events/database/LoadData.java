package me.huntifi.castlesiege.events.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

/**
 * Load a player's data when they join the game
 */
public class LoadData implements Listener {

    /**
     * Load a player's data when they join the game
     * @param e The event called when a player joins the game
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        MVPStats.addPlayer(uuid);
        load(uuid);
        updateName(uuid, "player_stats");
        updateName(uuid, "player_rank");
    }

    /**
     * Load a player's stats and rank data
     * @param uuid The unique ID of the player
     */
    private void load(UUID uuid) {
        try {
            // Stats data
            createEntry(uuid, "player_stats");
            Tuple<PreparedStatement, ResultSet> prStats = getData(uuid, "player_stats");

            // Rank data
            createEntry(uuid, "player_rank");
            Tuple<PreparedStatement, ResultSet> prRank = getData(uuid, "player_rank");

            // Add retrieved data to active data
            PlayerData data = new PlayerData(prStats.getSecond(), prRank.getSecond());
            ActiveData.addPlayer(uuid, data);

            // Release resources
            prStats.getFirst().close();
            prRank.getFirst().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all data a table in the database
     * @param uuid The unique id of the player whose data to get
     * @param table The table to get the data from
     * @return A tuple of the prepared statement (to close later) and the query's result
     * @throws SQLException If something goes wrong executing the query
     */
    private Tuple<PreparedStatement, ResultSet> getData(UUID uuid, String table) throws SQLException {
        // Get player stats from the database
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "SELECT * FROM " + table + " WHERE UUID=?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();

        // Return result set with pointer on first (and only) row
        rs.next();
        return new Tuple<>(ps, rs);
    }

    /**
     * Create an entry in the player_stats table, does nothing if entry already exists
     * @param uuid The player for whom to create an entry
     * @param table The table to create an entry in
     * @throws SQLException If something goes wrong executing the insert
     */
    private void createEntry(UUID uuid, String table) throws SQLException {
        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "INSERT IGNORE INTO " + table + " (NAME, UUID) VALUES (?, ?)");
        ps.setString(1, Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
        ps.setString(2, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Update the player name saved in the database
     * @param uuid The unique ID of the user
     * @param table The table to update
     */
    private void updateName(UUID uuid, String table) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                            "UPDATE " + table + " SET NAME = ? WHERE UUID = ?");
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
}
