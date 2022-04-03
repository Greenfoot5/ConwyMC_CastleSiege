package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class ThunderstoneLeave implements Listener {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (MapController.currentMapIs("Thunderstone")) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {

				LobbyPlayer.removePlayer(p);
				PlayerTeam.removePlayerFromTeam(p, 1);

			}

			if (PlayerTeam.playerIsInTeam(p, 2)) {

				LobbyPlayer.removePlayer(p);
				PlayerTeam.removePlayerFromTeam(p, 2);


			}


		}
	}

}
