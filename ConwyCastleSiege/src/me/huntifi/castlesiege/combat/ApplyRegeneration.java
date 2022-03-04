package me.huntifi.castlesiege.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class ApplyRegeneration implements Runnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {

		for (Player online : Bukkit.getOnlinePlayers()) {

			if(!LobbyPlayer.containsPlayer(online)) {

				if(!CustomRegeneration.inCombat.contains(online)) {

					if (!online.isDead()) {

						if (!StatsChanging.getKit(online.getUniqueId()).equalsIgnoreCase("Halberdier")) {

							Double health = online.getHealth();

							if ((health + 3.0) > online.getMaxHealth()) {

								online.setHealth(online.getMaxHealth());

							} else 

								if (health + 3.0 < online.getMaxHealth()) {

									online.setHealth(health + 3.0);

								}

						} else if (StatsChanging.getKit(online.getUniqueId()).equalsIgnoreCase("Halberdier")) {
							
							Double health = online.getHealth();

							if ((health + 6.0) > online.getMaxHealth()) {

								online.setHealth(online.getMaxHealth());

							} else 

								if (health + 6.0 < online.getMaxHealth()) {

									online.setHealth(health + 6.0);

								}
							
						}
					}

				}
			}
		}

	}


}


