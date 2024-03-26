package me.huntifi.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.commands.gameplay.VoteSkipCommand;
import me.huntifi.castlesiege.commands.info.leaderboard.MVPCommand;
import me.huntifi.castlesiege.commands.staff.boosters.GrantBooster;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.data_types.Booster;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.data_types.CSStats;
import me.huntifi.castlesiege.data_types.CoinBooster;
import me.huntifi.castlesiege.data_types.KitBooster;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.gameplay.Explosion;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.castlesiege.kits.kits.free_kits.Swordsman;
import me.huntifi.castlesiege.maps.events.NextMapEvent;
import me.huntifi.castlesiege.maps.objects.Catapult;
import me.huntifi.castlesiege.maps.objects.Core;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.maps.objects.Gate;
import me.huntifi.castlesiege.maps.objects.Ram;
import me.huntifi.castlesiege.secrets.SecretItems;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.gui.Gui;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Bukkit.getWorld;

/**
 * Manages what map the game is currently on
 */
public class MapController {

	// Boosters - chances
	private static final double BASE_BOOSTER_CHANCE = 0.15;
	private static final double COIN_BOOSTER_CHANCE = 0.5;
	//private static final double KIT_BOOSTER_CHANCE = 0.5; // Not actually used, set for reference

	// Boosters - limits/sub-chances
	private static final int COIN_BOOSTER_MAX_TIME = 9000;
	private static final int COIN_BOOSTER_MIN_TIME = 1800;
	private static final double COIN_BOOSTER_GAUSSIAN_DIV = 2.75;
	private static final double COIN_BOOSTER_GAUSSIAN_ADD = 3;
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
			case DestroyTheCore:
				if (MapController.getCurrentMap() instanceof CoreMap) {
					CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
					// Check if the defenders have won
					for (Core core : coreMap.getCores()) {
						if (core.isDestroyed && core.getOwners().equalsIgnoreCase(getCurrentMap().teams[1].name)) {
							winners = getCurrentMap().teams[0].name;
						} else if (core.isDestroyed && core.getOwners().equalsIgnoreCase(getCurrentMap().teams[0].name)) {
							winners = getCurrentMap().teams[1].name;
						}
					}
				}
				break;
			case Control:
				Main.instance.getLogger().severe("Control game mode has not been implemented yet! It's a draw!");
				break;
			case Charge:
				// Check if the defenders have won
				for (Flag flag : getCurrentMap().flags) {
					if (Objects.equals(flag.getCurrentOwners(), getCurrentMap().teams[0].name)) {
                        winners = getCurrentMap().teams[0].name;
                    } else {
                        winners = getCurrentMap().teams[1].name;
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
					}

					else if (flagCounts.get(teamName).equals(flagCounts.get(currentWinners))) {
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
				if (team.name.equals(winners)) {
					Messenger.broadcast(Component.text("~~~~~~~~" + team.name + " has won!~~~~~~~~", team.primaryChatColor));
				} else {
					Messenger.broadcast(Component.text("~~~~~~~~" + team.name + " has lost!~~~~~~~~", team.primaryChatColor));
				}

				Messenger.broadcast(MVPCommand.getMVPMessage(team));

				if (team.name.equals(winners)) {
					giveCoinReward(team);
				}

			}
		// The map was a draw
		} else {
			for (Team team : getCurrentMap().teams) {
				Messenger.broadcast(Component.text("~~~~~~~~" + team.name + " has drawn!~~~~~~~~", team.primaryChatColor));

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

		// Begins the next map
		new BukkitRunnable() {
			@Override
			public void run() {
				nextMap();
			}
		}.runTaskLater(Main.plugin, 200);

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

			if (Bukkit.getOnlinePlayers().size() >= 6 && score >= 20) {
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
						do {
							kit = dKits.get(new Random().nextInt(dKits.size()));
						} while (Kit.getKit(kit) instanceof TeamKit);
					}
					booster = new KitBooster(duration, kit);
				}

				GrantBooster.updateDatabase(uuid, booster);
				data.addBooster(booster);
				Player player = getPlayer(uuid);
				if (player != null) {
					Messenger.broadcastSuccess(player.displayName().append(Component.text(" gained a " + booster.getName() + " for being MVP!")));
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

					Main.instance.getLogger().info("Completed map cycle! Restarting server...");
					getServer().spigot().restart();
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
			loadMap();
		}
	}

