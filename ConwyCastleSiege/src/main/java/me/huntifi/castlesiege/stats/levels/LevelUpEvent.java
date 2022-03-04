package me.huntifi.castlesiege.stats.levels;

import org.bukkit.Bukkit;

public class LevelUpEvent implements Runnable {

	@Override
	public void run() {

		if (Bukkit.getOnlinePlayers() != null) {

			LevelingEvent.doLeveling();

		}
	}

}
