package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 *
 */
public class PlayerDisconnect implements Listener {

    /**
     * Store the player's data and remove them from their team
     * @param e The event called when a player leaves the game
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);
        if (data == null) {
            return;
        }

        MapController.removePlayer(e.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Award deaths for logging out on the battlefield
            if (InCombat.isPlayerInCombat(uuid) && MapController.isOngoing()) {
                UpdateStats.addDeaths(uuid, 2);
            } else if (!InCombat.isPlayerInLobby(uuid) && MapController.isOngoing()) {
                UpdateStats.addDeaths(uuid, 1);
            }
        });
    }
}
