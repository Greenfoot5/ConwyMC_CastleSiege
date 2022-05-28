package me.huntifi.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.SpectateCommand;
import me.huntifi.castlesiege.commands.info.MVPCommand;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 * Manages what map the game is currently on
 */
public class MapController implements CommandExecutor {

	public static List<Map> maps = new ArrayList<>();
	public static int mapIndex = 0;
	public static Timer timer;

	public static int mapCount = 3;

	// Delays
	public static int preGameDelay = 0;
	public static int preMapDelay = 0;
	public static int explorationTime = 0;

	public static boolean isMatch = false;
	public static boolean keepTeams = false;
	private static final ArrayList<ArrayList<UUID>> teams = new ArrayList<>();

	/**
	 * Begins the map loop
	 */
	public static void startLoop() {
		if (!isMatch) {
			Collections.shuffle(maps);
			if ( mapCount > 0 && mapCount < maps.size())
				maps = maps.subList(0, mapCount);
		}
		loadMap();
	}

	public static Map getUnplayedMap(String mapName) {
		for (int i = mapIndex; i < maps.size(); i++) {
			if (Objects.equals(maps.get(i).name, mapName)) {
				return maps.get(i);
			}
		}
		return null;
	}

	public static Map getMap(String mapName) {
		for (Map map : maps) {
			if (Objects.equals(map.name, mapName))
				return map;
		}
		return null;
	}

	/**
	 * Sets the maps, and enables match mode
	 * Returns false if a map cannot be found
	 * @param mapNames A list of map names to play
	 */
	public static void setMaps(List<String> mapNames) {
		List<Map> newMaps = new ArrayList<>();
		for (String mapName : mapNames) {
			Map map = getMap(mapName);
			if (map != null) {
				newMaps.add(map);
			} else {
				getLogger().severe("Could not load match mode. Could not find map: `" + mapName + "`");
			}
		}
		maps = newMaps;
	}

	/**
	 * Sets the current map by string
	 * @param mapName the name of the map to set the current map to
	 */
	public static void setMap(String mapName) {
		String oldMap = maps.get(mapIndex).name;
		for (int i = 0; i < maps.size(); i++) {
			if (Objects.equals(maps.get(i).name, mapName)) {
				getLogger().info("Loading map - " + mapName);

				mapIndex = i;

				loadMap();
				if (!oldMap.equals(mapName))
					unloadMap(oldMap);
				return;
			}
		}
	}

