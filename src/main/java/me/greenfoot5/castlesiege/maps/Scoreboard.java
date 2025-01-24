package me.greenfoot5.castlesiege.maps;

import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.objects.FlagSidebar;
import me.greenfoot5.castlesiege.maps.objects.StatsSidebar;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation;
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.greenfoot5.castlesiege.Main.scoreboardLibrary;

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

	/**
	 * Clears the scoreboard for one player
	 * @param player The player to clear the scoreboard for
	 */
	public static void clearScoreboard(Player player) {
		if (statsSidebars.containsKey(player.getUniqueId())) {
			statsSidebars.get(player.getUniqueId()).close();
			statsSidebars.remove(player.getUniqueId());
		} else {
			if (flagSidebar != null)
				flagSidebar.removePlayer(player);
		}
	}

	/**
	 * @return The text of the current time &amp; state
	 */
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

		for (UUID uuid : TeamController.getEveryone()) {
			Player player = Bukkit.getPlayer(uuid);
			CSPlayerData data = CSActiveData.getData(uuid);
			if (data != null) {
				if (data.getSetting("scoreboard").startsWith("flag")) {
					flagSidebar.addPlayer(player);
					continue;
				}

				if (!statsSidebars.containsKey(uuid)) {
					statsSidebars.put(uuid, new StatsSidebar(scoreboardLibrary.createSidebar(), uuid));
					statsSidebars.get(uuid).addPlayer(player);
				}
				statsSidebars.get(uuid).tick();
			}

			if (TeamController.isSpectating(uuid)) {
				flagSidebar.addPlayer(player);
			}
		}
	}

	/**
	 * Creates a set of frames for some text
	 * @param text The text to animate
	 * @return The set of frames to animate
	 */
	public static @NotNull SidebarAnimation<Component> createGradientAnimation(@NotNull Component text) {
		float step = 1f / 16f;

		TagResolver.Single textPlaceholder = Placeholder.component("text", text);
		List<Component> frames = new ArrayList<>((int) (2f / step));

		float phase = -1f;
		while (phase < 1) {
			frames.add(Messenger.mm.deserialize("<gradient:#e96443:#904e95:" + phase + "><text>", textPlaceholder));
			phase += step;
		}

		return new CollectionSidebarAnimation<>(frames);
	}
}
