package me.huntifi.castlesiege.security;

import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class Hunger implements Runnable {

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, "Halberdier")) {
				p.setFoodLevel(4);
			} else {
				p.setFoodLevel(p.getFoodLevel() + 1);
			}
		}
		
	}

}
