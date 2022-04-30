package me.huntifi.castlesiege.stats;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.database.SQLGetter;
import me.huntifi.castlesiege.database.SQLStats;
import me.huntifi.castlesiege.events.join.stats.StatsChanging;
import me.huntifi.castlesiege.events.join.stats.StatsLoading;
import me.huntifi.castlesiege.tags.NametagsEvent;

public class StatsEverything {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static void LoadAllStatsIntoLists(Player p) {

		UUID uuid = p.getUniqueId();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			StatsLoading.PlayerKills.put(uuid, SQLStats.getKills(uuid));
			StatsLoading.PlayerDeaths.put(uuid, SQLStats.getDeaths(uuid));
			StatsLoading.PlayerCaptures.put(uuid, SQLStats.getCaptures(uuid));
			StatsLoading.PlayerAssists.put(uuid, SQLStats.getAssists(uuid));
			StatsLoading.PlayerHeals.put(uuid, SQLStats.getHeals(uuid));
			StatsLoading.PlayerSupports.put(uuid, SQLStats.getSupports(uuid));
			StatsLoading.PlayerKillstreak.put(uuid, SQLStats.getKillstreak(uuid));
			StatsLoading.PlayerSecrets.put(uuid, SQLStats.getSecrets(uuid));
			StatsLoading.PlayerKit.put(uuid, SQLStats.getKit(uuid));
			StatsLoading.PlayerLevel.put(uuid, SQLStats.getLevel(uuid));
			StatsLoading.PlayerRank.put(uuid, SQLGetter.getRank(uuid));
			StatsLoading.PlayerStaffRank.put(uuid, SQLGetter.getStaffRank(uuid));
			

			Bukkit.getScheduler().runTask(plugin, () -> {

				StatsLoading.PlayerScore.put(uuid, calculateScore(p));
				NametagsEvent.GiveNametag(p);

			});

		});


	}
	
	public static double calculateScore(Player p) {
		
		DecimalFormat currencyFormat = new DecimalFormat("0.0");
		
		UUID uuid = p.getUniqueId();
		
		double kills = StatsChanging.getKills(uuid);
		double heals = StatsChanging.getHeals(uuid);
		double deaths = StatsChanging.getDeaths(uuid);
		double assists = StatsChanging.getAssists(uuid);
		double supports = StatsChanging.getSupports(uuid);
		double captures = StatsChanging.getCaptures(uuid);
		
		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);
		currencyFormat.format(totalScore);
		
		return totalScore;
		
	}
	
	public static double calculateMvpScore(Player p) {
		
		UUID uuid = p.getUniqueId();
		
		/*double kills = MVPstats.getKills(uuid);
		double heals = MVPstats.getHeals(uuid);
		double deaths = MVPstats.getDeaths(uuid);
		double assists = MVPstats.getAssists(uuid);
		double supports = MVPstats.getSupports(uuid);
		double captures = MVPstats.getCaptures(uuid);
		
		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);
		
		return totalScore;*/
		return 0;
		
	}

	public static void LoadAllMvpStatsIntoDatabase(Player p) {
		
		UUID uuid = p.getUniqueId();

		/*Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			
			SQLStats.addKills(uuid, MVPstats.getKills(uuid));
			SQLStats.addCaptures(uuid, MVPstats.getCaptures(uuid));
			SQLStats.addAssists(uuid, MVPstats.getAssists(uuid));
			SQLStats.addDeaths(uuid, MVPstats.getDeaths(uuid));
			SQLStats.addHeals(uuid, MVPstats.getHeals(uuid));
			SQLStats.addSupports(uuid, MVPstats.getSupports(uuid));
			SQLStats.addSupports(uuid, MVPstats.getSupports(uuid));
			SQLStats.setScore(uuid, calculateScore(p));
			
			Bukkit.getScheduler().runTask(plugin, () -> {
				
				MVPstats.setAssists(uuid, 0.0);
				MVPstats.setKills(uuid, 0.0);
				MVPstats.setCaptures(uuid, 0.0);
				MVPstats.setScore(uuid, 0.0);
				MVPstats.setDeaths(uuid, 0.0);
				MVPstats.setHeals(uuid, 0.0);
				MVPstats.setSupports(uuid, 0.0);
				
			});
			
		});*/

	}

	public static void ClearMainStatsLists(UUID uuid) {

		StatsLoading.PlayerScore.remove(uuid, StatsLoading.PlayerScore.remove(uuid));
		StatsLoading.PlayerKills.remove(uuid, StatsLoading.PlayerKills.remove(uuid));
		StatsLoading.PlayerDeaths.remove(uuid, StatsLoading.PlayerDeaths.remove(uuid));
		StatsLoading.PlayerCaptures.remove(uuid, StatsLoading.PlayerCaptures.remove(uuid));
		StatsLoading.PlayerAssists.remove(uuid, StatsLoading.PlayerAssists.remove(uuid));
		StatsLoading.PlayerHeals.remove(uuid, StatsLoading.PlayerHeals.remove(uuid));
		StatsLoading.PlayerSupports.remove(uuid, StatsLoading.PlayerSupports.remove(uuid));
		StatsLoading.PlayerKillstreak.remove(uuid, StatsLoading.PlayerKillstreak.remove(uuid));
		StatsLoading.PlayerSecrets.remove(uuid, StatsLoading.PlayerSecrets.remove(uuid));
		StatsLoading.PlayerKit.remove(uuid, StatsLoading.PlayerKit.remove(uuid));
		StatsLoading.PlayerRank.remove(uuid, StatsLoading.PlayerRank.remove(uuid));
		StatsLoading.PlayerStaffRank.remove(uuid, StatsLoading.PlayerStaffRank.remove(uuid));

	}

	public static void UpdateMainListsWithMvpStats(UUID uuid) {

		/*StatsChanging.addScore(uuid, MVPstats.getScore(uuid));
		StatsChanging.addKills(uuid, MVPstats.getKills(uuid));
		StatsChanging.addCaptures(uuid, MVPstats.getCaptures(uuid));
		StatsChanging.addAssists(uuid, MVPstats.getAssists(uuid));
		StatsChanging.addDeaths(uuid, MVPstats.getDeaths(uuid));
		StatsChanging.addHeals(uuid, MVPstats.getHeals(uuid));
		StatsChanging.addSupports(uuid, MVPstats.getSupports(uuid));*/

	}
	
	public static void updateScore(UUID uuid, Player p) {
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
		
		SQLStats.setScore(uuid, StatsUpdater.returnMainScore(p));
		
		});
		
	}

}
