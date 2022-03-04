package me.huntifi.castlesiege.security;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class armorTakeOff implements Listener {
	
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		
		if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
			
			e.setCancelled(true);
			
		}
		
	}

}
