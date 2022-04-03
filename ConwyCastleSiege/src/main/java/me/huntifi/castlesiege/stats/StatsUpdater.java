package me.huntifi.castlesiege.stats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;


public class StatsUpdater {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static void statUpdate() {

			for (Player p : Bukkit.getOnlinePlayers()) {

				UUID uuid = p.getUniqueId();

				if (p != null) {

					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {	SQLStats.setScore(uuid, returnMainScore(p)); });
			

				}


			}

		
	}
	
	public static double returnMainScore (Player p) {
		
		UUID uuid = p.getUniqueId();
		
		double kills = StatsChanging.getKills(uuid);
		double heals = StatsChanging.getHeals(uuid);
		double deaths = StatsChanging.getDeaths(uuid);
		double assists = StatsChanging.getAssists(uuid);
		double supports = StatsChanging.getSupports(uuid);
		double captures = StatsChanging.getCaptures(uuid);
		
		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);
		
		return totalScore;
		
		
	}


}
