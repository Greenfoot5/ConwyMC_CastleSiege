package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class ThunderstoneMVPupdater implements Runnable {
	
	@Override
	public void run() {

		if (MapController.currentMapIs("Thunderstone")) {

			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p != null) {

					if (PlayerTeam.playerIsInTeam(p, 1)) {
						
						if (ThunderstoneEndMVP.Cloudcrawlers.containsKey(p)) {
						
							ThunderstoneEndMVP.Cloudcrawlers.remove(p);
							ThunderstoneEndMVP.Cloudcrawlers.put(p, MVPstats.getScore(p.getUniqueId()));

						}
					}


					if (PlayerTeam.playerIsInTeam(p, 2)) {
						
						if (ThunderstoneEndMVP.ThunderstoneGuards.containsKey(p)) {

							ThunderstoneEndMVP.ThunderstoneGuards.remove(p);
							ThunderstoneEndMVP.ThunderstoneGuards.put(p, MVPstats.getScore(p.getUniqueId()));
							
						}
					}

				}

			}

		}

	}

}
