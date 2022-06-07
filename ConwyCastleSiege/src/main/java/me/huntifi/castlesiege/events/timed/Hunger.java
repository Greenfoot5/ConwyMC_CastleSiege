package me.huntifi.castlesiege.events.timed;

import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Manages the player's food level
 */
public class Hunger implements Runnable {

	/**
	 * Increase the player's food level,
	 * or set to 4 for halberdier
	 */
	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Kit kit = Kit.equippedKits.get(p.getUniqueId());
			if (kit == null) {
				continue;
			}

			if (Objects.equals(kit.name, "Halberdier") || Objects.equals(kit.name, "Abyssal")) {
				p.setFoodLevel(4);
			} else {
				p.setFoodLevel(p.getFoodLevel() + 1);
			}
		}
		
	}

}
