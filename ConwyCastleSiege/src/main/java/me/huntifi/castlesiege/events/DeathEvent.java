package me.huntifi.castlesiege.events;

import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathEvent implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Team team = MapController.getCurrentMap().getTeam(player.getUniqueId());

        MVPstats.addDeaths(player.getUniqueId(), 1);
        event.setRespawnLocation(team.lobby.spawnPoint);
        player.teleport(team.lobby.spawnPoint);

        Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
    }
}
