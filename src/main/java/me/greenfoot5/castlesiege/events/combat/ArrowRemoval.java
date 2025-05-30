package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Removes arrows sticking out of entities
 */
public class ArrowRemoval implements Listener {

	/**
	 * Removes the arrow if it hits an entity
	 * @param e Called when a projectile hits something
	 */
	@EventHandler
	public void onHit(final ProjectileHitEvent e) {
		if (!MapController.isOngoing()) {
			return;
		}

		if (e.getEntity() instanceof Arrow arrow) {
            arrow.remove();
		}
	}
}
