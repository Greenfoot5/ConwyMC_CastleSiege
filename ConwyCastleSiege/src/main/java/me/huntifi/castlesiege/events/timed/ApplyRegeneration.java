package me.huntifi.castlesiege.events.timed;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Applies regeneration to all online players
 */
public class ApplyRegeneration implements Runnable {

	/**
	 * Grants players health back depending on class
	 */
	@Override
	public void run() {

		for (Player online : Bukkit.getOnlinePlayers()) {
			// Check the player isn't dead
			if(!online.isDead()) {
				// Check the player isn't in combat
				if(!InCombat.isPlayerInCombat(online.getUniqueId())) {

					double maxHealth = Objects.requireNonNull(online.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();

					// Make sure the player has been assigned a kit already
					Kit kit = Kit.equippedKits.get(online.getUniqueId());
					if (kit == null) {
						continue;
					}

					online.setHealth(Math.min(online.getHealth() + kit.getRegen(), maxHealth));
				}
			}
		}
	}
}


