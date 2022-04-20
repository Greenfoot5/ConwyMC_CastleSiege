package me.huntifi.castlesiege.events.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

/**
 * Makes sure a player can't hurt anything in the lobby
 */
public class NoLobbyCombat implements Listener {

	/**
	 * Player has attempted to attack an entity
	 * Cancels event if the player is in the lobby
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onHurt(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			UUID uuid = e.getEntity().getUniqueId();

			if (!InCombat.hasPlayerSpawned(uuid)) {
				e.setCancelled(true);
			}
		}
	}
}
