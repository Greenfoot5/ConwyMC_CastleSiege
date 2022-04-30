package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

/**
 *
 */
public class PlayerDisconnect implements Listener {

    /**
     * Store the player's data and remove them from their team
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        storeData(p.getUniqueId());
        MapController.leaveTeam(p.getUniqueId());
    }

    /**
     * Store the player's data in the database
     * Remove the player from active storage
     * @param uuid The unique ID of the player
     */
    private void storeData(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    StoreData.store(uuid);
                    ActiveData.removePlayer(uuid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
