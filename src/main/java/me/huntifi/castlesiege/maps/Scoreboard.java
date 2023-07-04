package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Displays the scoreboard with current flag ownership to all players
 */
public class Scoreboard implements Runnable {

	/**
	 * Clears the scoreboard for all players
	 */
	public static void clearScoreboard() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			Objective objective = online.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
			if (objective != null)
				objective.unregister();
		}
	}

	/**
	 * Clears the scoreboard for one player
	 * @param player The player to clear the scoreboard for
	 */
	public static void clearScoreboard(Player player) {
		Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		if (objective != null)
			objective.unregister();
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
			String displayName = ChatColor.DARK_RED + String.valueOf(ChatColor.BOLD) + "Mode: " + ChatColor.RED + ChatColor.BOLD + MapController.getCurrentMap().gamemode;

			// If there isn't an object by the name of the player on the scoreboard,
			// Create a new one
			Objective objective;
			if (score.getObjective(online.getName()) == null) {
				objective = score.registerNewObjective(online.getName(), "dummy", displayName);
			} else {
				objective = score.getObjective(online.getName());
			}

			if (ActiveData.getData(online.getUniqueId()).getSetting("showBattlepoints").equals("true")) {
				assert objective != null;
				objective.setDisplayName(displayName);
				replaceScore(objective, 15, String.format("%s%sMap:%s %s",
						ChatColor.GOLD, ChatColor.BOLD, ChatColor.GREEN, MapController.getCurrentMap().name));
				DecimalFormat dec = new DecimalFormat("0.00");
				PlayerData data = ActiveData.getData(online.getUniqueId());
				// Setup timer display
				replaceScore(objective, 14, ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + getTimeText());
				replaceScore(objective, 13, ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Battlepoints: " + ChatColor.WHITE + dec.format(data.getBattlepoints()));
				replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");

			} else if (ActiveData.getData(online.getUniqueId()).getSetting("showBattlepoints").equals("false")){
				assert objective != null;
				objective.setDisplayName(displayName);
				replaceScore(objective, 15, "");
				replaceScore(objective, 15, String.format("%s%sMap:%s %s",
						ChatColor.GOLD, ChatColor.BOLD, ChatColor.GREEN, MapController.getCurrentMap().name));

				// Setup timer display
				replaceScore(objective, 13, ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + getTimeText());
				replaceScore(objective, 12, ChatColor.DARK_GRAY + "-");
			}

			if (ActiveData.getData(online.getUniqueId()).getSetting("statsBoard").equals("false")) {
				// Display the flag scoreboard
				for (Flag flag : MapController.getCurrentMap().flags) {
					if (flag.isActive()) {
						Team owners = MapController.getCurrentMap().getTeam(flag.getCurrentOwners());
						Scoreboard.replaceScore(objective, flag.scoreboard,
								(owners == null ? ChatColor.GRAY : owners.primaryChatColor) + flag.name);
					}
				}

			} else if (ActiveData.getData(online.getUniqueId()).getSetting("statsBoard").equals("true")) {
				// Display the stats scoreboard
				DecimalFormat dec = new DecimalFormat("0.00");
				DecimalFormat num = new DecimalFormat("0");
				PlayerData data = MVPStats.getStats(online.getUniqueId());

				replaceScore(objective, 10, ChatColor.WHITE + "Score: " + ChatColor.WHITE + num.format(data.getScore()));
				replaceScore(objective, 9, ChatColor.GREEN + "Kills: " + ChatColor.WHITE + num.format(data.getKills()));
				replaceScore(objective, 8, ChatColor.RED + "Deaths: " + ChatColor.WHITE + num.format(data.getDeaths()));
				replaceScore(objective, 7, ChatColor.YELLOW + "KDR: " + ChatColor.WHITE + dec.format(data.getKills() / data.getDeaths()));
				replaceScore(objective, 6, ChatColor.DARK_GREEN + "Assists: " + ChatColor.WHITE + num.format(data.getAssists()));
				replaceScore(objective, 5, ChatColor.GRAY + "Captures: " + ChatColor.WHITE + num.format(data.getCaptures()));
				replaceScore(objective, 4, ChatColor.LIGHT_PURPLE + "Heals: " + ChatColor.WHITE + num.format(data.getHeals()));
				replaceScore(objective, 3, ChatColor.DARK_PURPLE + "Supports: " + ChatColor.WHITE + num.format(data.getSupports()));
				replaceScore(objective, 2, ChatColor.DARK_GRAY + "Killstreak: " + ChatColor.WHITE + num.format(data.getKillStreak()));
			}

			// Actually displays the scoreboard
			if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			}

			online.setScoreboard(score);
		}
	}
}
