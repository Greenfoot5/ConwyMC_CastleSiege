package me.huntifi.castlesiege.events.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

public class StoreData implements Listener {

    /**
     * Store the player's data in the database and remove them from active storage asynchronously
     * @param e The event called when a player quits the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        System.out.println("The event is called!");
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    UUID uuid = e.getPlayer().getUniqueId();
                    store(uuid);
                    ActiveData.removePlayer(uuid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Store the data of all players who are in active storage
     * Should only be called when shutting down the server
     */
    public void storeAll() {
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
     * Store the player's data in the database
     * @param uuid The unique ID of the player
     * @throws SQLException If something goes wrong executing the update
     */
    private void store(UUID uuid) throws SQLException {
        PlayerData data = ActiveData.getData(uuid);

        PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE player_stats SET SCORE = ?, KILLS = ?, DEATHS = ?, CAPTURES = ?, ASSISTS = ?, HEALS = ?, "
                        + "SUPPORTS = ?, MVPS = ?, SECRETS = ?, LEVEL = ?, KIT = ?, KILLSTREAK = ? WHERE UUID = ?");
        ps.setDouble(1, data.getScore());
        ps.setDouble(2, data.getKills());
        ps.setDouble(3, data.getDeaths());
        ps.setDouble(4, data.getCaptures());
        ps.setDouble(5, data.getAssists());
        ps.setDouble(6, data.getHeals());
        ps.setDouble(7, data.getSupports());
        ps.setInt(8, data.getMVPs());
        ps.setInt(9, data.getSecrets());
        ps.setInt(10, data.getLevel());
        ps.setString(11, data.getKit());
        ps.setInt(12, data.getMaxKillStreak());
        ps.setString(13, uuid.toString());
        ps.executeUpdate();
        ps.close();

        ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE player_rank SET RANK = ?, STAFFRANK = ?, RANKPOINTS = ?, COINS = ? WHERE UUID = ?");
        ps.setString(1, data.getRank());
        ps.setString(2, data.getStaffRank());
        ps.setDouble(3, data.getRankPoints());
        ps.setDouble(4, data.getCoins());
        ps.setString(5, uuid.toString());
        ps.executeUpdate();
        ps.close();
    }
}
