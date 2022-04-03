package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class ThunderstoneDeath implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player p = (Player) e.getPlayer();

		if(MapController.currentMapIs("Thunderstone")) {

			if (e.getPlayer() instanceof Player) {
				
				MVPstats.addDeaths(p.getUniqueId(), 1);
				
				if (PlayerTeam.playerIsInTeam(p, 1)) {
					
					e.setRespawnLocation(new Location(plugin.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1));
					p.teleport(new Location(plugin.getServer().getWorld("Thunderstone"), -191, 202, 159, -90, 1));
					LobbyPlayer.addPlayer(p);
					
				}

				if (PlayerTeam.playerIsInTeam(p, 2)) {
					
					e.setRespawnLocation(new Location(plugin.getServer().getWorld("Thunderstone"), -187, 202, 106, -90, 2));
					p.teleport(new Location(plugin.getServer().getWorld("Thunderstone"), -187, 202, 106, -90, 2));
					LobbyPlayer.addPlayer(p);

				}	
			}

		}
	}

}
