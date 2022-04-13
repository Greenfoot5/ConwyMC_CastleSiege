package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Applies regeneration to all online players
 */
public class ApplyRegeneration implements Runnable {

	private final static double REGEN_AMOUNT = 3.0;

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

					// Halberdier gets double the health regen
					if (Kit.equippedKits.get(online.getUniqueId()).name.equalsIgnoreCase("Halberdier")) {
						online.setHealth(Math.min(online.getHealth() + REGEN_AMOUNT * 2, maxHealth));
					} else {
						online.setHealth(Math.min(online.getHealth() + REGEN_AMOUNT, maxHealth));
					}
				}
			}
		}
	}
}


