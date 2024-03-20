package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.gameplay.VoteSkipCommand;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import me.huntifi.castlesiege.maps.objects.Ram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

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
        UUID uuid = e.getPlayer().getUniqueId();
        CSPlayerData data = ActiveData.getData(uuid);
        if (data == null) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Award deaths for logging out on the battlefield
            if (InCombat.isPlayerInCombat(uuid) && MapController.isOngoing()) {
                UpdateStats.addDeaths(uuid, 2);
            } else if (!InCombat.isPlayerInLobby(uuid) && MapController.isOngoing()) {
                UpdateStats.addDeaths(uuid, 1);
            }
            InCombat.playerDied(uuid);

            // Remove player from gameplay elements
            stopCapping(e.getPlayer());
            stopRamming(e.getPlayer());

            Kit.equippedKits.remove(uuid);
            storeData(uuid);
            MapController.leaveTeam(uuid);
            BarCooldown.remove(uuid);
            VoteSkipCommand.removePlayer(uuid);
        });
    }

    /**
     * Remove the player from all capping zones
     * @param p The player
     */
    private void stopCapping(Player p) {
        for (Flag flag : MapController.getCurrentMap().flags) {
            flag.playerExit(p);
        }
    }

    /**
     * Remove the player from all rams
     * @param p The player
     */
    private void stopRamming(Player p) {
        for (Gate gate : MapController.getCurrentMap().gates) {
            Ram ram = gate.getRam();
            if (ram != null)
                ram.playerExit(p);
        }
    }

    /**
     * Store the player's data in the database
     * Remove the player from active storage
     * @param uuid The unique ID of the player
     */
    private void storeData(UUID uuid) {
        try {
            StoreData.store(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
