package me.huntifi.castlesiege.events;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathEvent implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Team team = MapController.getCurrentMap().getTeam(player.getUniqueId());

        assert team != null && team.lobby.spawnPoint != null;
        if (team.lobby.spawnPoint.getWorld() == null) {
            team.lobby.spawnPoint.setWorld(Bukkit.getWorld(MapController.getCurrentMap().worldName));
        }

        //MVPstats.addDeaths(player.getUniqueId(), 1);
        event.setRespawnLocation(team.lobby.spawnPoint);
        player.teleport(team.lobby.spawnPoint);

        Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        new BukkitRunnable() {

            @Override
            public void run() {
                Player player = event.getEntity();
                player.spigot().respawn();
            }

        }.runTaskLater(Main.plugin, 10);
    }
}
