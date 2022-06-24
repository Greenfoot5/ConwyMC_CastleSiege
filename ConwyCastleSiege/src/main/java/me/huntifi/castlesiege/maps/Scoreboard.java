package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Objects;

/**
 * Displays the scoreboard with current flag ownership to all players
 */
public class Scoreboard implements Runnable {

	/**
	 * Clears the scoreboard
	 */
	public static void clearScoreboard() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			for (Objective objective : online.getScoreboard().getObjectives()) {
				objective.unregister();
			}
		}
	}

	public static String getEntryFromScore(Objective o, int score) {
		if(o == null) return null;
		if(!hasScoreTaken(o, score)) return null;
		for (String s : Objects.requireNonNull(o.getScoreboard()).getEntries()) {
			if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
		}
		return null;
	}

	public static boolean hasScoreTaken(Objective o, int score) {
		for (String s : Objects.requireNonNull(o.getScoreboard()).getEntries()) {
			if(o.getScore(s).getScore() == score) return true;
		}
		return false;
	}

	public static void replaceScore(Objective o, int score, String name) {
		if(hasScoreTaken(o, score)) {
			if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
			if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) Objects.requireNonNull(o.getScoreboard()).resetScores(getEntryFromScore(o, score));
		}
		o.getScore(name).setScore(score);
	}

	public static String getTimeText() {
		String name;

		if (MapController.timer == null)
			return "Time: " + ChatColor.RESET + "N/A";
		
		switch (MapController.timer.state) {
			case PREGAME:
				name = "Pre-Game: ";
				break;
			case EXPLORATION:
				name = "Explore: ";
				break;
			case LOBBY_LOCKED:
				name = "Lobby: ";
				break;
			case ONGOING:
				name = "Play: ";
				break;
			case ENDED:
				return "MAP ENDED";
			default:
				return "Time: " + ChatColor.RESET + "N/A";
		}

		if (MapController.timer.seconds < 0 || MapController.timer.minutes < 0)
			return name + ChatColor.RESET + "--:--";
		else if (MapController.timer.seconds < 10)
			return name + ChatColor.RESET + MapController.timer.minutes + ":0" + MapController.timer.seconds;
		else
			return name + ChatColor.RESET + MapController.timer.minutes + ":" + MapController.timer.seconds;
	}

	@Override
	public void run() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			// If the player is seeing the default scoreboard, we want to give them a new one
			if (online.getScoreboard() == Objects.requireNonNull(Bukkit.getServer().getScoreboardManager()).getMainScoreboard()) {
				online.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}

			// Gets the scoreboard displayed to the player
			org.bukkit.scoreboard.Scoreboard score = online.getScoreboard();

			// Title/display name of the scoreboard
			String displayName = ChatColor.RED + "" + ChatColor.BOLD + "Castle Siege";

			// If there isn't an object by the name of the player on the scoreboard,
			// Create a new one
			Objective objective;
			if (score.getObjective(online.getName()) == null) {
				objective = score.registerNewObjective(online.getName(), "dummy", displayName);
			} else {
				objective = score.getObjective(online.getName());
			}

			assert objective != null;
			objective.setDisplayName(displayName);
			replaceScore(objective, 15, ChatColor.DARK_GRAY + "");
			replaceScore(objective, 14, ChatColor.GOLD + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + ChatColor.GREEN + MapController.getCurrentMap().name);

			// Setup timer display
			replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + getTimeText());
			if (MapController.timer.seconds < 10) { replaceScore(objective, 13, ChatColor.GOLD + "" + ChatColor.BOLD + getTimeText()); }
			replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");

			for (int i = 0; i < MapController.getCurrentMap().flags.length; i++) {
				Flag flag = MapController.getCurrentMap().flags[i];
				Team owners = MapController.getCurrentMap().getTeam(flag.getCurrentOwners());
				Scoreboard.replaceScore(objective, flag.scoreboard, owners == null ? ChatColor.GRAY + flag.name : owners.primaryChatColor + flag.name);
			}

			// Actually displays the scoreboard
			if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			online.setScoreboard(score);
		}
	}
}
