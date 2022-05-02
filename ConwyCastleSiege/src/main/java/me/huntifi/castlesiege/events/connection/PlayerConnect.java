package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.*;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Handles what happens when someone logs in
 */
public class PlayerConnect implements Listener {

    /**
     * Assign the player to a team
     * Give the player their last used kit
     * @param e The event called when a player join the game
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Load the player's data
        PlayerData data = LoadData.load(uuid);
        assert data != null;
        ActiveData.addPlayer(uuid, data);
        MVPStats.addPlayer(uuid);

        // Join a team and assign kit
        InCombat.playerDied(uuid);
        MapController.joinATeam(uuid);
        p.performCommand(data.getKit());

        // Assign the player's staff and donator permissions
        Permissions.addPlayer(uuid);
        Permissions.setStaffPermission(uuid, data.getStaffRank());
        Permissions.setDonatorPermission(uuid, data.getRank());

        // Update the names stored in the database
        StoreData.updateName(uuid, "player_stats");
        StoreData.updateName(uuid, "player_rank");
    }
}
