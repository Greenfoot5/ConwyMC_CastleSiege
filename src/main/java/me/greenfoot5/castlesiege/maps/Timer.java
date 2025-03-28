package me.greenfoot5.castlesiege.maps;

import me.greenfoot5.castlesiege.Main;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A timer to measure the end of a map
 */
public class Timer {

	public TimerState state;
	public Integer minutes;
	public Integer seconds;
	// Tracked the length of time the map was played for
	public Integer ongoingTimePassed;

	/**
	 * @param startMinutes The minutes left on the time
	 * @param startSeconds The seconds left on the time
	 * @param startState The state of the timer
	 */
	public Timer(int startMinutes, int startSeconds, TimerState startState) {
		minutes = startMinutes;
		seconds = startSeconds;
		state = startState;
		if (seconds > 0 || minutes > 0)
			startTimer();
		ongoingTimePassed = 0;
	}

	/**
	 * @param startMinutes The minutes left on the time
	 * @param startSeconds The seconds left on the time
	 */
	public void restartTimer(int startMinutes, int startSeconds) {
		minutes = startMinutes;
		seconds = startSeconds;
		startTimer();
	}

	/**
	 * Begins the timer
	 */
	public void startTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (state == TimerState.ENDED) {
					this.cancel();
				} else if (minutes <= 0 && seconds <= 0) {
					this.cancel();
					nextState();
				} else if (seconds == 0) {
					minutes--;
					seconds = 59;
				} else {
					seconds--;
				}

				if (state == TimerState.ONGOING)
					ongoingTimePassed++;
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
				ongoingTimePassed = 0;
				break;
			case ENDED:
				MapController.endMap();
				break;
		}
	}
}
