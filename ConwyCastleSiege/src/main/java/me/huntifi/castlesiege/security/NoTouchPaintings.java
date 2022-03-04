package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class NoTouchPaintings implements Listener {
	
	@EventHandler
	public void onTouch(HangingBreakByEntityEvent e) {
		
		if (e.getEntity() instanceof Painting) {
			
			if (e.getRemover() instanceof Player) {
				
				Player p = (Player) e.getRemover();
				
				if (p.getGameMode() == GameMode.CREATIVE) {
					
					e.setCancelled(false);
					
				} else {
					
					e.setCancelled(true);
					
				}
				
			}
			
			if (e.getRemover() instanceof Arrow) {
				
				e.setCancelled(true);
				
			}
			
		}
		
		
		if (e.getEntity() instanceof ItemFrame) {
			
			if (e.getRemover() instanceof Player) {
				
				Player p = (Player) e.getRemover();
				
				if (p.getGameMode() == GameMode.CREATIVE) {
					
					e.setCancelled(false);
					
				} else {
					
					e.setCancelled(true);
					
				}
				
			}
			
			if (e.getRemover() instanceof Arrow) {
				
				e.setCancelled(true);
				
			}
			
		}
		
		
	}
	
	
	

}
