package me.huntifi.castlesiege.security;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class instarespawn implements Listener {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void a(PlayerDeathEvent event) {

		new BukkitRunnable() {

			@Override
			public void run() {

				final Player player = event.getEntity();

				player.spigot().respawn();

			}

		}.runTaskLater(plugin, 10);

	}

}
