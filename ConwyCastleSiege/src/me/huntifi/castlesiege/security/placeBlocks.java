package me.huntifi.castlesiege.security;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class placeBlocks implements Listener {
	
	 @EventHandler
	 public void onBlockPlaced(BlockPlaceEvent e) {
		 
		 Player p = e.getPlayer();
		 
			if (!(p.hasPermission("moderator"))) {
				
			e.setCancelled(true);
			
			} else if (p.hasPermission("moderator")) {
				
			e.setCancelled(false);
				
			}
		 
		 
	 }

}
