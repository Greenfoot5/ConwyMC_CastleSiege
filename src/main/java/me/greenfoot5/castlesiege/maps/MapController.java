package me.greenfoot5.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.commands.gameplay.VoteSkipCommand;
import me.greenfoot5.castlesiege.commands.info.leaderboard.MVPCommand;
import me.greenfoot5.castlesiege.commands.staff.boosters.GrantBoosterCommand;
import me.greenfoot5.castlesiege.data_types.Booster;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.data_types.CSStats;
import me.greenfoot5.castlesiege.data_types.CoinBooster;
import me.greenfoot5.castlesiege.data_types.KitBooster;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.database.StoreData;
import me.greenfoot5.castlesiege.events.combat.AssistKill;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.gameplay.Explosion;
import me.greenfoot5.castlesiege.events.map.NextMapEvent;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.objects.Cannon;
import me.greenfoot5.castlesiege.maps.objects.Catapult;
import me.greenfoot5.castlesiege.maps.objects.Core;
import me.greenfoot5.castlesiege.maps.objects.Door;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import me.greenfoot5.castlesiege.maps.objects.Gate;
import me.greenfoot5.castlesiege.maps.objects.Ram;
import me.greenfoot5.castlesiege.secrets.SecretItems;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.database.LoadData;
import me.greenfoot5.conwymc.gui.Gui;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.SequencedCollection;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getWorld;

/**
 * Manages what map the game is currently on
 */
public class MapController {

	// Boosters - chances
	private static final double BASE_BOOSTER_CHANCE = 0.075;
	private static final double COIN_BOOSTER_CHANCE = 0.4;
	//private static final double KIT_BOOSTER_CHANCE = 0.6; // Not actually used, set for reference

	// Boosters - limits/sub-chances
	private static final int COIN_BOOSTER_MAX_TIME = 9000;
	private static final int COIN_BOOSTER_MIN_TIME = 1800;
	private static final double COIN_BOOSTER_GAUSSIAN_DIV = 2.75;
	private static final double COIN_BOOSTER_GAUSSIAN_ADD = 3;
	private static final int KIT_BOOSTER_MAX_TIME = 9000;
	private static final int KIT_BOOSTER_MIN_TIME = 1800;
	private static final double KIT_BOOSTER_RANDOM_CHANCE = 0.35;
	private static final double KIT_BOOSTER_WILD_CHANCE = 0.10;

	private static final SequencedCollection<Map> mapsInRotation = new LinkedHashSet<>();

	private static List<Map> maps = new ArrayList<>();
	private static int mapIndex = 0;

	public static Timer timer;
	public static int mapCount = 3;

	// Delays
	public static int preGameTime = 0;
	public static int lobbyLockedTime = 0;
	public static int explorationTime = 0;

	// If we're treating the game as a match
	public static boolean isMatch = false;
	// Disables purchase check for kits
	public static boolean allKitsFree = false;
	// Randomises a player's kit when they respawn
	public static boolean forcedRandom = false;

	// Has the map rotation started
	public static boolean hasStarted = false;

	/**
	 * Adds a map to the potential map rotation
	 * @param map The map to add to the rotation
	 */
	public static void addMapToRotation(@NotNull Map map) {
		mapsInRotation.add(map);
	}

