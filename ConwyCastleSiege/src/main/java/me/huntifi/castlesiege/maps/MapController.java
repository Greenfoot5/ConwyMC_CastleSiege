package me.huntifi.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.info.leaderboard.MVPCommand;
import me.huntifi.castlesiege.commands.staff.boosters.GrantBooster;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.data_types.*;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.gameplay.Explosion;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.maps.objects.*;
import me.huntifi.castlesiege.secrets.SecretItems;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.Bukkit.*;

/**
 * Manages what map the game is currently on
 */
public class MapController {

	// Boosters - chances
	private static final double BASE_BOOSTER_CHANCE = 0.15;
	private static final double COIN_BOOSTER_CHANCE = 0.35;
	private static final double BATTLEPOINT_BOOSTER_CHANCE = 0.3;
	//private static final double KIT_BOOSTER_CHANCE = 0.35; // Not actually used, set for reference
	// Boosters - limits/sub-chances
	private static final int COIN_BOOSTER_MAX_TIME = 9000;
	private static final int COIN_BOOSTER_MIN_TIME = 1800;
	private static final double COIN_BOOSTER_GAUSSIAN_DIV = 2.75;
	private static final double COIN_BOOSTER_GAUSSIAN_ADD = 3;
	private static final int BP_BOOSTER_MAX_TIME = 2700;
	private static final int BP_BOOSTER_MIN_TIME = 300;
	private static final double BP_BOOSTER_MULT_CHANCE = 0.5;
	private static final double BP_BOOSTER_MAX_MULT = 2.5;
	private static final double BP_BOOSTER_MIN_MULT = -1;
	private static final int KIT_BOOSTER_MAX_TIME = 9000;
	private static final int KIT_BOOSTER_MIN_TIME = 1800;
	private static final double KIT_BOOSTER_RANDOM_CHANCE = 0.35;
	private static final double KIT_BOOSTER_WILD_CHANCE = 0.15;


	public static List<Map> maps = new ArrayList<>();
	public static int mapIndex = 0;
	public static Timer timer;

	public static int mapCount = 3;

	// Delays
	public static int preGameTime = 0;
	public static int lobbyLockedTime = 0;
	public static int explorationTime = 0;

	public static boolean isMatch = false;
	public static boolean keepTeams = false;
	private static final ArrayList<ArrayList<UUID>> teams = new ArrayList<>();
	public static boolean disableSwitching = false;
	public static boolean allKitsFree = false;
	public static boolean forcedRandom = false;

