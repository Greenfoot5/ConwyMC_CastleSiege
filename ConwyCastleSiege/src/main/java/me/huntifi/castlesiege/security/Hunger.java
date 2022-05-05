package me.huntifi.castlesiege.security;

import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Hunger implements Runnable {

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			String kit = Kit.equippedKits.get(p.getUniqueId()).name;
			if (kit == null) {
				continue;
			}

			if (Objects.equals(kit, "Halberdier")) {
				p.setFoodLevel(4);
			} else {
				p.setFoodLevel(p.getFoodLevel() + 1);
			}
		}
		
	}

}
