package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.Helmsdeep.flags.HelmsdeepReset;
import me.huntifi.castlesiege.Thunderstone.Flags.ThunderstoneReset;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneEndMap;
import me.huntifi.castlesiege.Thunderstone.ThunderstoneTimer;
import me.huntifi.castlesiege.joinevents.stats.MainStats;
import me.huntifi.castlesiege.stats.levels.LevelingEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Manages what map the game is currently on
 */
public class MapController {

	public static MapsList currentMap = MapsList.HelmsDeep;
	public static Map[] maps;
	public static int mapIndex = 0;

	/**
	 * Sets the current map by string
	 * @param mapName the name of the map to set the current map to
	 */
	public static void setMap(String mapName) {

		switch (mapName.toLowerCase()) {
			case "helmsdeep":
				currentMap = MapsList.HelmsDeep;
				break;
			case "thunderstone":
				currentMap = MapsList.Thunderstone;
				break;
		}
	}

	/**
	 * Increments the map by one
	 */
	public static void nextMap() {
		if (finalMap()) {
			ThunderstoneReset.onReset();
			Bukkit.getServer().spigot().restart();
		}
		else {
			currentMap = MapsList.values()[currentMap.ordinal() + 1];
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p != null) {

					LevelingEvent.doLeveling();
					MainStats.updateStats(p.getUniqueId(), p);

				}
			}

			HelmsdeepReset.onReset(); //reset the map

			ThunderstoneTimer.Minutes = 30;
			ThunderstoneTimer.Seconds = 3;
			ThunderstoneTimer.ThunderstoneTimerEvent();

			ThunderstoneEndMap.TS_hasEnded = false;
		}
	}

	/**
	 * Checks if the current map is the one specified
	 * @param mapName The name of the map
	 * @return If mapName is the same as the current map
	 */
	public static Boolean currentMapIs(String mapName) {
		return currentMap.name().equalsIgnoreCase(mapName);
	}

	/**
	 * Checks if the game is on the last map
	 * @return if the game is on the final map
	 */
	public static boolean finalMap() {
		return currentMap.ordinal() == MapsList.values().length - 1;
	}

	/**
	 * Gets the name of the current map
	 * @return A string containing the current map's name
	 */
	public static Map getCurrentMap() {
		return maps[mapIndex];
	}

	/**
	 * Checks if the current map has ended
	 * @return If the map has currently ended
	 */
	public static Boolean isMapEnded() {
		String[] flags;

		// Get a list of current flags
		switch (currentMap) {
			case HelmsDeep:
				flags = new String[]{"MainGate", "Courtyard", "Caves", "Horn", "GreatHalls", "SupplyCamp"};
				break;
			case Thunderstone:
				flags = new String[]{"skyviewtower", "stairhall", "westtower", "easttower",
						"shiftedtower", "twinbridge", "lonelytower"};
				break;
			default:
				return false;
		}

		// return false if all flags don't belong to the same team
		int startingTeam = FlagTeam.getFlagTeam(flags[0]);
		for (int i = 1; i < flags.length; i++) {
			if (!FlagTeam.isFlagTeam(flags[i], startingTeam))
				return false;
		}
		return true;
	}
}
