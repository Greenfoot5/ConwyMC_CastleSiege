package me.huntifi.castlesiege.stats.levels;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class LevelSave {
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	 
	public static void saveLevel(Player p) {
		
		UUID uuid = p.getUniqueId();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			
			SQLStats.setLevel(uuid, StatsChanging.getLevel(uuid));
			
		});
		
	}

}
