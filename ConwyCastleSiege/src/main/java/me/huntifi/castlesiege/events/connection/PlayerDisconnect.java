package me.huntifi.castlesiege.events.connection;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.Permissions;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.ChatColor;
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
        UUID uuid = e.getPlayer().getUniqueId();
        if (ActiveData.getData(uuid) == null) {
            return;
        }

        // Set the leave message
        if (!ActiveData.getData(uuid).getLeaveMessage().isEmpty()) {
            e.setQuitMessage(ChatColor.YELLOW + ActiveData.getData(uuid).getLeaveMessage());
        }

        if (InCombat.isPlayerInCombat(uuid) && MapController.isOngoing()) {
            UpdateStats.addDeaths(uuid, 2);
        } else if (!InCombat.isPlayerInLobby(uuid) && MapController.isOngoing()) {
            UpdateStats.addDeaths(uuid, 1);
        }
        InCombat.playerDied(uuid);

        stopCapping(e.getPlayer());

        storeData(uuid);
        MapController.leaveTeam(uuid);
        Kit.equippedKits.remove(uuid);
        Permissions.removePlayer(uuid);
        BarCooldown.remove(uuid);
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
