package me.huntifi.castlesiege.joinevents.stats;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.stats.StatsEverything;

public class StatsLoading implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	/*/
	 One of the most important stats classes of this entire plugin. Once the player joins all their current castlesiege stats 
	 will be put into hashmaps together with the UUID. That way the server doesn't have to communicate with the database all the time.
	 Resulting in better server performance.
    /*/

	public static HashMap<UUID, Double> PlayerScore = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerKills = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerDeaths = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerCaptures = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerAssists = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerHeals = new HashMap<UUID, Double>();
	public static HashMap<UUID, Double> PlayerSupports = new HashMap<UUID, Double>();
	public static HashMap<UUID, Integer> PlayerKillstreak = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> PlayerSecrets = new HashMap<UUID, Integer>();
	public static HashMap<UUID, String> PlayerKit = new HashMap<UUID, String>();
	public static HashMap<UUID, Integer> PlayerLevel = new HashMap<UUID, Integer>();
	public static HashMap<UUID, String> PlayerRank = new HashMap<UUID, String>();
	public static HashMap<UUID, String> PlayerStaffRank = new HashMap<UUID, String>();

	@EventHandler
	public void LoadStatsOnJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		StatsEverything.LoadAllStatsIntoLists(p);

	}

}
