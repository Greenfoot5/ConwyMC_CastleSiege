package me.huntifi.castlesiege.Thunderstone;

import me.huntifi.castlesiege.maps.MapsList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.EndMapAction;
import me.huntifi.castlesiege.maps.MapController;

public class ThunderstoneEndMap implements Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static boolean TS_hasEnded = false; //determines whether this class should be run fully or not

	@Override
	public void run() {

		if (MapController.currentMapIs("Thunderstone")) {

			if (MapController.isMapEnded()) {

				//This means the Uruk-hai win, cause they have captured all flags!

				//HelmsdeepBallistaEvent.removeBallistaMinecart();

				if (TS_hasEnded == false) {

					ThunderstoneEndMVP.CloudcrawlersWin();

						EndMapAction.endMap(MapsList.Thunderstone);

					TS_hasEnded = true;

				}

			} else if (ThunderstoneTimer.hasGameEnded) {

				//This means Rohan wins, cause they had atleast one flag left by the end of the timer!

				//HelmsdeepBallistaEvent.removeBallistaMinecart();

				if (TS_hasEnded == false) {

					ThunderstoneEndMVP.ThunderstoneGuardsWin();

						EndMapAction.endMap(MapsList.Thunderstone);
					
					TS_hasEnded = true;

				}
			}
		}


	}

}