	/**
	 * Resets the list of maps to potentially add to rotation
	 */
	public static void resetMapsInRotation() {
		mapsInRotation.clear();
		timer = null;
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
					Main.instance.getLogger().severe("Could not load match mode. Could not find map: `" + mapName + "`");
				}
			}
			maps = newMaps;
		});
	}

	/**
	 * Shuffles the map list if the server hasn't started yet
	 */
	public static void shuffle() {
		if (mapIndex == 0 && (timer == null || timer.state == TimerState.PREGAME)) {
			maps = new ArrayList<>(mapsInRotation);
			Collections.shuffle(maps);
			mapsInRotation.clear();
		}
	}

	/**
	 * Begins the map loop
	 */
	public static void startLoop() {
		mapIndex = 0;
		if (!isMatch) {
			shuffle();
			if (mapCount > 0 && mapCount < maps.size())
				maps = maps.subList(0, mapCount);
		}
		loadMap(false);
		hasStarted = true;
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
	 * Gets all the maps in rotation
	 * @return The list of maps currently in rotation, played or not
	 */
	public static List<Map> getMaps() {
		return maps;
	}

	/**
	 * @param mapName The name of the map to get
	 * @return The Map of the map name, null if no map of that name exists
	 */
	public static Map getMap(String mapName) {
		for (Map map : maps) {
			if (Objects.equals(map.name, mapName))
				return map;
		}
		return null;
	}

	/**
	 * Sets the current map by string
	 * @param mapName the name of the map to set the current map to
	 */
	public static void setMap(String mapName) {
		timer.state = TimerState.ENDED;
		Map oldMap = maps.get(mapIndex);
		VoteSkipCommand.clearVotes();
		for (int i = 0; i < maps.size(); i++) {
			if (Objects.equals(maps.get(i).name, mapName)) {
				Main.instance.getLogger().info("Loading map - " + mapName);
				mapIndex = i;
				unloadMap(oldMap);
				loadMap(false);
				return;
			}
		}
	}

	/**
	 * Ends the current map, and starts the next one
	 */
	public static void endMap() {
		forceEndMap();

		// Begins the next map
		new BukkitRunnable() {
			@Override
			public void run() {
				nextMap();
			}
		}.runTaskLater(Main.plugin, 200);
	}

	/**
	 * Handles ending the map
	 * (called by the timer or if all flags are captured)
	 */
	public static void forceEndMap() {
		timer.state = TimerState.ENDED;

		// Calculate the winner based on the game mode
		String winners = getWinners();

		// Moves all players to the lobby
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					Team team = TeamController.getTeam(player.getUniqueId());
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
		if (winners != null) {
			for (Team team : getCurrentMap().teams) {
				if (team.getName().equals(winners)) {
					Messenger.broadcast(Component.text("~~~~~~~~" + team.getName() + " has won!~~~~~~~~", team.primaryChatColor));
				} else {
					Messenger.broadcast(Component.text("~~~~~~~~" + team.getName() + " has lost!~~~~~~~~", team.primaryChatColor));
				}

				Messenger.broadcast(MVPCommand.getMVPMessage(team));

				if (team.getName().equals(winners)) {
					//giveCoinReward(team);
					Tuple<UUID, CSStats> mvp = team.getMVP();
					if (mvp != null) {
						Bukkit.getScheduler().runTask(Main.plugin, () -> {
							Player player = getPlayer(mvp.getFirst());
							if (player == null) {
								player = Bukkit.getOfflinePlayer(mvp.getFirst()).getPlayer();
							}
							assert player != null;
						});
					}
				}

			}
		// The map was a draw
		} else {
			for (Team team : getCurrentMap().teams) {
				Messenger.broadcast(Component.text("~~~~~~~~" + team.getName() + " has drawn!~~~~~~~~", team.primaryChatColor));

				Messenger.broadcast(MVPCommand.getMVPMessage(team));
			}
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				beginVote();
			}
		}.runTask(Main.plugin);

		VoteSkipCommand.clearVotes();
		AssistKill.reset();
		Explosion.reset();
		awardMVPs();
	}

	private static String getWinners() {
		String lastPlace = null;
		if (getCurrentMap() instanceof CoreMap coreMap) {
			for (Team team : getCurrentMap().teams) {
				int survivingCores = 0;
				for (Core core : coreMap.getCores()) {
					if (!core.isDestroyed)
						survivingCores++;
				}

				if (survivingCores == 0)
					lastPlace = team.getName();
			}

			if (coreMap.flags.length == 0)
				return null;
		} else if (getCurrentMap().gamemode == Gamemode.Charge) {
			// Check if the defenders have won
			for (Flag flag : getCurrentMap().flags) {
				if (Objects.equals(flag.getCurrentOwners(), getCurrentMap().teams[0].getName())) {
					return getCurrentMap().teams[0].getName();
				} else {
					return getCurrentMap().teams[1].getName();
				}
			}
		} else if (getCurrentMap().gamemode == Gamemode.Assault) {
			// Find and exclude the team that lost all their lives
			for (Team team : getCurrentMap().teams) {
				if (team.getLives() == 0) {
					lastPlace = team.getName();
				}
			}
		}

		// If there are only teams, the winner is the not loser
		if (getCurrentMap().teams.length == 2 && lastPlace != null) {
			for (Team team : getCurrentMap().teams) {
				if (!Objects.equals(team.getName(), lastPlace)) {
					return team.getName();
				}
			}
		}

		// If no other method has declared a winner, use domination

		// Get a count of who owns which flag
		java.util.Map<String, Integer> flagCounts = new HashMap<>();
		java.util.Map<String, Integer> notStaticFlags = new HashMap<>();
		for (Flag flag : getCurrentMap().flags) {
			if (flag.isActive() && !flag.getCurrentOwners().equals("null") && !flag.getCurrentOwners().equals(lastPlace)) {
				flagCounts.merge(flag.getCurrentOwners(), 1, Integer::sum);
				if (!flag.isStatic())
					notStaticFlags.merge(flag.getCurrentOwners(), 1, Integer::sum);
			}
		}

		// If only one team controls flags all the non-static, they win
		if (notStaticFlags.size() == 1) {
			return (String) notStaticFlags.keySet().toArray()[0];
		}

		// If recapture is disabled, defenders win
		// Also known as the team starting with the most non-static flags
		if (!MapController.getCurrentMap().canRecap) {
			flagCounts = new HashMap<>();
			for (Flag flag : getCurrentMap().flags) {
				if (flag.isActive() && !flag.getStartingOwners().equals("null") && !flag.isStatic() && !flag.getCurrentOwners().equals(lastPlace)) {
					flagCounts.merge(flag.getStartingOwners(), 1, Integer::sum);
				}
			}
			return getWinnersFromFlags(flagCounts, true);
		}

		// Just get the team with the most flags
		return getWinnersFromFlags(flagCounts, false);
	}

    private static String getWinnersFromFlags(@NotNull java.util.Map<String, Integer> flagCounts, boolean findSmallest) {
		// Get the team that started with the least number of flags (the defenders)
		String currentWinners = (String) flagCounts.keySet().toArray()[0];
		for (String teamName : flagCounts.keySet()) {
			if ((!findSmallest && flagCounts.get(teamName) > flagCounts.get(currentWinners)) ||
			findSmallest && flagCounts.get(teamName) < flagCounts.get(currentWinners)) {
				currentWinners = teamName;
				// If two teams are the largest, we set up for a draw
			} else if (!Objects.equals(teamName, currentWinners) && Objects.equals(flagCounts.get(teamName), flagCounts.get(currentWinners))) {
				break;
			}
		}

		return currentWinners;
	}

	/**
	 * Gives a 50 coins reward for winning the game, is also affected by coin boosters.
     * @param team The team to grant the coins to
     */
	public static void giveCoinReward(Team team) {

		for (UUID uuid : team.getPlayers()) {
			Player p = getPlayer(uuid);
			assert p != null;
			double score = MVPStats.getStats(p.getUniqueId()).getScore();

			if (TeamController.getPlayers().size() >= 6 && score >= 20) {
				CSActiveData.getData(p.getUniqueId()).addCoins(50 * CSPlayerData.getCoinMultiplier());
				Messenger.sendSuccess("<gold>+" + (50 * CSPlayerData.getCoinMultiplier()) + "</gold> coins for winning!", p);
			}
		}
	}

	/**
	 * Awards MVPs to the correct players, and grants boosters as necessary
	 * They are only granted if the map has had 2 players per team earn at least 20 points
	 */
	private static void awardMVPs() {
		// Check if the map has enough activity
		int minimumCount = 0;
		for (CSStats data : MVPStats.getStats().values()) {
			if (data.getScore() >= 20) {
				minimumCount++;
			}
		}
		if (minimumCount < getCurrentMap().teams.length * 2) {
			return;
		}

		Random random = new Random();
		for (Team team : getCurrentMap().teams) {
			Tuple<UUID, CSStats> mvp = team.getMVP();
			if (mvp == null)
				continue; // Continue to the next team if this one doesn't have an MVP
			UUID uuid = mvp.getFirst();
			CSPlayerData data = CSActiveData.getData(uuid);
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
				else {
					int duration = random.nextInt((KIT_BOOSTER_MAX_TIME - KIT_BOOSTER_MIN_TIME) + 1) + KIT_BOOSTER_MIN_TIME;
					String kit;
					double boosterType = random.nextDouble();
					if (boosterType < KIT_BOOSTER_RANDOM_CHANCE) {
						kit = "random";
					} else if (boosterType + KIT_BOOSTER_RANDOM_CHANCE < KIT_BOOSTER_WILD_CHANCE) {
						kit = "wild";
					} else {
						ArrayList<String> dKits = (ArrayList<String>) CoinKit.getKits();
						kit = dKits.get(new Random().nextInt(dKits.size()));
					}
					booster = new KitBooster(duration, kit);
				}

				GrantBoosterCommand.updateDatabase(uuid, booster);
				data.addBooster(booster);
				Player player = getPlayer(uuid);
				if (player != null) {
					Messenger.broadcastSuccess(player.displayName()
							.append(Component.text(" gained a "))
							.append(booster.getName())
							.append(Component.text(" for being MVP!")));
				}
			}
		}
	}

	/**
	 * Increments the map index by one
	 * starts the loading of the next map
	 */
	private static void nextMap() {
		Map oldMap = maps.get(mapIndex);
		if (finalMap()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					NextMapEvent event = new NextMapEvent(oldMap.name, false);
					Bukkit.getPluginManager().callEvent(event);

					// Save all data + reset mvp stats
					StoreData.storeAll();

					Main.instance.getLogger().info("Completed map cycle! Restarting server...");
					Main.instance.reloadMaps();
				}
			}.runTask(Main.plugin);
		} else {
			new BukkitRunnable() {
				@Override
				public void run() {
					NextMapEvent event = new NextMapEvent(oldMap.name, true);
					Bukkit.getPluginManager().callEvent(event);
				}
			}.runTaskAsynchronously(Main.plugin);

			mapIndex++;
			Main.instance.getLogger().info("Loading next map: " + maps.get(mapIndex).name);
			unloadMap(oldMap);
			loadMap(false);
		}
	}

	/**
	 * Loads the current map into play
	 */
	public static void loadMap(boolean setMap) {
		// Clear the scoreboard & reset stats
		Scoreboard.clearScoreboard();
		MVPStats.reset();
		hasStarted = true;

		// Register doors
		for (Door door : maps.get(mapIndex).doors) {
			Main.plugin.getServer().getPluginManager().registerEvents(door, Main.plugin);
		}

		// Register the woolmap clicks
		for (Team team : maps.get(mapIndex).teams) {
			getServer().getPluginManager().registerEvents(team.lobby.woolmap, Main.plugin);
			World world = Bukkit.getWorld(maps.get(mapIndex).worldName);
			world.getChunkAt(team.lobby.spawnPoint);
		}

		// Add players to new map and teleport
		if (mapIndex > 0 && !setMap) {
			TeamController.loadTeams(maps.get(mapIndex - 1), getCurrentMap());
		} else {
			TeamController.loadTeams(getCurrentMap());
		}
		TeamController.teleportPlayers();

		//Spawn secret items if there are any
		SecretItems.spawnSecretItems();
		Main.plugin.getComponentLogger().info(Component.text("Spawning secret items if there are any.", NamedTextColor.DARK_GREEN));

		// Set up the time
		World world = getWorld(maps.get(mapIndex).worldName);
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
		// Kill all the exploring players
		if (explorationTime != 0) {
			for (UUID uuid : TeamController.getPlayers()) {
				Player player = Bukkit.getPlayer(uuid);
				if (player != null) {
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

				// Register cannons
				for (Cannon cannon : maps.get(mapIndex).cannons) {
					Main.plugin.getServer().getPluginManager().registerEvents(cannon, Main.plugin);
				}

				// Register cores and regions
				if (maps.get(mapIndex) instanceof CoreMap coreMap) {
                    for (Core core : coreMap.getCores()) {
						Main.plugin.getServer().getPluginManager().registerEvents(core, Main.plugin);

						if (core.region != null) {
							Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
											BukkitAdapter.adapt(Objects.requireNonNull(getWorld(maps.get(mapIndex).worldName)))))
									.addRegion(core.region);
						}
					}
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

				for (UUID uuid : TeamController.getPlayers()) {
					Player player = getPlayer(uuid);
					if (player == null) continue;
					Team team = TeamController.getTeam(uuid);
					player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_2, 1f, 1f);
					Messenger.send(Component.text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").color(team.primaryChatColor), player);
					Messenger.send(Component.text("~~~~~~~~~~~~~~~~~ FIGHT! ~~~~~~~~~~~~~~~~~~").color(team.primaryChatColor), player);
					Messenger.send(Component.text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").color(team.primaryChatColor), player);
				}
				//enable the bossbars
				Flag.registerBossbars();

				Scoreboard.clearScoreboard();
			}
		}.runTask(Main.plugin);

		if (timer != null) {
			timer.restartTimer(getCurrentMap().duration.getFirst(), getCurrentMap().duration.getSecond());
		}
	}

	/**
	 * Does any unloading needed for the map
	 * @param oldMap The map to unload
	 */
	public static void unloadMap(Map oldMap) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			// Clear map stats
			InCombat.clearCombat();

			// Clear capture zones
			for (Flag flag : oldMap.flags) {
				flag.clear();
			}

			// Unregister core listeners and regions
			if (maps.get(mapIndex) instanceof CoreMap coreMap) {
				for (Core core : coreMap.getCores()) {
					HandlerList.unregisterAll(core);

					if (core.region != null) {
						Bukkit.getScheduler().runTask(Main.plugin, () ->
								Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
												BukkitAdapter.adapt(Objects.requireNonNull(getWorld(oldMap.worldName)))))
										.removeRegion(core.name.replace(' ', '_')));
					}
				}
			}

		// Unregister catapult listeners
		for (Catapult catapult : oldMap.catapults) {
			HandlerList.unregisterAll(catapult);
		}

		// Unregister cannon listeners
		for (Cannon cannon : oldMap.cannons) {
			HandlerList.unregisterAll(cannon);
		}

		// Unregister flag regions
		for (Flag flag : oldMap.flags) {
			if (flag.region != null) {
				Bukkit.getScheduler().runTask(Main.plugin, () ->
						Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(
								BukkitAdapter.adapt(Objects.requireNonNull(getWorld(oldMap.worldName)))))
						.removeRegion(flag.getName().replace(' ', '_')));
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

		// Unregister woolmap listeners
		for (Team team : oldMap.teams) {
			HandlerList.unregisterAll(team.lobby.woolmap);
		}

		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			getServer().unloadWorld(oldMap.worldName, false);

			// Delete the world's files
			Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
				Main.plugin.getLogger().info("Deleting world " + oldMap.worldName);
				// Creating a File object for directory
				File directoryPath = new File(Bukkit.getWorldContainer(), oldMap.worldName);
                try {
                    FileUtils.deleteDirectory(directoryPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
			});
		});
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
		if (maps.isEmpty())
			return null;
		return maps.get(mapIndex);
	}

	/**
	 * @return true if the map is ongoing, false otherwise (i.e. exploration phase)
	 */
	public static boolean isOngoing() {
		return timer != null && timer.state == TimerState.ONGOING;
	}

	/**
	 * Checks if the map rotation has begun
	 * @return true if the map rotation has been started
	 */
	public static boolean hasStarted() {
		return hasStarted;
	}

	/**
	 * Displays the GUI allowing players to vote if they liked/disliked a map
	 */
	public static void beginVote() {
		if (TeamController.getPlayers().size() < 4) {
			Messenger.broadcastError("Not enough players for a fair vote. Map votes will not be recorded.");
			return;
		}

		for (UUID uuid : TeamController.getPlayers()) {
			Player p = getPlayer(uuid);
			if (p == null) continue;

			Gui gui = new Gui(Component.text("Did you enjoy the map?"), 1, true);

			gui.addItem(Component.text("Vote: Yes", NamedTextColor.DARK_GREEN), Material.EMERALD_BLOCK,
					Collections.singletonList(Component.text("Click here if you liked this map", NamedTextColor.DARK_GREEN)),
					3, "mapvote yes", true);
			gui.addItem(Component.text("Vote: No", NamedTextColor.RED), Material.REDSTONE_BLOCK,
					Collections.singletonList(Component.text("Click here if you didn't like this map", NamedTextColor.RED)),
					5, "mapvote no", true);

            gui.open(p);
		}
	}

	/**
	 * Completely removes a player from Castle Siege
	 * @param player The player to remove
	 */
	public static void removePlayer(Player player) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			TeamController.leaveTeam(player.getUniqueId());
			TeamController.leaveSpectator(player.getUniqueId());

			// Remove player from gameplay
			for (Flag flag : MapController.getCurrentMap().flags) {
				flag.playerExit(player);
			}
			for (Gate gate : MapController.getCurrentMap().gates) {
				Ram ram = gate.getRam();
				if (ram != null)
					ram.playerExit(player);
			}

			// Remove from events/commands
			BarCooldown.remove(player.getUniqueId());
			VoteSkipCommand.removePlayer(player.getUniqueId());
			Scoreboard.clearScoreboard(player);
			InCombat.playerDied(player.getUniqueId());
			Kit.equippedKits.remove(player.getUniqueId());

			try {
				// Save a player's data and reset their current data into PlayerData from CSPlayerData
				StoreData.store(player.getUniqueId(), CSActiveData.getData(player.getUniqueId()));
				PlayerData data = LoadData.load(player.getUniqueId());
				ActiveData.addPlayer(player.getUniqueId(), data);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
    }
}
