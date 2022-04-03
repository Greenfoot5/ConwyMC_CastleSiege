package me.huntifi.castlesiege.stats.levels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RegisterLevel implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onJoinLevel(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		new BukkitRunnable() {

			@Override
			public void run() {

				LevelingEvent.levelPlayer(p);

			}
		}.runTaskLater(plugin, 40);
	}

}
