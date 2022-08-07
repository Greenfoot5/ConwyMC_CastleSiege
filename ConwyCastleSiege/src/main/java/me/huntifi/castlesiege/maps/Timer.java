package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A timer to measure the end of a map
 */
public class Timer {

	public TimerState state;
	public Integer minutes;
	public Integer seconds;

	public Timer(int startMinutes, int startSeconds, TimerState startState) {
		minutes = startMinutes;
		seconds = startSeconds;
		state = startState;
		if (seconds > 0 || minutes > 0)
			startTimer();
	}

	public void restartTimer(int startMinutes, int startSeconds) {
		minutes = startMinutes;
		seconds = startSeconds;
		startTimer();
	}
	
	public void startTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (state == TimerState.ENDED) {
					this.cancel();
				} else if (minutes == 0 && seconds == 0 || minutes < 0) {
					this.cancel();
					nextState();
				} else if (seconds == 0) {
					minutes--;
					seconds = 59;
				} else {
					seconds--;
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 20, 20);
	}

	private void nextState() {
		state = TimerState.values()[1 + state.ordinal()];
		switch (state) {
			case EXPLORATION:
				MapController.beginExploration();
				break;
			case LOBBY_LOCKED:
				MapController.beginLobbyLock();
				break;
			case ONGOING:
				MapController.beginMap();
				break;
			case ENDED:
				MapController.endMap();
				break;
		}
	}
}
