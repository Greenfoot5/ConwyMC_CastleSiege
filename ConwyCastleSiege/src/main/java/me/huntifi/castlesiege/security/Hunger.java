package me.huntifi.castlesiege.security;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Hunger implements Runnable {

	@Override
	public void run() {
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			
			online.setFoodLevel(online.getFoodLevel() + 1);
			
		}
		
	}

}
