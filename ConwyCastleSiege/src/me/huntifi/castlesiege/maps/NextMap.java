package me.huntifi.castlesiege.maps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Helmsdeep.flags.HelmsdeepReset;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMap;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneTimer;
import me.huntifi.castlesiege.Thunderstone.Flags.ThunderstoneReset;
import me.huntifi.castlesiege.joinevents.stats.MainStats;
import me.huntifi.castlesiege.stats.levels.LevelingEvent;

public class NextMap {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void nextMap() {

		if (currentMaps.currentMapIs("HelmsDeep")) {

			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p != null) {

					LevelingEvent.doLeveling();
					MainStats.updateStats(p.getUniqueId(), p);

				}	
			}
			
			HelmsdeepReset.onReset(); //reset the map
			
			ThunderstoneTimer.Minutes = 30;
			ThunderstoneTimer.Seconds = 3;
			ThunderstoneTimer.ThunderstoneTimerEvent();
			currentMaps.setMap("Thunderstone");
			
			ThunderstoneEndMap.TS_hasEnded = false;
			
			return;

		} else if (currentMaps.currentMapIs("Thunderstone")) {

			ThunderstoneReset.onReset(); //reset the map
			Bukkit.getServer().spigot().restart();
			
			return;
		}
 
	}

}
