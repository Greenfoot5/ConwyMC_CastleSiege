package me.huntifi.castlesiege.joinevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.stats.MVP.MVPstats;

public class newLogin implements Listener {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		UUID uuid = p.getUniqueId();
		
		if(!p.hasPlayedBefore()){
			
			//DO NOT TOUCH THIS, or you will ruin everyones stats
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			
			SQLStats.setScore(uuid, 0.0);
			SQLStats.setKills(uuid, 0.0);
			SQLStats.setDeaths(uuid, 0.0);
			SQLStats.setCaptures(uuid, 0.0);
			SQLStats.setHeals(uuid, 0.0);
			SQLStats.setSupports(uuid, 0.0);
			SQLStats.setAssists(uuid, 0.0);
			SQLStats.setLevel(uuid, 0);
			SQLStats.setMvps(uuid, 0);
			SQLStats.setSecrets(uuid, 0);
			SQLStats.setKillstreak(uuid, 0);
			SQLStats.setKit(uuid, "Swordsman");
			
			MVPstats.setScore(uuid, 0.0);
			MVPstats.setKills(uuid, 0.0);
			MVPstats.setDeaths(uuid, 0.0);
			MVPstats.setCaptures(uuid, 0.0);
			MVPstats.setHeals(uuid, 0.0);
			MVPstats.setSupports(uuid, 0.0);
			MVPstats.setAssists(uuid, 0.0);
			MVPstats.setKillstreak(uuid, 0);
			
			});
		}
		
	}

}