	/**
	 * Begins the map loop
	 */
	public static void startLoop() {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			if (!isMatch) {
				Collections.shuffle(maps);
				if (mapCount > 0 && mapCount < maps.size())
					maps = maps.subList(0, mapCount);
			}
			Bukkit.getScheduler().runTask(Main.plugin, MapController::loadMap);
		});
	}

	/**
	 * @param mapName The name of the map to get
	 * @return Gets the map only if it's unplayed, null if no map of that name has been unplayed
	 */
	public static Map getUnplayedMap(String mapName) {
		for (int i = mapIndex + 1; i < maps.size(); i++) {
			if (Objects.equals(maps.get(i).name, mapName)) {
				return maps.get(i);
			}
		}
		return null;
	}

	/**
	 * @param mapName The name of the map to get
	 * @return The Map of the mapname, null if no map of that name exists
	 */
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
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
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
		});
	}

	/**
	 * Sets the current map by string
	 * @param mapName the name of the map to set the current map to
	 */
	public static void setMap(String mapName) {
		timer.state = TimerState.ENDED;
		Map oldMap = maps.get(mapIndex);
		for (int i = 0; i < maps.size(); i++) {
			if (Objects.equals(maps.get(i).name, mapName)) {
				getLogger().info("Loading map - " + mapName);
				mapIndex = i;
				unloadMap(oldMap);
				loadMap();
				return;
			}
		}
	}

	/**
	 * Handles ending the map
	 * (called by the timer or if all flags are captured)
	 */
	public static void endMap() {
		timer.state = TimerState.ENDED;

		// Calculate the winner based on the game mode
		String winners = null;
		switch(getCurrentMap().gamemode) {
			case Control:
				getLogger().severe("Control game mode has not been implemented yet! It's a draw!");
				break;
			case Charge:
				// Check if the defenders have won
				for (Flag flag : getCurrentMap().flags) {
					if (flag.isActive() && Objects.equals(flag.getCurrentOwners(), getCurrentMap().teams[0].name)) {
						winners = getCurrentMap().teams[0].name;
					}
				}
				break;
			case Assault:
			case Domination:
			default:
				// Get a count of who owns which flag
				java.util.Map<String, Integer> flagCounts = new HashMap<>();
				for (Flag flag : getCurrentMap().flags) {
					if (flag.isActive() && !flag.getCurrentOwners().equals("null")) {
						flagCounts.merge(flag.getCurrentOwners(), 1, Integer::sum);
					}
				}
				// Get the team with the largest
				String currentWinners = (String) flagCounts.keySet().toArray()[0];
				winners = currentWinners;
				for (String teamName : flagCounts.keySet()) {
					if (flagCounts.get(teamName) > flagCounts.get(currentWinners)) {
						winners = teamName;
						currentWinners = teamName;
					// If two teams are the largest, we set up for a draw
					} else if (flagCounts.get(teamName).equals(flagCounts.get(currentWinners))) {
						winners = null;
					}
				}
				break;
		}
		// Moves all players to the lobby
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					Team team = TeamController.getTeam(player.getUniqueId());
					if (team != null) {
						player.teleport(team.lobby.spawnPoint);
					}

					// Refund the player's bp if they didn't die
					UUID uuid = player.getUniqueId();
					if (!InCombat.isPlayerInLobby(uuid))
					{
						Kit kit = Kit.equippedKits.get(uuid);
						if (kit instanceof DonatorKit) {
							DonatorKit dKit = (DonatorKit) kit;
							ActiveData.getData(player.getUniqueId()).addBattlepointsClean(dKit.getBattlepointPrice());
						}
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
		if (winners != null) {
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
		// The map was a draw
		} else {
			for (Team team : getCurrentMap().teams) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(team.primaryChatColor + "~~~~~~~~" + team.name + " has drawn!~~~~~~~~");

				// Broadcast MVP
				for (String message : MVPCommand.getMVPMessage(team)) {
					Bukkit.broadcastMessage(message);
				}
			}
		}
		AssistKill.reset();
		Explosion.reset();
		awardMVPs();

		// Begins the next map
		new BukkitRunnable() {
			@Override
			public void run() {
				nextMap();
			}
		}.runTaskLater(Main.plugin, 150);

	}

	/**
	 * Awards MVPs to the correct players, and grants boosters as necessary
	 * They are only granted if the map has had 2 players per team earn at least 20 points
	 */
	private static void awardMVPs() {
		// Check if the map has enough activity
		int minimumCount = 0;
		for (PlayerData data : MVPStats.getStats().values()) {
			if (data.getScore() >= 20) {
				minimumCount++;
			}
		}
		if (minimumCount < getCurrentMap().teams.length * 2) {
			return;
		}

		Random random = new Random();
		for (Team team : getCurrentMap().teams) {
			UUID uuid = team.getMVP().getFirst();
			PlayerData data = ActiveData.getData(uuid);
			data.addMVP();

			// Find out if boosters should be awarded
			if (random.nextDouble() < BASE_BOOSTER_CHANCE) {
				Booster booster;
				double boosterChoice = random.nextDouble();
				if (boosterChoice < COIN_BOOSTER_CHANCE) {
					int duration = random.nextInt((COIN_BOOSTER_MAX_TIME - COIN_BOOSTER_MIN_TIME) + 1) + COIN_BOOSTER_MIN_TIME;
					double mult = (random.nextGaussian() + COIN_BOOSTER_GAUSSIAN_ADD) / COIN_BOOSTER_GAUSSIAN_DIV;
					mult = Math.abs(mult);
					booster = new CoinBooster(duration, mult);
				}
				else if (boosterChoice + COIN_BOOSTER_CHANCE < BATTLEPOINT_BOOSTER_CHANCE) {
					int duration = random.nextInt((BP_BOOSTER_MAX_TIME - BP_BOOSTER_MIN_TIME) + 1) + BP_BOOSTER_MIN_TIME;
					double mult = (BP_BOOSTER_MAX_MULT - BP_BOOSTER_MIN_MULT) * random.nextDouble() + BP_BOOSTER_MIN_MULT;
					if (random.nextDouble() < BP_BOOSTER_MULT_CHANCE) {
						booster = new BattlepointBooster(duration);
					} else {
						booster = new BattlepointBooster(duration, mult);
					}
				}
				else {
					int duration = random.nextInt((KIT_BOOSTER_MAX_TIME - KIT_BOOSTER_MIN_TIME) + 1) + KIT_BOOSTER_MIN_TIME;
					String kit;
					double boosterType = random.nextDouble();
					if (boosterType < KIT_BOOSTER_RANDOM_CHANCE) {
						kit = "random";
					} else if (boosterType + KIT_BOOSTER_RANDOM_CHANCE < KIT_BOOSTER_WILD_CHANCE) {
						kit = "wild";
					} else {
						ArrayList<String> dKits = (ArrayList<String>) DonatorKit.getKits();
						do {
							kit = dKits.get(new Random().nextInt(dKits.size()));
						} while (Kit.getKit(kit) instanceof TeamKit);
					}
					booster = new KitBooster(duration, kit);
				}

				GrantBooster.updateDatabase(uuid, booster);
				Player player = Bukkit.getPlayer(uuid);
				if (player != null) {
					Messenger.sendSuccess("You gained a " + booster.getName() + " for being MVP!", player);
				}
			}
		}
	}

	/**
	 * Increments the map index by one
	 * starts the loading of the next map
	 */
	private static void nextMap() {
		MVPStats.reset();

		Map oldMap = maps.get(mapIndex);
		if (finalMap()) {
			getLogger().info("Completed map cycle! Restarting server...");
			getServer().spigot().restart();
		}
		else {
			mapIndex++;
			getLogger().info("Loading next map: " + maps.get(mapIndex).name);
			unloadMap(oldMap);
			loadMap();
		}
	}

	/**
	 * Loads the current map into play
	 */
	public static void loadMap() {
		// Clear the scoreboard & reset stats
		Scoreboard.clearScoreboard();
		MVPStats.reset();

		// Register doors
		for (Door door : maps.get(mapIndex).doors) {
			Main.plugin.getServer().getPluginManager().registerEvents(door, Main.plugin);
		}

		// Register the woolmap clicks
		for (Team team : maps.get(mapIndex).teams) {
			getServer().getPluginManager().registerEvents(team.lobby.woolmap, Main.plugin);
		}

		// Move all players to the new map and team
		if (!keepTeams || maps.get(mapIndex).teams.length < teams.size()) {
			for (Player player : Main.plugin.getServer().getOnlinePlayers()) {
				if (!SpectateCommand.spectators.contains(player.getUniqueId()))
					joinATeam(player.getUniqueId());
			}
		} else {
			Collections.shuffle(teams);
			for (int i = 0; i < teams.size(); i++) {
				for (UUID uuid : teams.get(i))
					joinTeam(uuid, getCurrentMap().teams[i]);
			}
			teams.clear();
		}

		//Spawn secret items if there are any
		SecretItems.spawnSecretItems();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Spawning secret items if there are any.");

		// Teleport Spectators
		for (UUID spectator : SpectateCommand.spectators) {
			Player player = Bukkit.getPlayer(spectator);
			if (player != null && player.isOnline()) {
				player.teleport(getCurrentMap().flags[0].spawnPoint);
			}
		}

		// Set up the time
		World world = Bukkit.getWorld(maps.get(mapIndex).worldName);
		assert world != null;
		world.setTime(maps.get(mapIndex).startTime);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, maps.get(mapIndex).daylightCycle);

		// Start the timer!
		if (mapIndex == 0) {
			// Pregame
			if (preGameTime > 0) {
				timer = new Timer(preGameTime / 60, preGameTime % 60, TimerState.PREGAME);
				return;
			} else if (preGameTime < 0) {
				timer = new Timer(-1, -1, TimerState.PREGAME);
				return;
			}
		}
		// Explore Time
		if (explorationTime > 0) {
			timer = new Timer(explorationTime / 60, explorationTime % 60, TimerState.EXPLORATION);
		} else if (explorationTime < 0) {
			timer = new Timer(-1, -1, TimerState.EXPLORATION);
		// Lobby time
		} else if (lobbyLockedTime > 0) {
			timer = new Timer(lobbyLockedTime / 60, lobbyLockedTime % 60, TimerState.LOBBY_LOCKED);
		} else if (lobbyLockedTime < 0) {
			timer = new Timer(-1, -1, TimerState.PREGAME);
		// No timer
		} else {
			beginMap();
			timer = new Timer(getCurrentMap().duration.getFirst(), getCurrentMap().duration.getSecond(), TimerState.ONGOING);
		}
	}

	/**
	 * Starts the exploration phase
	 */
	public static void beginExploration() {
		if (explorationTime > 1) {
			timer.restartTimer(explorationTime / 60, explorationTime % 60);
		} else if (explorationTime == 0) {
			timer.startTimer();
		} else {
			timer.seconds = explorationTime;
		}
	}

	/**
	 * Starts the lobby phase of the map
	 */
	public static void beginLobbyLock() {
		if (explorationTime != 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!isSpectator(player.getUniqueId())) {
					Bukkit.getScheduler().runTask(Main.plugin, () -> player.setHealth(0));
				}
			}
		}

		if (lobbyLockedTime > 1) {
			timer.restartTimer(lobbyLockedTime / 60, lobbyLockedTime % 60);
		} else if (lobbyLockedTime == 0) {
			timer.startTimer();
		} else {
			timer.seconds = lobbyLockedTime;
		}
	}

	/**
	 * Starts a map
	 */
	public static void beginMap() {
		new BukkitRunnable() {
			@Override
			public void run() {
				// Register catapults
				for (Catapult catapult : maps.get(mapIndex).catapults) {
					Main.plugin.getServer().getPluginManager().registerEvents(catapult, Main.plugin);
				}

				// Register gates
				for (Gate gate : maps.get(mapIndex).gates) {
					Main.plugin.getServer().getPluginManager().registerEvents(gate, Main.plugin);
					Ram ram = gate.getRam();
					if (ram != null) {
						Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
										BukkitAdapter.adapt(Objects.requireNonNull(getWorld(maps.get(mapIndex).worldName)))))
								.addRegion(ram.getRegion());
					}
				}

				// Register the flag regions
				for (Flag flag : maps.get(mapIndex).flags) {
					if (flag.region != null) {
						Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
								BukkitAdapter.adapt(Objects.requireNonNull(getWorld(maps.get(mapIndex).worldName)))))
								.addRegion(flag.region);

						if (flag.isActive())
							flag.createHologram();
					}
				}

				for (ArrayList<UUID> t : teams) {
					for (UUID uuid : t) {
						Player player = getPlayer(uuid);
						if (player == null) continue;
						Team team = TeamController.getTeam(uuid);
						player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~");
						player.sendMessage(team.primaryChatColor + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					}
				}
			}
		}.runTask(Main.plugin);

		if (timer != null) {
			timer.restartTimer(getCurrentMap().duration.getFirst(), getCurrentMap().duration.getSecond());
		}
	}

	/**
	 * Restocks the players kit, or sets them to swordsman if they were using a team kit
	 * @param player The player to restock/reset kits for
	 */
	private static void checkTeamKit(Player player) {
		Kit kit = Kit.equippedKits.get(player.getUniqueId());
		if (kit == null)
			return;

		if (kit instanceof TeamKit) {
			Kit.equippedKits.put(player.getUniqueId(), new Swordsman());
			ActiveData.getData(player.getUniqueId()).setKit("swordsman");
		}

		Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
	}

	/**
	 * Does any unloading needed for the a map
	 * @param oldMap The map to unload
	 */
	public static void unloadMap(Map oldMap) {
		// Make sure teams are stored before the next map is loaded
		if (keepTeams) {
			for (Team team : oldMap.teams)
				teams.add(new ArrayList<>(team.getPlayers()));
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
		// Clear map stats
		InCombat.clearCombat();

		// Clear capture zones
		for (Flag flag : oldMap.flags) {
			flag.clear();
		}

		// Unregister catapult listeners
		for (Catapult catapult : oldMap.catapults) {
			HandlerList.unregisterAll(catapult);
		}

		// Unregister flag regions
		for (Flag flag : oldMap.flags) {
			if (flag.region != null) {
				Bukkit.getScheduler().runTask(Main.plugin, () ->
						Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
								BukkitAdapter.adapt(Objects.requireNonNull(getWorld(oldMap.worldName)))))
						.removeRegion(flag.name.replace(' ', '_')));
			}
		}

		// Unregister door listeners
		for (Door door : oldMap.doors) {
			HandlerList.unregisterAll(door);
		}

		// Unregister gate listeners
		for (Gate gate : oldMap.gates) {
			HandlerList.unregisterAll(gate);
			Ram ram = gate.getRam();
			if (ram != null) {
				Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
								BukkitAdapter.adapt(Objects.requireNonNull(getWorld(oldMap.worldName)))))
						.removeRegion(ram.getRegion().getId());
			}
		}

		// Unregister woolmap listeners and clear teams
		for (Team team : oldMap.teams) {
			HandlerList.unregisterAll(team.lobby.woolmap);
			team.clear();
		}
	 });
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
		if (timer.state == TimerState.ENDED) {
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
		joinTeam(uuid, getCurrentMap().smallestTeam());
	}

	/**
	 * Adds a player to a specific team.
	 * @param uuid The player's unique ID
	 * @param team The team
	 */
	private static void joinTeam(UUID uuid, Team team) {
		Player player = Bukkit.getPlayer(uuid);
		assert player != null;

		team.addPlayer(uuid);

		player.teleport(team.lobby.spawnPoint);
		NameTag.give(player);

		player.sendMessage("You joined" + team.primaryChatColor + " " + team.name);

		checkTeamKit(player);
	}


	/**
	 * Removes a player from the team when they disconnect
	 * @param uuid the uuid to remove
	 */
	public static void leaveTeam(UUID uuid) {
		Team team = TeamController.getTeam(uuid);
		if (team != null)
			team.removePlayer(uuid);
		else if (isSpectator(uuid))
			SpectateCommand.spectators.remove(uuid);
	}

	/**
	 * Checks if a player is a spectator
	 * @param uuid The uuid of the player to check
	 */
	public static boolean isSpectator(UUID uuid) {
		return SpectateCommand.spectators.contains(uuid);
	}

	/**
	 * @return true if the map is ongoing, false otherwise (i.e. exploration phase)
	 */
	public static boolean isOngoing() {
		return timer.state == TimerState.ONGOING;
	}
}
