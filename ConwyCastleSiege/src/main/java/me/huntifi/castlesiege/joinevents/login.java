package me.huntifi.castlesiege.joinevents;

import me.huntifi.castlesiege.kits.EnderchestRefill;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class login implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	public static ArrayList<Player> Playerlist = new ArrayList<Player>();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		
		p.setFoodLevel(20);
		p.setHealthScaled(true);
		
		if (!Playerlist.contains(p)) { Playerlist.add(p); }

		new BukkitRunnable() {

			@Override
			public void run() {

				EnderchestRefill.refill(p);

			}

		}.runTaskLater(plugin, 40);

		MapController.joinATeam(p);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (Playerlist.contains(p)) { Playerlist.remove(p); }
		
		MapController.leaveTeam(p);
	}
}
