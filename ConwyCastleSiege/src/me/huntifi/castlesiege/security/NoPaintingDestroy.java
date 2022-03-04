package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class NoPaintingDestroy implements Listener {

	@EventHandler
	public void ondestroy(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof ArmorStand && e.getDamager() instanceof Player) {

			Player p = (Player) e.getDamager();

			if (p.getGameMode() == GameMode.CREATIVE && p.hasPermission("moderator")) {

				e.setCancelled(false);

			} else {

				e.setCancelled(true);

			}
		}

		if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player) {

			Player p = (Player) e.getDamager();

			if (p.getGameMode() == GameMode.CREATIVE && p.hasPermission("moderator")) {

				e.setCancelled(false);

			} else {

				e.setCancelled(true);

			}
		}
	}
	
	
	@EventHandler
	public void OnInteractAtEntity(PlayerInteractAtEntityEvent e) {
		
		Player p = e.getPlayer();
			
		if (e.getRightClicked() instanceof ItemFrame) {
			
			if (p.getGameMode() != GameMode.CREATIVE && !p.hasPermission("moderator")) {
				
	        e.setCancelled(true);
	        
			}
		} else if (e.getRightClicked() instanceof Painting) {
			
			if (p.getGameMode() != GameMode.CREATIVE && !p.hasPermission("moderator")) {
				
		        e.setCancelled(true);
		        
				}
			
		} else if (e.getRightClicked() instanceof ArmorStand) {
			
			if (p.getGameMode() != GameMode.CREATIVE && !p.hasPermission("moderator")) {
				
		        e.setCancelled(true);
		        
				}
			
		}
	}

}
