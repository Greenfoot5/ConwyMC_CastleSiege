package me.greenfoot5.castlesiege.events.connection;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Handles a player disconnecting from the game
 */
public class PlayerDisconnect implements Listener {

    /**
     * Store the player's data and remove them from their team
     * @param e The event called when a player leaves the game
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        CSPlayerData data = CSActiveData.getData(uuid);
        if (data == null) {
            return;
        }

        // Award deaths for logging out on the battlefield
        if (InCombat.isPlayerInCombat(uuid) && MapController.isOngoing()) {
            UpdateStats.addDeaths(uuid, 2);
        } else if (!InCombat.isPlayerInLobby(uuid) && MapController.isOngoing()) {
            UpdateStats.addDeaths(uuid, 1);
        }

        MapController.removePlayer(e.getPlayer());
    }
}
