package me.huntifi.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.flags.Flag;
import me.huntifi.castlesiege.joinevents.stats.MainStats;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.kits.kits.Swordsman;
import me.huntifi.castlesiege.stats.levels.LevelingEvent;
import me.huntifi.castlesiege.tags.NametagsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

/**
 * Manages what map the game is currently on
 */
public class MapController {

	public static MapsList currentMap = MapsList.HelmsDeep;
	public static Map[] maps;
	public static int mapIndex = 0;
	public static Timer timer;

	public static void startLoop() {
		currentMap = MapsList.values()[0];
		loadMap();
	}

	/**
	 * Sets the current map by string
	 * @param mapName the name of the map to set the current map to
	 */
	public static void setMap(String mapName) {
		String oldMap = currentMap.name();
		switch (mapName.toLowerCase()) {
			case "helmsdeep":
				currentMap = MapsList.HelmsDeep;
				break;
			case "thunderstone":
				currentMap = MapsList.Thunderstone;
				break;
		}
		getLogger().info("Loading map - " + mapName);

		loadMap();
		if (!oldMap.equals(mapName))
			unloadMap(oldMap);
	}

	/**
	 * Increments the map by one
	 */
	public static void nextMap() {
		String oldMap = currentMap.name();
		if (finalMap()) {
			getLogger().info("Completed map cycle! Restarting server...");
			getServer().spigot().restart();
		}
		else {
			currentMap = MapsList.values()[currentMap.ordinal() + 1];
			mapIndex++;
			getLogger().info("Loading next map: " + currentMap.name());
			for (Player p : Bukkit.getOnlinePlayers()) {

				if (p != null) {
					LevelingEvent.doLeveling();
					MainStats.updateStats(p.getUniqueId(), p);
				}
			}
			loadMap();
			if (!oldMap.equals(currentMap.name())) {
				unloadMap(oldMap);
			}
		}
	}

	/**
	 * Loads the current map
	 */
	public static void loadMap() {
		// Load the world
		WorldCreator worldSettings = new WorldCreator(currentMap.name());
		worldSettings.generateStructures(false);
		worldSettings.createWorld();

		// Register the flag regions
		for (Flag flag : maps[mapIndex].flags) {
			WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld(currentMap.name()))).addRegion(flag.region);
		}

		// Register the woolmap clicks
		for (Team team : maps[mapIndex].teams) {
			getServer().getPluginManager().registerEvents(team.lobby.woolmap, Main.plugin);
		}

		// Move all players to the new map and team
		Kit.equippedKits = new HashMap<>();
		for (Player player : Main.plugin.getServer().getOnlinePlayers()) {
			joinATeam(player.getUniqueId());
		}

		// Start the timer!
		timer = new Timer(getCurrentMap().duration.getFirst(), getCurrentMap().duration.getSecond());
	}

	/**
	 * Does any unloading needed for the current map
	 */
	public static void unloadMap(String worldName) {
		for (Map map:maps) {
			if (Objects.equals(map.worldName, worldName)) {
				for (Team team:map.teams) {
					team.clear();
				}
			}
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

	/**
	 * Adds a player to a team on the current map
	 * @param uuid the player to add to a team
	 */
	public static void joinATeam(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		Team team = maps[mapIndex].smallestTeam();

		team.addPlayer(uuid);
		assert player != null;
		if (team.lobby.spawnPoint.getWorld() == null) {
			team.lobby.spawnPoint.setWorld(Bukkit.getWorld(getCurrentMap().worldName));
		}
		player.teleport(team.lobby.spawnPoint);
		player.getInventory().clear();
		new Swordsman().addPlayer(uuid);

		player.sendMessage("You joined" + team.primaryChatColor + " " + team.name);
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		ItemStack wool = new ItemStack(team.primaryWool);
		ItemMeta woolMeta = wool.getItemMeta();
		assert woolMeta != null;
		woolMeta.setDisplayName(ChatColor.GREEN + "WoolHat");
		player.getInventory().setHelmet(wool);

		NametagsEvent.GiveNametag(player);
	}

	/**
	 * Removes a player from the team when they disconnect
	 * @param uuid the uuid to remove
	 */
	public static void leaveTeam(UUID uuid) {
		Map map = MapController.getCurrentMap();
		Team team = map.getTeam(uuid);
		if (team != null) {
			team.removePlayer(uuid);
		}
	}
}
