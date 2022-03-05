package me.huntifi.castlesiege.Thunderstone.Rams;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class ThunderstoneRam implements Runnable {

	public static int rammingSpeed = 240;

	public static ArrayList<Player> rammingPlayers = new ArrayList<Player>();

	Location ramRadius = new Location(Bukkit.getServer().getWorld("Thunderstone"), 152, 65, 67);

	@Override
	public void run() {

		if(MapController.currentMapIs("Thunderstone")) {

			for (Player rammer : PlayerTeam.Cloudcrawlers) {

				Location rammerLoc = rammer.getLocation();

				if (rammerLoc.distance(ramRadius) <= 2) {
					
					if (rammingPlayers.size() <= 4)  {

					if (!rammingPlayers.contains(rammer)) { rammingPlayers.add(rammer);  }
					
					}
				}

			}
			
			if (rammingPlayers.size() == 1) {
				rammingSpeed = 240;
			} else if (rammingPlayers.size() == 2) {
				rammingSpeed = 200;
			} else if (rammingPlayers.size() == 3) {
				rammingSpeed = 150;
			} else if (rammingPlayers.size() == 4) {
				rammingSpeed = 100;
			}
			

			if (rammingPlayers.size() != 0) {

				for (Player rammers : PlayerTeam.Cloudcrawlers) {

					if (rammers != null) {

						Location rammerLoc = rammers.getLocation();

						if (rammerLoc.distance(ramRadius) > 2) {

							if (rammingPlayers.contains(rammers)) { rammingPlayers.remove(rammers); }

						}

					} else {
						
						return;
					}
				}
				
			} else {
				
				return;
				
			}
		}

	}

}
