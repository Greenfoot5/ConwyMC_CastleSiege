package me.huntifi.castlesiege.Thunderstone;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.stats.MVP.MVPstats;

public class ThunderstoneEndMVP implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static HashMap<Player, Double> ThunderstoneGuards = new HashMap<Player, Double>();


	public static HashMap<Player, Double> Cloudcrawlers = new HashMap<Player, Double>();

	static DecimalFormat currencyFormat = new DecimalFormat("0.00");
	static DecimalFormat NumberFormat = new DecimalFormat("0");

	public static Player returnThunderstoneGuardsMVP() {

		Double highest1 = 0.0;
		Player highestStr1 = null;


		for (Entry<Player, Double> entry : ThunderstoneGuards.entrySet()) {

			if (entry != null) {
				if (entry.getValue() >= highest1) {

					highest1 = entry.getValue();
					highestStr1 = entry.getKey();
					return highestStr1;
				}


			}
		}


		return null;
	}

	public static Player returnCloudcrawlersMVP() {

		Double highest1 = 0.0;
		Player highestStr1 = null;


		for (Entry<Player, Double> entry : Cloudcrawlers.entrySet()) {

			if (entry != null) {
				if (entry.getValue() >= highest1) {

					highest1 = entry.getValue();
					highestStr1 = entry.getKey();
					return highestStr1;

				} 
			}
		}
		return null;
	}



	@EventHandler
	public void onUserQuit(PlayerQuitEvent e){

			final Player p = e.getPlayer();

			ThunderstoneGuards.remove(p, MVPstats.getScore(p.getUniqueId()));
			Cloudcrawlers.remove(p, MVPstats.getScore(p.getUniqueId()));

	}


	public static void CloudcrawlersWin() {

		Player UrukhaiMVP = returnCloudcrawlersMVP();

		Player RohanMVP = returnThunderstoneGuardsMVP();

		if (UrukhaiMVP == null) {
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "~~~~~~~~" + ChatColor.DARK_AQUA + "The Cloudcrawlers have won!" + ChatColor.DARK_AQUA + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cloudcrawlers " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");

		} else {

			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "~~~~~~~~" + ChatColor.DARK_AQUA + "The Cloudcrawlers have won!" + ChatColor.DARK_AQUA + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cloudcrawlers " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + UrukhaiMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(UrukhaiMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(UrukhaiMVP.getUniqueId()) / MVPstats.getDeaths(UrukhaiMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(UrukhaiMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(UrukhaiMVP.getUniqueId())));

		}

		if (RohanMVP == null) {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.GOLD + "~~~~~~~~" + ChatColor.GOLD + "The Thunderstone Guards have lost!" + ChatColor.GOLD + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.GOLD + "Thunderstone Guards " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
		} else {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.GOLD + "~~~~~~~~" + ChatColor.GOLD + "The Thunderstone Guards have lost!" + ChatColor.GOLD + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.GOLD + "Thunderstone Guards " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + RohanMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(RohanMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId())));
		}

		Cloudcrawlers.clear();
		ThunderstoneGuards.clear();

	}


	public static void ThunderstoneGuardsWin() {

		Player UrukhaiMVP = returnCloudcrawlersMVP();

		Player RohanMVP = returnThunderstoneGuardsMVP();

		if (RohanMVP == null) {

			Bukkit.broadcastMessage(ChatColor.GOLD + "~~~~~~~~" + ChatColor.GOLD + "The Thunderstone Guards has won!" + ChatColor.GOLD + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.GOLD + "Thunderstone Guards " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
		} else {

			Bukkit.broadcastMessage(ChatColor.GOLD + "~~~~~~~~" + ChatColor.GOLD + "The Thunderstone Guards has won!" + ChatColor.GOLD + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.GOLD + "Thunderstone Guards " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + RohanMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(RohanMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId())));


		}

		if (UrukhaiMVP == null) {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "~~~~~~~~" + ChatColor.DARK_AQUA + "The Cloudcrawlers have lost!" + ChatColor.DARK_AQUA + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cloudcrawlers " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");

		} else {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "~~~~~~~~" + ChatColor.DARK_AQUA + "The Cloudcrawlers have lost!" + ChatColor.DARK_AQUA + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Cloudcrawlers " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + UrukhaiMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(UrukhaiMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(UrukhaiMVP.getUniqueId()) / MVPstats.getDeaths(UrukhaiMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(UrukhaiMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(UrukhaiMVP.getUniqueId())));


		}

		Cloudcrawlers.clear();
		ThunderstoneGuards.clear();

	}


	public static double returnMVPScore (Player p) {

		UUID uuid = p.getUniqueId();

		double kills = MVPstats.getKills(uuid);
		double heals = MVPstats.getHeals(uuid);
		double deaths = MVPstats.getDeaths(uuid);
		double assists = MVPstats.getAssists(uuid);
		double supports = MVPstats.getSupports(uuid);
		double captures = MVPstats.getCaptures(uuid);

		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);

		return totalScore;

	}


}