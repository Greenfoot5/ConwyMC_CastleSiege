package me.huntifi.castlesiege.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Scoreboard;

public class HelmsdeepScoreboard {

	public static void showHelmsdeepScoreboard(Player p) {
		if(p.getScoreboard().equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard()); //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
		org.bukkit.scoreboard.Scoreboard score = p.getScoreboard(); //Personalized scoreboard
		@SuppressWarnings("deprecation")
		Objective objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.
		String displayName = "Castle Siege";
		objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + displayName);
		Scoreboard.replaceScore(objective, 15, ChatColor.DARK_GRAY + "");
		Scoreboard.replaceScore(objective, 14, ChatColor.GOLD + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + ChatColor.GREEN + MapController.getCurrentMap());
		//Scoreboard.replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + HelmsdeepTimer.Minutes + ":" + HelmsdeepTimer.Seconds);
		//if (HelmsdeepTimer.Seconds < 10) { Scoreboard.replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + "Time: " + ChatColor.RESET + HelmsdeepTimer.Minutes + ":0" + HelmsdeepTimer.Seconds); }
		Scoreboard.replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");

		if (FlagTeam.isFlagTeam("Caves", 2)) {
			Scoreboard.replaceScore(objective, 11, ChatColor.DARK_GREEN + "Caves");
		} else if (FlagTeam.isFlagTeam("Caves", 1)) {
			Scoreboard.replaceScore(objective, 11, ChatColor.DARK_GRAY + "Caves");
		} else if (FlagTeam.isFlagTeam("Caves", 0)) {
			Scoreboard.replaceScore(objective, 11, ChatColor.GRAY + "Caves");
		}

		if (FlagTeam.isFlagTeam("Courtyard", 2)) {
			Scoreboard.replaceScore(objective, 10, ChatColor.DARK_GREEN + "Courtyard");
		} else if (FlagTeam.isFlagTeam("Courtyard", 1)) {
			Scoreboard.replaceScore(objective, 10, ChatColor.DARK_GRAY + "Courtyard");
		} else if (FlagTeam.isFlagTeam("Courtyard", 0)) {
			Scoreboard.replaceScore(objective, 10, ChatColor.GRAY + "Courtyard");
		}

		if (FlagTeam.isFlagTeam("GreatHalls", 2)) {
			Scoreboard.replaceScore(objective, 9, ChatColor.DARK_GREEN + "Great Hall");
		} else if (FlagTeam.isFlagTeam("GreatHalls", 1)) {
			Scoreboard.replaceScore(objective, 9, ChatColor.DARK_GRAY + "Great Hall");
		} else if (FlagTeam.isFlagTeam("GreatHalls", 0)) {
			Scoreboard.replaceScore(objective, 9, ChatColor.GRAY + "Great Hall");
		}

		if (FlagTeam.isFlagTeam("Horn", 2)) {
			Scoreboard.replaceScore(objective, 8, ChatColor.DARK_GREEN + "Horn");
		} else if (FlagTeam.isFlagTeam("Horn", 1)) {
			Scoreboard.replaceScore(objective, 8, ChatColor.DARK_GRAY + "Horn");
		} else if (FlagTeam.isFlagTeam("Horn", 0)) {
			Scoreboard.replaceScore(objective, 8, ChatColor.GRAY + "Horn");
		}

		if (FlagTeam.isFlagTeam("MainGate", 2)) {
			Scoreboard.replaceScore(objective, 6, ChatColor.DARK_GREEN + "Main Gate");
		} else if (FlagTeam.isFlagTeam("MainGate", 1)) {
			Scoreboard.replaceScore(objective, 6, ChatColor.DARK_GRAY + "Main Gate");
		} else if (FlagTeam.isFlagTeam("MainGate", 0)) {
			Scoreboard.replaceScore(objective, 6, ChatColor.GRAY + "Main Gate");
		}

		if (FlagTeam.isFlagTeam("SupplyCamp", 2)) {
			Scoreboard.replaceScore(objective, 5, ChatColor.DARK_GREEN + "Supply Camp");
		} else if (FlagTeam.isFlagTeam("SupplyCamp", 1)) {
			Scoreboard.replaceScore(objective, 5, ChatColor.DARK_GRAY + "Supply Camp");
		} else if (FlagTeam.isFlagTeam("SupplyCamp", 0)) {
			Scoreboard.replaceScore(objective, 5, ChatColor.GRAY + "Supply Camp");
		}

		Scoreboard.replaceScore(objective, 1, ChatColor.DARK_GRAY + "Uruk-hai Camp");



		if(objective.getDisplaySlot() != DisplaySlot.SIDEBAR) objective.setDisplaySlot(DisplaySlot.SIDEBAR); //Vital functionality
		p.setScoreboard(score); //Vital functionality
	}

}
