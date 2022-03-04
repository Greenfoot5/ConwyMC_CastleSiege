package me.huntifi.castlesiege.security;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoFireDestroy implements Listener {
	
	@EventHandler
	public void onPlayerInteractFire(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	     
	    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
	        if (player.getTargetBlock((Set<Material>) null, 5).getType() == Material.FIRE) {
	        	
	        	if (!player.hasPermission("Moderator.breakBlock")) {
	        		
	            event.setCancelled(true);
	            
	        	} else {
	        		
	        		event.setCancelled(false);
	        		
	        	}
	        }
	    }
	}

}
