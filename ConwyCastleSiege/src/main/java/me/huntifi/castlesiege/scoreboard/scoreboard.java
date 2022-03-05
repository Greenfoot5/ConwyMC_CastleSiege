package me.huntifi.castlesiege.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import me.huntifi.castlesiege.maps.MapController;

public class scoreboard implements Runnable {

	public static String getEntryFromScore(Objective o, int score) {
		if(o == null) return null;
		if(!hasScoreTaken(o, score)) return null;
		for (String s : o.getScoreboard().getEntries()) {
			if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
		}
		return null;
	}

	public static boolean hasScoreTaken(Objective o, int score) {
		for (String s : o.getScoreboard().getEntries()) {
			if(o.getScore(s).getScore() == score) return true;
		}
		return false;
	}

	public static void replaceScore(Objective o, int score, String name) {
		if(hasScoreTaken(o, score)) {
			if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
			if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) o.getScoreboard().resetScores(getEntryFromScore(o, score));
		}
		o.getScore(name).setScore(score);
	}

	@Override
	public void run() {

		for (Player online : Bukkit.getOnlinePlayers()) {

			if (MapController.currentMapIs("HelmsDeep")) {

				HelmsdeepScoreboard.showHelmsdeepScoreboard(online);
				
			} else if (MapController.currentMapIs("Thunderstone")) {

				ThunderstoneScoreboard.showThunderstoneScoreboard(online);
			}

		}

	}

}
