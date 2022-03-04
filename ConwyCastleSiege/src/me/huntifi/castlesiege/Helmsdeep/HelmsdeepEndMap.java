package me.huntifi.castlesiege.Helmsdeep;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.EndMapAction;
import me.huntifi.castlesiege.maps.currentMaps;

public class HelmsdeepEndMap implements Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	public static boolean HD_hasEnded = false; //determines whether this class should be run fully or not

	@Override
	public void run() {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			if (currentMaps.isMapEnded()) {

				//This means the Uruk-hai win, cause they have captured all flags!

				//HelmsdeepBallistaEvent.removeBallistaMinecart();
				
				if (HD_hasEnded == false) {

						HelmsdeepEndMVP.UrukhaiWin();
							
						EndMapAction.HelmsdeepEndMap();

						
						HD_hasEnded = true;
				}

			} else if (HelmsdeepTimer.hasGameEnded) {

				//This means Rohan wins, cause they had at least one flag left by the end of the timer!

				//HelmsdeepBallistaEvent.removeBallistaMinecart();
					
					if (HD_hasEnded == false) {

						HelmsdeepEndMVP.RohanWin();
				
					    EndMapAction.HelmsdeepEndMap();
						
						HD_hasEnded = true;
				}

			}
		}


	}

}
