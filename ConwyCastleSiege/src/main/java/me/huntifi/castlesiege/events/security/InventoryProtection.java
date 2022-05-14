package me.huntifi.castlesiege.events.security;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Makes sure a player can't change their inventory
 */
public class InventoryProtection implements Listener {

	/**
	 * Cancels event when player attempts to move an inventory item
	 * @param e The event called when a player clicks an inventory item
	 */
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (p.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player attempts to drop an item
	 * @param e The event called when a player drops an item
	 */
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
}
