package me.huntifi.castlesiege.events.join.stats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.stats.StatsEverything;
import me.huntifi.castlesiege.stats.levels.LevelSave;

public class StatsSaving implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void ClearStatsOnLeave(PlayerQuitEvent e) {

		Player p = e.getPlayer();
		
		UUID uuid = p.getUniqueId();

		MainStats.updateStats(uuid, p);
		
		LevelSave.saveLevel(p);

		StatsEverything.ClearMainStatsLists(uuid);


	}



}
