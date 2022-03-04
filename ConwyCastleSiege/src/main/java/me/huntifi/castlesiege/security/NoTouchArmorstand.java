package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class NoTouchArmorstand implements Listener {

	@EventHandler
	public void onTouch(PlayerInteractAtEntityEvent e) {
		
		Player p = e.getPlayer();

		if (e.getRightClicked() instanceof ArmorStand) {
			
			if (p.getGameMode() != GameMode.CREATIVE) {
				
				e.setCancelled(true);
				
			} else if (p.getGameMode() == GameMode.CREATIVE) {
				
				e.setCancelled(false);
				
			}
			
		}

	}
	
	
	@EventHandler
	public void onTouch2(PlayerInteractAtEntityEvent e) {
		
		Player p = e.getPlayer();

		if (e.getRightClicked() instanceof ItemFrame) {
			
			if (p.getGameMode() != GameMode.CREATIVE) {
				
				e.setCancelled(true);
				
			} else if (p.getGameMode() == GameMode.CREATIVE) {
				
				e.setCancelled(false);
				
			}
			
		}

	}

}