	/**
	 * Loads the current map into play
	 */
	public static void loadMap() {
		// Clear the scoreboard & reset stats
		Scoreboard.clearScoreboard();

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
				if (!SpectateCommand.spectators.contains(player.getUniqueId()) && !DuelCommand.isDueling(player))
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
		Main.plugin.getComponentLogger().info(Component.text("Spawning secret items if there are any.", NamedTextColor.DARK_GREEN));

		// Teleport Spectators
		for (UUID spectator : SpectateCommand.spectators) {
			Player player = getPlayer(spectator);
			if (player != null && player.isOnline()) {
				if (MapController.getCurrentMap() instanceof CoreMap) {
					CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
						player.teleport(coreMap.getCore(1).getSpawnPoint());

				} else {
					player.teleport(MapController.getCurrentMap().flags[0].getSpawnPoint());
				}
			}
		}

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

				// Register cores and regions
				if (maps.get(mapIndex) instanceof CoreMap) {
					CoreMap coreMap = (CoreMap) maps.get(mapIndex);
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

				for (UUID uuid : getPlayers()) {
					Player player = getPlayer(uuid);
					if (player == null) continue;
					Team team = TeamController.getTeam(uuid);
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
	 * Restocks the players kit, or sets them to swordsman if they were using a team kit
	 * @param player The player to restock/reset kits for
	 */
	private static void checkTeamKit(Player player) {
		Kit kit = Kit.equippedKits.get(player.getUniqueId());
		if (kit == null)
			return;

		if (kit instanceof TeamKit || kit instanceof MapKit) {
			Kit.equippedKits.put(player.getUniqueId(), new Swordsman());
			CSActiveData.getData(player.getUniqueId()).setKit("swordsman");
		}

		Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId(), true);
	}

	/**
	 * Does any unloading needed for the map
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

		// Unregister core listeners and regions
		if (maps.get(mapIndex) instanceof CoreMap) {
			CoreMap coreMap = (CoreMap) maps.get(mapIndex);
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
		Player player = getPlayer(uuid);
		assert player != null;

		team.addPlayer(uuid);

		player.teleport(team.lobby.spawnPoint);

		player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_2, 1f, 1f);

		Messenger.send(Component.text("You joined ").append(Component.text(team.name, team.primaryChatColor)), player);

		checkTeamKit(player);
	}

	/**
	 * Puts the players back in their spawnrooms.
	 * @param uuid the player to add to a team
	 */
	public static void rejoinAfterDuel(UUID uuid) {
		rejoinAfterDuels(uuid, getCurrentMap().smallestTeam());
	}

	/**
	 *
	 * @param uuid the player to sent to the spawnroom
	 * @param team their team
	 */
	private static void rejoinAfterDuels(UUID uuid, Team team) {
		Player player = getPlayer(uuid);
		assert player != null;

		player.teleport(team.lobby.spawnPoint);
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
	 * @return All the players currently playing the game
	 */
	public static List<UUID> getPlayers() {
		List<UUID> players = new ArrayList<>();
		for (Team t : getCurrentMap().teams) {
            players.addAll(t.getPlayers());
		}

		return players;
	}

	/**
	 * @return All the players playing the game and not in a lobby
	 */
	public static List<UUID> getActivePlayers() {
		List<UUID> players = new ArrayList<>();
		for (Team t : getCurrentMap().teams) {
			for (UUID uuid : t.getPlayers()) {
				if (!InCombat.isPlayerInLobby(uuid))
					players.add(uuid);
			}
		}

		return players;
	}

	/**
	 * @return All the players and spectators
	 */
	public static List<UUID> getEveryone() {
		List<UUID> players = new ArrayList<>();
		for (Team t : getCurrentMap().teams) {
			players.addAll(t.getPlayers());
		}
		players.addAll(SpectateCommand.spectators);

		return players;
	}

	/**
	 * Checks if a player is a spectator
	 * @param uuid The uuid of the player to check
     * @return If the player is a spectator
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

	public static void beginVote() {
		if (getPlayers().size() < 4) {
			Messenger.broadcastError("Not enough players for a fair vote. Map votes will not be recorded.");
			return;
		}

		for (UUID uuid : getPlayers()) {
			Player p = Bukkit.getPlayer(uuid);
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
}
