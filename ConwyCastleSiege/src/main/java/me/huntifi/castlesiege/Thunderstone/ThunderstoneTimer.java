package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.maps.MapController;

public class ThunderstoneTimer {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static Integer Minutes = 30;
	public static Integer Seconds = 1;
	public static boolean hasGameEnded = false;


	public static void ThunderstoneTimerEvent() {

		new BukkitRunnable() {

			@Override
			public void run() {

				if(MapController.currentMapIs("Thunderstone")) {

					if (Seconds == 0) {



						Minutes--;
						Seconds = 59;



					} else if ((Minutes == 0 && Seconds == 1) || Minutes <= 0) {



						Minutes = 0;
						Seconds = 0;
						hasGameEnded = true;
						this.cancel();

					}

					else if (!(Minutes == 0 && Seconds == 0)) {

						Seconds--;

					}

				} else {

					this.cancel();
				}

			}

		}.runTaskTimerAsynchronously(plugin, 20, 20);


	}

}
