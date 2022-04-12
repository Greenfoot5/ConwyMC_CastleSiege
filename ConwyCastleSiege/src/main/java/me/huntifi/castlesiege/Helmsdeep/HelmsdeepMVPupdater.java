package me.huntifi.castlesiege.Helmsdeep;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class HelmsdeepMVPupdater implements Runnable {

	@Override
	public void run() {

		if (MapController.currentMapIs("HelmsDeep")) {

			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p != null) {

					/*if (PlayerTeam.playerIsInTeam(p, 1)) {
						
						if (HelmsdeepEndMVP.Urukhai.containsKey(p)) {
						
							HelmsdeepEndMVP.Urukhai.remove(p);
							HelmsdeepEndMVP.Urukhai.put(p, MVPstats.getScore(p.getUniqueId()));

						}
					}


					if (PlayerTeam.playerIsInTeam(p, 2)) {
						
						if (HelmsdeepEndMVP.Rohan.containsKey(p)) {

							HelmsdeepEndMVP.Rohan.remove(p);
							HelmsdeepEndMVP.Rohan.put(p, MVPstats.getScore(p.getUniqueId()));
							
						}
					}*/

				}

			}

		}

	}

}
