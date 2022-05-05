package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.*;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Handles what happens when someone logs in
 */
public class PlayerConnect implements Listener {

    /**
     * Load the player's data
     * Assign the player to a team
     * @param e The event called when a player join the game
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Load data and perform related actions
        Player p = e.getPlayer();
        loadData(p);

        // Join team
        UUID uuid = p.getUniqueId();
        MapController.joinATeam(uuid);

    }

    /**
     * Load the player's data
     * Apply stored data
     * Update the player's name in the database
     * @param p The player
     */
    private void loadData(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Load the player's data
                UUID uuid = p.getUniqueId();
                PlayerData data = LoadData.load(uuid);
                assert data != null;
                ActiveData.addPlayer(uuid, data);
                MVPStats.addPlayer(uuid);

                // Assign the player's staff and donator permissions
                Permissions.addPlayer(uuid);
                Permissions.setStaffPermission(uuid, data.getStaffRank());
                Permissions.setDonatorPermission(uuid, data.getRank());

                // Assign stored kit
                InCombat.playerDied(uuid);
                p.performCommand(data.getKit());

                // Apply the correct name representation
                NameTag.give(p);

                // Update the names stored in the database
                StoreData.updateName(uuid, "player_stats");
                StoreData.updateName(uuid, "player_rank");
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
