package me.huntifi.castlesiege.Helmsdeep;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class HelmsdeepDeath implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player p = (Player) e.getPlayer();

		if(currentMaps.currentMapIs("HelmsDeep")) {

			if (e.getPlayer() instanceof Player) {
				
				MVPstats.addDeaths(p.getUniqueId(), 1);
				
				if (PlayerTeam.playerIsInTeam(p, 1)) {
					
					e.setRespawnLocation(new Location(plugin.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17));
					p.teleport(new Location(plugin.getServer().getWorld("HelmsDeep"), 1745, 14, 957, -95, -17));
					LobbyPlayer.addPlayer(p);
					
				}

				if (PlayerTeam.playerIsInTeam(p, 2)) {
					
					e.setRespawnLocation(new Location(plugin.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1));
					p.teleport(new Location(plugin.getServer().getWorld("HelmsDeep"), 277, 13, 987, -178, -1));
					LobbyPlayer.addPlayer(p);

				}	
			}

		}
	}

}
