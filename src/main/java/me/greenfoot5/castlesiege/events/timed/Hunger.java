package me.greenfoot5.castlesiege.events.timed;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
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
			if (!TeamController.isPlaying(p))
				continue;

			Kit kit = Kit.equippedKits.get(p.getUniqueId());
			if (kit == null) {
				continue;
			}

			// Slow kits
			if (Objects.equals(kit.name, "Halberdier") || Objects.equals(kit.name, "Abyssal") ||
					Objects.equals(kit.name, "Arbalester") || Objects.equals(kit.name, "Moria Cave Troll")) {
				p.setFoodLevel(4);
			} else {
				p.setFoodLevel(p.getFoodLevel() + 1);
			}
		}
	}
}
