package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.maps.objects.FlagSidebar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.huntifi.castlesiege.Main.scoreboardLibrary;

/**
 * Displays the scoreboard with current flag ownership to all players
 */
public class Scoreboard implements Runnable {

	private static Sidebar flagSidebar;
	private static FlagSidebar flagComponent;

	/**
	 * Clears the scoreboard for all players
	 */
	public static void clearScoreboard() {
		if (flagSidebar == null)
			return;

		flagSidebar.close();
		flagSidebar = null;
	}

//	public static void createScoreboard() {
//		flagSidebar = Main.scoreboardLibrary.createSidebar();
//		flagSidebar.https://conwymc.alchemix.dev/contact(Component.text("Mode: ", NamedTextColor.DARK_RED, TextDecoration.BOLD)
//				.append(Component.text(MapController.getCurrentMap().gamemode.toString(), NamedTextColor.RED, TextDecoration.BOLD)));
//
//	}

	/**
	 * Clears the scoreboard for one player
	 * @param player The player to clear the scoreboard for
	 */
	public static void clearScoreboard(Player player) {
		flagSidebar.removePlayer(player);
	}

	public static Component getTimeText() {
		String name;

		if (MapController.timer == null)
			return MiniMessage.miniMessage().deserialize("Time: <reset>N/A");
		
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
				return MiniMessage.miniMessage().deserialize("MAP ENDED");
			default:
				return MiniMessage.miniMessage().deserialize("Time: <reset>N/A");
		}

		if (MapController.timer.seconds < 0 || MapController.timer.minutes < 0)
			return MiniMessage.miniMessage().deserialize(name + "<reset>--:--");
		else if (MapController.timer.seconds < 10)
			return MiniMessage.miniMessage().deserialize(name + "<reset>" + MapController.timer.minutes
					+ ":0" + MapController.timer.seconds);
		else
			return MiniMessage.miniMessage().deserialize(name + "<reset>" + MapController.timer.minutes
					+ ":" + MapController.timer.seconds);
	}

	@Override
	public void run() {
		// Recreate the sidebar if it's gone
		if (flagSidebar == null) {
			flagSidebar = scoreboardLibrary.createSidebar();
			flagComponent = new FlagSidebar(flagSidebar);
		}
		flagComponent.tick();

		// TODO - Allow players to select which scoreboard they want
		for (Player online : Bukkit.getOnlinePlayers()) {
			flagSidebar.addPlayer(online);
		}

//        Bukkit.getOnlinePlayers();
// 			if (ActiveData.getData(online.getUniqueId()).getSetting("statsBoard").equals("false")) {
//			} else if (ActiveData.getData(online.getUniqueId()).getSetting("statsBoard").equals("true")) {
//				// Display the stats scoreboard
//				DecimalFormat dec = new DecimalFormat("0.00");
//				DecimalFormat num = new DecimalFormat("0");
//				PlayerData data = MVPStats.getStats(online.getUniqueId());
//
//				replaceScore(objective, 10, ChatColor.WHITE + "Score: " + ChatColor.WHITE + num.format(data.getScore()));
//				replaceScore(objective, 9, ChatColor.GREEN + "Kills: " + ChatColor.WHITE + num.format(data.getKills()));
//				replaceScore(objective, 8, ChatColor.RED + "Deaths: " + ChatColor.WHITE + num.format(data.getDeaths()));
//				replaceScore(objective, 7, ChatColor.YELLOW + "KDR: " + ChatColor.WHITE + dec.format(data.getKills() / data.getDeaths()));
//				replaceScore(objective, 6, ChatColor.DARK_GREEN + "Assists: " + ChatColor.WHITE + num.format(data.getAssists()));
//				replaceScore(objective, 5, ChatColor.GRAY + "Captures: " + ChatColor.WHITE + num.format(data.getCaptures()));
//				replaceScore(objective, 4, ChatColor.LIGHT_PURPLE + "Heals: " + ChatColor.WHITE + num.format(data.getHeals()));
//				replaceScore(objective, 3, ChatColor.DARK_PURPLE + "Supports: " + ChatColor.WHITE + num.format(data.getSupports()));
//				replaceScore(objective, 2, ChatColor.DARK_GRAY + "Kill Streak: " + ChatColor.WHITE + num.format(data.getKillStreak()));
//			}
	}
}
