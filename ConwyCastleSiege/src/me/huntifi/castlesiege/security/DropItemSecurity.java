package me.huntifi.castlesiege.security;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemSecurity implements Listener {
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		
		event.setCancelled(true);

	}
}
