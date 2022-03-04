package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class NoMoveInventory implements Listener {
	
	
	@EventHandler
	public void onclick(InventoryDragEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		
		if (p.getGameMode() != GameMode.CREATIVE) {
			
			e.setCancelled(true);
			
		}
		
	}
	
	
	@EventHandler
	public void onclick(InventoryClickEvent e) {
		
		Player p = (Player) e.getWhoClicked();
		
		if (p.getGameMode() != GameMode.CREATIVE) {
			
			if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			
			e.setCancelled(true);
			
			}
			
		}
		
	}

}
