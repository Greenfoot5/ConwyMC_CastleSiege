package me.huntifi.castlesiege.combat;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Removes arrows sticking out of entities
 */
public class arrowRemoval implements Listener {

	@EventHandler
	public void onHit(final ProjectileHitEvent e) {

		if (e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();
				arrow.remove();
		}
	}
}
