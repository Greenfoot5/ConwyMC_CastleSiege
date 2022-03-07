package me.huntifi.castlesiege.kits;

import org.bukkit.entity.Player;

public class ClearSlots {
	
	public static void clearAllSlots(Player p) {
		
		p.getInventory().setItem(1, null);
		p.getInventory().setItem(2, null);
		p.getInventory().setItem(3, null);
		p.getInventory().setItem(4, null);
		p.getInventory().setItem(5, null);
		p.getInventory().setItem(6, null);
		p.getInventory().setItem(7, null);
		p.getInventory().setItem(8, null);
		p.getInventory().setItem(0, null);
		p.getInventory().setItemInOffHand(null);
		WoolHat.setHead(p);

	}

}
