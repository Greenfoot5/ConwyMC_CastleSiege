package me.huntifi.castlesiege.Helmsdeep;

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

import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.stats.MVP.MVPstats;

public class HelmsdeepEndMVP implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static HashMap<Player, Double> Rohan = new HashMap<Player, Double>();


	public static HashMap<Player, Double> Urukhai = new HashMap<Player, Double>();

	static DecimalFormat currencyFormat = new DecimalFormat("0.00");
	static DecimalFormat NumberFormat = new DecimalFormat("0");

	public static Player returnRohanMVP() {

		Double highest1 = 0.0;
		Player highestStr1 = null;


		for (Entry<Player, Double> entry : Rohan.entrySet()) {

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

	public static Player returnUrukhaiMVP() {

		Double highest1 = 0.0;
		Player highestStr1 = null;


		for (Entry<Player, Double> entry : Urukhai.entrySet()) {

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

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			final Player p = e.getPlayer();

			Rohan.remove(p, MVPstats.getScore(p.getUniqueId()));
			Urukhai.remove(p, MVPstats.getScore(p.getUniqueId()));

		});
	}


	public static void UrukhaiWin() {

		Player UrukhaiMVP = returnUrukhaiMVP();

		Player RohanMVP = returnRohanMVP();

		if (UrukhaiMVP == null) {
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "~~~~~~~~" + ChatColor.DARK_GRAY + "The Uruk-hai have won!" + ChatColor.DARK_GRAY + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");

		} else {

			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "~~~~~~~~" + ChatColor.DARK_GRAY + "The Uruk-hai have won!" + ChatColor.DARK_GRAY + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + UrukhaiMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(UrukhaiMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(UrukhaiMVP.getUniqueId()) / MVPstats.getDeaths(UrukhaiMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(UrukhaiMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(UrukhaiMVP.getUniqueId())));

		}

		if (RohanMVP == null) {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "~~~~~~~~" + ChatColor.DARK_GREEN + "Rohan has lost!" + ChatColor.DARK_GREEN + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
		} else {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "~~~~~~~~" + ChatColor.DARK_GREEN + "Rohan has lost!" + ChatColor.DARK_GREEN + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + RohanMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(RohanMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + NumberFormat.format(MVPstats.getCaptures(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId())));
		}

		Urukhai.clear();
		Rohan.clear();
	}


	public static void RohanWin() {

		Player UrukhaiMVP = returnUrukhaiMVP();

		Player RohanMVP = returnRohanMVP();

		if (RohanMVP == null) {

			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "~~~~~~~~" + ChatColor.DARK_GREEN + "Rohan has won!" + ChatColor.DARK_GREEN + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
		} else {

			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "~~~~~~~~" + ChatColor.DARK_GREEN + "Rohan has won!" + ChatColor.DARK_GREEN + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + RohanMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(RohanMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + MVPstats.getDeaths(RohanMVP.getUniqueId()) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + MVPstats.getCaptures(RohanMVP.getUniqueId()) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId())));


		}

		if (UrukhaiMVP == null) {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "~~~~~~~~" + ChatColor.DARK_GRAY + "The Uruk-hai have lost!" + ChatColor.DARK_GRAY + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "None");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + "NaN" + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");

		} else {

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "~~~~~~~~" + ChatColor.DARK_GRAY + "The Uruk-hai have lost!" + ChatColor.DARK_GRAY + "~~~~~~~~");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + UrukhaiMVP.getName());
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + currencyFormat.format(returnMVPScore(UrukhaiMVP)) + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + NumberFormat.format(MVPstats.getKills(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + NumberFormat.format(MVPstats.getDeaths(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + currencyFormat.format((MVPstats.getKills(UrukhaiMVP.getUniqueId()) / MVPstats.getDeaths(UrukhaiMVP.getUniqueId()))) + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + NumberFormat.format(MVPstats.getAssists(UrukhaiMVP.getUniqueId())));
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + NumberFormat.format(MVPstats.getHeals(UrukhaiMVP.getUniqueId())) + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + MVPstats.getCaptures(UrukhaiMVP.getUniqueId()) + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + NumberFormat.format(MVPstats.getSupports(UrukhaiMVP.getUniqueId())));


		}

		Urukhai.clear();
		Rohan.clear();
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

	public static double returnMainScore (Player p) {

		UUID uuid = p.getUniqueId();

		double kills = SQLStats.getKills(uuid);
		double heals = SQLStats.getHeals(uuid);
		double deaths = SQLStats.getDeaths(uuid);
		double assists = SQLStats.getAssists(uuid);
		double supports = SQLStats.getSupports(uuid);
		double captures = SQLStats.getCaptures(uuid);

		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);

		return totalScore;


	}


}
