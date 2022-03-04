package me.huntifi.castlesiege.security;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class voidOfLimits implements Listener {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMcLobby");
	
	
	
	@EventHandler
	public void onPlayerWalkPad(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();

		Location loc = p.getLocation();
		
		if (loc.getY() < 0) {
			
			p.setHealth(0);
			
		}
		
	}

}
