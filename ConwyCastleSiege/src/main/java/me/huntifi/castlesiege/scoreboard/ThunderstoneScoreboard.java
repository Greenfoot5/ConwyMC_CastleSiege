package me.huntifi.castlesiege.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.huntifi.castlesiege.Thunderstone.ThunderstoneTimer;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagTeam;
import me.huntifi.castlesiege.maps.MapController;

public class ThunderstoneScoreboard {

	
	public static void showThunderstoneScoreboard(Player p) {
		if(p.getScoreboard().equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard()); //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
		Scoreboard score = p.getScoreboard(); //Personalized scoreboard
		@SuppressWarnings("deprecation")
		Objective objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.
		String displayName = "Castle Siege";
		objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + displayName);
		scoreboard.replaceScore(objective, 15, ChatColor.DARK_GRAY + "");
		scoreboard.replaceScore(objective, 14, ChatColor.GOLD + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + ChatColor.GREEN + MapController.getCurrentMap());
		scoreboard.replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + ThunderstoneTimer.Minutes + ":" + ThunderstoneTimer.Seconds);
		if (ThunderstoneTimer.Seconds < 10) { scoreboard.replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + ThunderstoneTimer.Minutes + ":0" + ThunderstoneTimer.Seconds); }
		scoreboard.replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");

		if (TS_FlagTeam.isFlagTeam("skyviewtower", 2)) {
			scoreboard.replaceScore(objective, 11, ChatColor.GOLD + "Skyview Tower");
		} else if (TS_FlagTeam.isFlagTeam("skyviewtower", 1)) {
			scoreboard.replaceScore(objective, 11, ChatColor.DARK_AQUA + "Skyview Tower");
		} else if (TS_FlagTeam.isFlagTeam("skyviewtower", 0)) {
			scoreboard.replaceScore(objective, 11, ChatColor.GRAY + "Skyview Tower");
		}

		if (TS_FlagTeam.isFlagTeam("lonelytower", 2)) {
			scoreboard.replaceScore(objective, 10, ChatColor.GOLD + "Lonely Tower");
		} else if (TS_FlagTeam.isFlagTeam("lonelytower", 1)) {
			scoreboard.replaceScore(objective, 10, ChatColor.DARK_AQUA + "Lonely Tower");
		} else if (TS_FlagTeam.isFlagTeam("lonelytower", 0)) {
			scoreboard.replaceScore(objective, 10, ChatColor.GRAY + "Lonely Tower");
		}

		if (TS_FlagTeam.isFlagTeam("stairhall", 2)) {
			scoreboard.replaceScore(objective, 9, ChatColor.GOLD + "Stair Hall");
		} else if (TS_FlagTeam.isFlagTeam("stairhall", 1)) {
			scoreboard.replaceScore(objective, 9, ChatColor.DARK_AQUA + "Stair Hall");
		} else if (TS_FlagTeam.isFlagTeam("stairhall", 0)) {
			scoreboard.replaceScore(objective, 9, ChatColor.GRAY + "Stair Hall");
		}

		if (TS_FlagTeam.isFlagTeam("twinbridge", 2)) {
			scoreboard.replaceScore(objective, 8, ChatColor.GOLD + "Twinbridge");
		} else if (TS_FlagTeam.isFlagTeam("twinbridge", 1)) {
			scoreboard.replaceScore(objective, 8, ChatColor.DARK_AQUA + "Twinbridge");
		} else if (TS_FlagTeam.isFlagTeam("twinbridge", 0)) {
			scoreboard.replaceScore(objective, 8, ChatColor.GRAY + "Twinbridge");
		}

		if (TS_FlagTeam.isFlagTeam("shiftedtower", 2)) {
			scoreboard.replaceScore(objective, 7, ChatColor.GOLD + "Shifted Tower");
		} else if (TS_FlagTeam.isFlagTeam("shiftedtower", 1)) {
			scoreboard.replaceScore(objective, 7, ChatColor.DARK_AQUA + "Shifted Tower");
		} else if (TS_FlagTeam.isFlagTeam("shiftedtower", 0)) {
			scoreboard.replaceScore(objective, 7, ChatColor.GRAY + "Shifted Tower");
		}

		if (TS_FlagTeam.isFlagTeam("easttower", 2)) {
			scoreboard.replaceScore(objective, 6, ChatColor.GOLD + "East Tower");
		} else if (TS_FlagTeam.isFlagTeam("easttower", 1)) {
			scoreboard.replaceScore(objective, 6, ChatColor.DARK_AQUA + "East Tower");
		} else if (TS_FlagTeam.isFlagTeam("easttower", 0)) {
			scoreboard.replaceScore(objective, 6, ChatColor.GRAY + "East Tower");
		}

		if (TS_FlagTeam.isFlagTeam("westtower", 2)) {
			scoreboard.replaceScore(objective, 5, ChatColor.GOLD + "West Tower");
		} else if (TS_FlagTeam.isFlagTeam("westtower", 1)) {
			scoreboard.replaceScore(objective, 5, ChatColor.DARK_AQUA + "West Tower");
		} else if (TS_FlagTeam.isFlagTeam("westtower", 0)) {
			scoreboard.replaceScore(objective, 5, ChatColor.GRAY + "West Tower");
		}

		scoreboard.replaceScore(objective, 1, ChatColor.DARK_AQUA + "Cloudcrawler Camp");



		if(objective.getDisplaySlot() != DisplaySlot.SIDEBAR) objective.setDisplaySlot(DisplaySlot.SIDEBAR); //Vital functionality
		p.setScoreboard(score); //Vital functionality
	}
}
