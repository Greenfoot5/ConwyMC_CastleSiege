package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.flags.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Objects;

public class Scoreboard implements Runnable {

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
			// Per player scoreboard, nor normally needed, but we're displaying their display name
			if (online.getScoreboard().equals(Objects.requireNonNull(Bukkit.getServer().getScoreboardManager()).getMainScoreboard())) {
				online.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}

			org.bukkit.scoreboard.Scoreboard score = online.getScoreboard();

			//Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.
			String displayName = ChatColor.RED + "" + ChatColor.BOLD + "Castle Siege";
			Objective objective = null;
			if (score.getObjective(online.getName()) == null) {
				objective = score.registerNewObjective(online.getName(), "dummy", displayName);
			} else {
				objective = score.getObjective(online.getName());
			}

			objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + displayName);
			replaceScore(objective, 15, ChatColor.DARK_GRAY + "");
			replaceScore(objective, 14, ChatColor.GOLD + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + ChatColor.GREEN + MapController.getCurrentMap().name);

			// Setup timer display
			replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + MapController.timer.minutes + ":" + MapController.timer.seconds);
			if (MapController.timer.seconds < 10) { replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + MapController.timer.minutes + ":0" + MapController.timer.seconds); }
			replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");

			for (int i = 0; i < MapController.getCurrentMap().flags.length; i++) {
				Flag flag = MapController.getCurrentMap().flags[i];
				Team owners = MapController.getCurrentMap().getTeam(flag.currentOwners);
				Scoreboard.replaceScore(objective, flag.scoreboard, owners == null ? String.valueOf(ChatColor.GRAY) : owners.primaryChatColor+ flag.name);
			}

			// Actually displays the scoreboard
			if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			online.setScoreboard(score);
		}
	}
}
