package me.huntifi.castlesiege.stats.MVP;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StatsMvpJoinevent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (!MVPstats.Score.containsKey(uuid)) {
		
		MVPstats.setScore(uuid, 0.0);
		MVPstats.setKills(uuid, 0.0);
		MVPstats.setDeaths(uuid, 0.0);
		MVPstats.setCaptures(uuid, 0.0);
		MVPstats.setHeals(uuid, 0.0);
		MVPstats.setSupports(uuid, 0.0);
		MVPstats.setAssists(uuid, 0.0);
		MVPstats.setKillstreak(uuid, 0);
		
		}
	}

}
