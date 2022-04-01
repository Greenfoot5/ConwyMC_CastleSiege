package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.maps.MapController;

public class Timer {

	public Integer minutes = 30;
	public Integer seconds = 1;
	public boolean hasGameEnded = false;

	public Timer(int startMinutes, int startSeconds) {
		minutes = startMinutes;
		seconds = startSeconds;
		startTimer();
	}

	public void startTimer() {

		new BukkitRunnable() {

			@Override
			public void run() {
				if (seconds == 0) {
					minutes--;
					seconds = 59;
				} else if ((minutes == 0 && seconds == 1) || minutes <= 0) {
					minutes = 0;
					seconds = 0;
					hasGameEnded = true;
					this.cancel();
				}
				else {
					seconds--;
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 20, 20);
	}
}