	/**
	 * Handles ending the map
	 * (called by the timer or if all flags are captured)
	 */
	public static void endMap() {
		timer.hasGameEnded = true;

		// Calculate the winner based on the game mode
		String winners = null;
		switch(getCurrentMap().gamemode) {
			case Control:
				getLogger().severe("Control game mode has not been implemented yet! Defaulting to teams[0] as winners");
				winners = getCurrentMap().teams[0].name;
				break;
			case Charge:
				// Check if the defenders have won
				for (Flag flag : getCurrentMap().flags) {
					if (Objects.equals(flag.getCurrentOwners(), getCurrentMap().teams[0].name)) {
						winners = getCurrentMap().teams[0].name;
					}
				}
				if (winners != null)
					break;
			case Assault:
			case Domination:
			default:
				// Get a count of who owns which flag
				java.util.Map<String, Integer> flagCounts = new HashMap<>();
				for (Flag flag : getCurrentMap().flags) {
					flagCounts.merge(flag.getCurrentOwners(), 1, Integer::sum);
				}
				// Get the team with the largest
				winners = (String) flagCounts.keySet().toArray()[0];
				for (String teamName : flagCounts.keySet()) {
					if (flagCounts.get(teamName) > flagCounts.get(winners)) {
						winners = teamName;
					}
				}
				break;
		}
		// Moves all players to the lobby
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					Team team = getCurrentMap().getTeam(player.getUniqueId());
					if (team != null) {
						player.teleport(team.lobby.spawnPoint);
					}
				}
				InCombat.clearCombat();
			}
		}.runTask(Main.plugin);

		// Clear all capture zones
		for (Flag flag : getCurrentMap().flags) {
			flag.clear();
		}

		// Broadcast the winners
		for (Team team : getCurrentMap().teams) {
			Bukkit.broadcastMessage("");
			if (team.name.equals(winners)) {
				Bukkit.broadcastMessage(team.primaryChatColor + "~~~~~~~~" + team.name + " has won!~~~~~~~~");
		    } else {
				Bukkit.broadcastMessage(team.primaryChatColor + "~~~~~~~~" + team.name + " has lost!~~~~~~~~");
			}

			// Broadcast MVP
			for (String message : MVPCommand.getMVPMessage(team)) {
				Bukkit.broadcastMessage(message);
			}
		}
		MVPStats.reset();

		// Begins the next map
		new BukkitRunnable() {
			@Override
			public void run() {
				nextMap();
			}
		}.runTaskLater(Main.plugin, 150);

	}

	/**
	 * Increments the map by one
	 */
	private static void nextMap() {
		String oldMap = maps.get(mapIndex).name;
		if (finalMap()) {
			getLogger().info("Completed map cycle! Restarting server...");
			getServer().spigot().restart();
		}
		else {
			mapIndex++;
			getLogger().info("Loading next map: " + maps.get(mapIndex).name);
			loadMap();
			if (!oldMap.equals(maps.get(mapIndex).name)) {
				unloadMap(oldMap);
			}
		}
	}

	/**
	 * Loads the current map
	 */
	public static void loadMap() {
		// Clear the scoreboard
		Scoreboard.clearScoreboard();

		// Register the flag regions
		for (Flag flag : maps.get(mapIndex).flags) {
			if (flag.region != null) {
				Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(getWorld(maps.get(mapIndex).worldName))))).addRegion(flag.region);
			}
		}

		// Register doors
		for (Door door : maps.get(mapIndex).doors) {
			Main.plugin.getServer().getPluginManager().registerEvents(door, Main.plugin);
		}

		// Register gates
		for (Gate gate : maps.get(mapIndex).gates) {
			Main.plugin.getServer().getPluginManager().registerEvents(gate, Main.plugin);
		}

		// Register the woolmap clicks
		for (Team team : maps.get(mapIndex).teams) {
			getServer().getPluginManager().registerEvents(team.lobby.woolmap, Main.plugin);
		}

		// Move all players to the new map and team
		if (!keepTeams || teams.size() > 0 && maps.get(mapIndex).teams.length > teams.size()) {
			for (Player player : Main.plugin.getServer().getOnlinePlayers()) {
				if (!SpectateCommand.spectators.contains(player.getUniqueId())) {
					joinATeam(player.getUniqueId());
					Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
				}
			}
		} else {
			Collections.shuffle(teams);
			for (int i = 0; i < teams.size(); i++) {
				for (UUID uuid : teams.get(i)) {
					// Check the player exists
					Player player = Bukkit.getPlayer(uuid);
					if (player != null) {
						if (maps.get(mapIndex).teams.length < i - 1)
							maps.get(mapIndex).teams[i].addPlayer(uuid);
						else
							joinATeam(uuid);
					}
				}
			}
			// Make sure all online players are on a team
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!SpectateCommand.spectators.contains(player.getUniqueId())) {
					if (maps.get(mapIndex).getTeam(player.getUniqueId()) == null) {
						joinATeam(player.getUniqueId());
					}
				}
			}
		}

		// Teleport Spectators
		int i = 0;
		while (i < SpectateCommand.spectators.size()) {
			Player player = Bukkit.getPlayer(SpectateCommand.spectators.get(i));
			if (player != null) {
				player.teleport(getCurrentMap().flags[0].spawnPoint);
			}
		}

		// Setup the time
		World world = Bukkit.getWorld(maps.get(mapIndex).worldName);
		assert world != null;
		world.setTime(maps.get(mapIndex).startTime);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, maps.get(mapIndex).daylightCycle);

		// Start the timer!
		timer = new Timer(getCurrentMap().duration.getFirst(), getCurrentMap().duration.getSecond());
	}

	/**
	 * Does any unloading needed for the current map
	 */
	public static void unloadMap(String worldName) {
		InCombat.clearCombat();
		MVPStats.reset();

		for (Flag flag : getCurrentMap().flags) {
			flag.clear();
		}

		for (Map map:maps) {
			if (Objects.equals(map.worldName, worldName)) {
				for (Team team:map.teams) {
					if (keepTeams) {
						teams.add(new ArrayList<>(team.getPlayers()));
					} else {
						team.clear();
					}
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
		return maps.get(mapIndex).name.equalsIgnoreCase(mapName);
	}

	/**
	 * Checks if the game is on the last map
	 * @return if the game is on the final map
	 */
	public static boolean finalMap() {
		return mapIndex == maps.size() - 1 || mapIndex >= mapCount;
	}

	/**
	 * Gets the name of the current map
	 * @return A string containing the current map's name
	 */
	public static Map getCurrentMap() {
		return maps.get(mapIndex);
	}

	/**
	 * Checks if the current map has ended
	 * @return If all the flags belong to the same team
	 */
	public static Boolean hasMapEnded() {
		if (timer.hasGameEnded) {
			return true;
		}

		String startingTeam = getCurrentMap().flags[0].getCurrentOwners();
		if (startingTeam == null) {
			return false;
		}
		for (Flag flag : getCurrentMap().flags) {
			if (!startingTeam.equalsIgnoreCase(flag.getCurrentOwners())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds a player to a team on the current map
	 * @param uuid the player to add to a team
	 */
	public static void joinATeam(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		Team team = maps.get(mapIndex).smallestTeam();

		team.addPlayer(uuid);
		assert player != null;
		if (team.lobby.spawnPoint.getWorld() == null) {
			team.lobby.spawnPoint.setWorld(Bukkit.getWorld(getCurrentMap().worldName));
		}

		player.teleport(team.lobby.spawnPoint);
		NameTag.give(player);

		player.sendMessage("You joined" + team.primaryChatColor + " " + team.name);
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
		player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
		} else {
			if (isSpectator(uuid)) {
				SpectateCommand.spectators.remove(uuid);
			}
		}
	}

	/**
	 * Checks if a player is a spectator
	 */
	public static boolean isSpectator(UUID uuid) {
		return SpectateCommand.spectators.contains(uuid);
	}

	private static void copyFileStructure(File source, File target){
		if (target == null) {
			getLogger().severe("No world to load for " + source.getName() + ". Could not reset the map!");
			return;
		}
		try {
			FileUtils.copyDirectory(source, target);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save map command.
	 * Saves the current map to the backup save
	 * TODO - FIX!
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (sender instanceof ConsoleCommandSender || (sender instanceof Player
				&& "AdminDeveloperModerator".contains((ActiveData.getData(((Player) sender).getUniqueId()).getStaffRank()))))
		{
			sender.sendMessage(ChatColor.DARK_AQUA + "Saving world: " + getCurrentMap().worldName);

			Objects.requireNonNull(getWorld(getCurrentMap().worldName)).save();
			copyFileStructure(new File(Bukkit.getWorldContainer(), getCurrentMap().worldName + "_save"), Objects.requireNonNull(getWorld(getCurrentMap().worldName)).getWorldFolder());

			sender.sendMessage(ChatColor.GREEN + "Saved " + getCurrentMap().worldName + "!");
		} else {
			Messenger.sendError("You don't have permission to do that!", sender);
		}
		return true;
	}
}
