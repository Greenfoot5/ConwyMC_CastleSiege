package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.commands.donator.duels.DuelCmd;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Makes sure a player can't hurt anything or get hurt in the lobby
 */
public class LobbyCombat implements Listener {

	/**
	 * Player takes damage
	 * Cancels event if the player is in the lobby
	 * @param e The event called when a player takes damage
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTakeDamage(EntityDamageEvent e) {
		if (InCombat.isPlayerInLobby(e.getEntity().getUniqueId()))
			e.setCancelled(true);
	}

	/**
	 * Player has attempted to attack an entity
	 * Cancels event if the player is in the lobby
	 * @param e The event called when a player hits another player
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onHurt(EntityDamageByEntityEvent e) {
		if (InCombat.isPlayerInLobby(e.getDamager().getUniqueId()))
			e.setCancelled(true);
	}

	/**
	 * Player has attempted to shoot an arrow
	 * Cancels event if the player is in the lobby
	 * @param e The event called when a player shoots an arrow
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onShoot(EntityShootBowEvent e) {
		if (InCombat.isPlayerInLobby(e.getEntity().getUniqueId()))
			e.setCancelled(true);
	}
}
