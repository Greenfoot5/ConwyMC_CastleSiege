package me.huntifi.castlesiege.joinevents.stats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.stats.StatsEverything;
import me.huntifi.castlesiege.stats.levels.LevelSave;

public class MainStats {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static void updateStats(UUID uuid , Player p) {
		
		StatsEverything.updateScore(uuid, p);
		
		LevelSave.saveLevel(p);

		StatsEverything.LoadAllMvpStatsIntoDatabase(p);

		//Syncs the hashmaps with the total stats.

		StatsEverything.UpdateMainListsWithMvpStats(uuid);

	}
}


