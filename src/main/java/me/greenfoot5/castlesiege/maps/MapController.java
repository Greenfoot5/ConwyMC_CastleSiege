package me.greenfoot5.castlesiege.maps;

import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.advancements.TutorialAdvancements;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplay;
import me.greenfoot5.castlesiege.commands.donator.DuelCommand;
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
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.castlesiege.kits.kits.free_kits.Swordsman;
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
import org.bukkit.GameMode;
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

	private static final ArrayList<UUID> spectators = new ArrayList<>();

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
							TutorialAdvancements.tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Simply the best"))).grant(player);
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

		// Begins the next map
		new BukkitRunnable() {
			@Override
			public void run() {
				nextMap();
			}
		}.runTaskLater(Main.plugin, 200);

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
			return (String) flagCounts.keySet().toArray()[0];
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

			if (Bukkit.getOnlinePlayers().size() >= 6 && score >= 20) {
				CSActiveData.getData(p.getUniqueId()).addCoins(50 * CSPlayerData.getCoinMultiplier());
				Messenger.sendSuccess("<gold>+" + (50 * CSPlayerData.getCoinMultiplier()) + "</gold> coins for winning!", p);
				new BukkitRunnable() {
					@Override
					public void run() {
						TutorialAdvancements.tab.getAdvancement(new AdvancementKey("siege_tutorial", NodeDisplay.cleanKey("Winner Winner Chicken Dinner"))).grant(p);
					}
				}.runTask(Main.plugin);
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
				if (!isSpectator(player.getUniqueId()) && !DuelCommand.isDueling(player.getUniqueId()))
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
		for (UUID spectator : spectators) {
			Player player = getPlayer(spectator);
			if (player != null && player.isOnline()) {
				if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
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

				for (UUID uuid : getPlayers()) {
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
	 * Restocks the players kit, or sets them to swordsman if they were using a team kit
	 * @param player The player to restock/reset kits for
	 */
	private static void checkTeamKit(Player player) {
		Kit kit = Kit.equippedKits.get(player.getUniqueId());
		if (kit == null)
			return;

		if (kit instanceof SignKit) {
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

		// Unregister woolmap listeners and clear teams
		for (Team team : oldMap.teams) {
			HandlerList.unregisterAll(team.lobby.woolmap);
			team.clear();
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
		Messenger.send(Component.text("You joined ").append(team.getDisplayName()), player);

		checkTeamKit(player);
	}

	/**
	 * Puts the players back in their spawnrooms.
	 * @param uuid the player to add to a team
	 */
	public static void rejoinAfterDuel(UUID uuid) {
		rejoinAfterDuel(uuid, getCurrentMap().smallestTeam());
	}

	/**
	 *
	 * @param uuid the player to sent to the spawnroom
	 * @param team their team
	 */
	private static void rejoinAfterDuel(UUID uuid, Team team) {
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
			spectators.remove(uuid);
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
        List<UUID> players = getPlayers();
		players.addAll(spectators);

		return players;
	}

	/**
	 * Checks if a player is a spectator
	 * @param uuid The uuid of the player to check
     * @return If the player is a spectator
	 */
	public static boolean isSpectator(UUID uuid) {
		return spectators.contains(uuid);
	}

	/**
	 * @return true if the map is ongoing, false otherwise (i.e. exploration phase)
	 */
	public static boolean isOngoing() {
		return timer.state == TimerState.ONGOING;
	}

	/**
	 * Displays the GUI allowing players to vote if they liked/disliked a map
	 */
	public static void beginVote() {
		if (getPlayers().size() < 4) {
			Messenger.broadcastError("Not enough players for a fair vote. Map votes will not be recorded.");
			return;
		}

		for (UUID uuid : getPlayers()) {
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
		Team team = TeamController.getTeam(player.getUniqueId());
		if (team != null)
			team.removePlayer(player.getUniqueId());

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

	public static void addSpectator(Player player) {
		spectators.add(player.getUniqueId());
		player.setGameMode(GameMode.SPECTATOR);
		removePlayer(player);
	}

	public static void removeSpectator(Player player) {
		MapController.joinATeam(player.getUniqueId());
		player.setGameMode(GameMode.SURVIVAL);
		InCombat.playerDied(player.getUniqueId());
		Scoreboard.clearScoreboard(player);
	}
}
