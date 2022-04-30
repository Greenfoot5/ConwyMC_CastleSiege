package me.huntifi.castlesiege.maps;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.objects.Door;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.Swordsman;
import me.huntifi.castlesiege.stats.levels.LevelingEvent;
import me.huntifi.castlesiege.tags.NametagsEvent;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.*;

/**
 * Manages what map the game is currently on
 */
public class MapController implements CommandExecutor {

	public static MapsList currentMap = MapsList.HelmsDeep;
	public static Map[] maps;
	public static int mapIndex = 0;
	public static Timer timer;

	/**
	 * Begins the map loop
	 */
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
	 * Handles ending the map
	 * (called by the timer or if all flags are captured)
	 */
	public static void endMap() {
		timer.hasGameEnded = true;

		// Calculate the winner based on the gamemode
		String winners;
		switch(getCurrentMap().gamemode) {
			case Control:
				getLogger().severe("Control gamemode has not been implemented yet! Defaulting to teams[0] as winners");
				winners = getCurrentMap().teams[0].name;
				break;
			case Assault:
				// Check if the defenders have won
				for (Flag flag : getCurrentMap().flags) {
					if (Objects.equals(flag.currentOwners, getCurrentMap().teams[0].name)) {
						winners = getCurrentMap().teams[0].name;
						break;
					}
				}
			default:
				// Get a count of who owns which flag
				java.util.Map<String, Integer> flagCounts = new HashMap<>();
				for (Flag flag : getCurrentMap().flags) {
					flagCounts.merge(flag.currentOwners, 1, Integer::sum);
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

		// Kills all the players
		// Moves all players to the lobby
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (getCurrentMap().getTeam(player.getUniqueId()) != null) {
						player.setHealth(0);
					}
				}
				InCombat.clearCombat();
			}
		}.runTask(Main.plugin);

		// Broadcast the winners
		for (Team team : getCurrentMap().teams) {
			Bukkit.broadcastMessage("");
			if (team.name.equals(winners)) {
				Bukkit.broadcastMessage(team.primaryChatColor + "~~~~~~~~" + team.name + " has won!~~~~~~~~");
		    } else {
				Bukkit.broadcastMessage(team.primaryChatColor + "~~~~~~~~" + team.name + " has lost!~~~~~~~~");
			}
			Bukkit.broadcastMessage(team.primaryChatColor + team.name + ChatColor.DARK_AQUA + " MVP: " + ChatColor.WHITE + "Coming Soon!");
			//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(RohanMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId())));
			//Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId())));
		}

		// Begins the
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
					//MainStats.updateStats(p.getUniqueId(), p);
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
		// Clear the scoreboard
		Scoreboard.clearScoreboard();

		// Register the flag regions
		for (Flag flag : maps[mapIndex].flags) {
			if (flag.region != null) {
				Objects.requireNonNull(WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(getWorld(currentMap.name()))))).addRegion(flag.region);
			}
		}

		// Register doors
		for (Door door : maps[mapIndex].doors) {
			Main.plugin.getServer().getPluginManager().registerEvents(door, Main.plugin);
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
	 * @return If all the flags belong to the same team
	 */
	public static Boolean hasMapEnded() {
		if (timer.hasGameEnded) {
			return true;
		}

		String startingTeam = getCurrentMap().flags[0].currentOwners;
		for (Flag flag : getCurrentMap().flags) {
			if (!startingTeam.equalsIgnoreCase(flag.currentOwners)) {
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
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (commandSender instanceof ConsoleCommandSender || (commandSender instanceof Player
				&& "AdminDeveloperModerator".contains((ActiveData.getData(((Player) commandSender).getUniqueId()).getStaffRank()))))
		{
			commandSender.sendMessage(ChatColor.DARK_AQUA + "Saving world: " + getCurrentMap().worldName);

			Objects.requireNonNull(getWorld(getCurrentMap().worldName)).save();
			copyFileStructure(new File(Bukkit.getWorldContainer(), getCurrentMap().worldName + "_save"), Objects.requireNonNull(getWorld(getCurrentMap().worldName)).getWorldFolder());

			commandSender.sendMessage(ChatColor.GREEN + "Saved " + getCurrentMap().worldName + "!");
		} else {
			commandSender.sendMessage(ChatColor.DARK_RED + "You don't have permission to do that!");
		}
		return true;
	}
}
