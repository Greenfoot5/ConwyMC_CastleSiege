package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.maps.objects.FlagSidebar;
import me.huntifi.castlesiege.maps.objects.StatsSidebar;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.huntifi.castlesiege.Main.scoreboardLibrary;

/**
 * Displays the scoreboard with current flag ownership to all players
 */
public class Scoreboard implements Runnable {

	private static final HashMap<UUID, StatsSidebar> statsSidebars = new HashMap<>();
	private static FlagSidebar flagSidebar;

	/**
	 * Clears the scoreboard for all players
	 */
	public static void clearScoreboard() {
		for (UUID uuid : statsSidebars.keySet()) {
			statsSidebars.get(uuid).close();
			statsSidebars.remove(uuid);
		}

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
		if (statsSidebars.containsKey(player.getUniqueId())) {
			statsSidebars.get(player.getUniqueId()).close();
			statsSidebars.remove(player.getUniqueId());
		} else {
			flagSidebar.removePlayer(player);
		}
	}

	public static Component getTimeText() {
		String name;

		if (MapController.timer == null)
			return Messenger.mm.deserialize("Time: <reset>N/A");
		
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
				return Messenger.mm.deserialize("MAP ENDED");
			default:
				return Messenger.mm.deserialize("Time: <reset>N/A");
		}

		if (MapController.timer.seconds < 0 || MapController.timer.minutes < 0)
			return Messenger.mm.deserialize(name + "<reset>--:--");
		else if (MapController.timer.seconds < 10)
			return Messenger.mm.deserialize(name + "<reset>" + MapController.timer.minutes
					+ ":0" + MapController.timer.seconds);
		else
			return Messenger.mm.deserialize(name + "<reset>" + MapController.timer.minutes
					+ ":" + MapController.timer.seconds);
	}

	@Override
	public void run() {
		// Recreate the sidebar if it's gone
		if (flagSidebar == null) {
			flagSidebar = new FlagSidebar(scoreboardLibrary.createSidebar());
		}
		flagSidebar.tick();

		// TODO - Allow players to select which scoreboard they want
		for (Player online : Bukkit.getOnlinePlayers()) {
			UUID uuid = online.getUniqueId();
			CSPlayerData data = CSActiveData.getData(uuid);
			if (data.getSetting("scoreboard").startsWith("flag")) {
				flagSidebar.addPlayer(online);
				return;
			}

            if (!statsSidebars.containsKey(uuid)) {
                statsSidebars.put(uuid, new StatsSidebar(scoreboardLibrary.createSidebar(), uuid));
				statsSidebars.get(uuid).addPlayer(online);
            }
            statsSidebars.get(uuid).tick();
		}
	}
}
