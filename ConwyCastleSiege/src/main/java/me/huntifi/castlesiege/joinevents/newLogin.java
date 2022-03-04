package me.huntifi.castlesiege.joinevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLstats;
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
			
			SQLstats.setScore(uuid, 0.0);
			SQLstats.setKills(uuid, 0.0);
			SQLstats.setDeaths(uuid, 0.0);
			SQLstats.setCaptures(uuid, 0.0);
			SQLstats.setHeals(uuid, 0.0);
			SQLstats.setSupports(uuid, 0.0);
			SQLstats.setAssists(uuid, 0.0);
			SQLstats.setLevel(uuid, 0);
			SQLstats.setMvps(uuid, 0);
			SQLstats.setSecrets(uuid, 0);
			SQLstats.setKillstreak(uuid, 0);
			SQLstats.setKit(uuid, "Swordsman");
			
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
