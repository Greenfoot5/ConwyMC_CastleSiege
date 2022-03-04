package me.huntifi.castlesiege.mvpCommand;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLstats;
import me.huntifi.castlesiege.Helmsdeep.HelmsdeepEndMVP;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class HelmsdeepMVP {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@SuppressWarnings("unused")
	public static void returnMVP(final int team, final Player p) {
		
		final UUID uuid = p.getUniqueId();

			final Player UrukhaiMVP = HelmsdeepEndMVP.returnUrukhaiMVP();

			final Player RohanMVP = HelmsdeepEndMVP.returnRohanMVP();
			
			DecimalFormat currencyFormat = new DecimalFormat("0.00");
			DecimalFormat NumberFormat = new DecimalFormat("0");

			if (currentMaps.currentMapIs("HelmsDeep")) {

				String score = currencyFormat.format(MVPstats.returnMainMvpScore(p));
				String kills = NumberFormat.format(MVPstats.getKills(uuid));
				String deaths = NumberFormat.format(MVPstats.getDeaths(uuid));
				String assists = NumberFormat.format(MVPstats.getAssists(uuid));
				String captures = NumberFormat.format(MVPstats.getCaptures(uuid));
				String heals = NumberFormat.format(MVPstats.getHeals(uuid));
				String supports = NumberFormat.format(MVPstats.getSupports(uuid));
				String playerKDR = currencyFormat.format((MVPstats.getKills(uuid) / MVPstats.getDeaths(uuid)));
				
					if (team == 1 && PlayerTeam.playerIsInTeam(p, team)) {

						HelmsdeepEndMVP.Urukhai.put(p, MVPstats.getScore(p.getUniqueId())); 

						String MVPscore = currencyFormat.format(MVPstats.returnMainMvpScore(UrukhaiMVP));
						String MVPkills = NumberFormat.format(MVPstats.getKills(UrukhaiMVP.getUniqueId()));
						String MVPdeaths = NumberFormat.format(MVPstats.getDeaths(UrukhaiMVP.getUniqueId()));
						String MVPassists = NumberFormat.format(MVPstats.getAssists(UrukhaiMVP.getUniqueId()));
						String MVPcaptures = NumberFormat.format(MVPstats.getCaptures(UrukhaiMVP.getUniqueId()));
						String MVPheals = NumberFormat.format(MVPstats.getHeals(UrukhaiMVP.getUniqueId()));
						String MVPsupports = NumberFormat.format(MVPstats.getSupports(UrukhaiMVP.getUniqueId()));
						String KDR = currencyFormat.format((MVPstats.getKills(UrukhaiMVP.getUniqueId()) / MVPstats.getDeaths(UrukhaiMVP.getUniqueId())));
						

							if (UrukhaiMVP != null) {

								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + UrukhaiMVP.getName()); 
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + MVPscore + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + MVPkills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + MVPdeaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + KDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + MVPassists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + MVPheals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + MVPcaptures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + MVPsupports); 
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_AQUA + "You:");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + score + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + kills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + deaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + playerKDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + assists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + heals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + captures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + supports); 
								p.sendMessage("");

							} else {

								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_GRAY + "Uruk-hai " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "none");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + 0.0 + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_AQUA + "You:");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + score + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + kills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + deaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + playerKDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + assists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + heals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + captures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + supports); 
								p.sendMessage("");

							}
				}

				if (team == 2 && PlayerTeam.playerIsInTeam(p, team)) {

						HelmsdeepEndMVP.Rohan.put(p, MVPstats.getScore(p.getUniqueId()));

						String MVPscore = currencyFormat.format(MVPstats.returnMainMvpScore(RohanMVP));
						String MVPkills = NumberFormat.format(MVPstats.getKills(RohanMVP.getUniqueId()));
						String MVPdeaths = NumberFormat.format(MVPstats.getDeaths(RohanMVP.getUniqueId()));
						String MVPassists = NumberFormat.format(MVPstats.getAssists(RohanMVP.getUniqueId()));
						String MVPcaptures = NumberFormat.format(MVPstats.getCaptures(RohanMVP.getUniqueId()));
						String MVPheals = NumberFormat.format(MVPstats.getHeals(RohanMVP.getUniqueId()));
						String MVPsupports = NumberFormat.format(MVPstats.getSupports(RohanMVP.getUniqueId()));
						String KDR = currencyFormat.format((MVPstats.getKills(RohanMVP.getUniqueId()) / MVPstats.getDeaths(RohanMVP.getUniqueId())));

							if (RohanMVP != null) {

								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + RohanMVP.getName()); 
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + MVPscore + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + MVPkills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + MVPdeaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + KDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + MVPassists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + MVPheals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + MVPcaptures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + MVPsupports); 
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_AQUA + "You:");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + score + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + kills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + deaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + playerKDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + assists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + heals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + captures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + supports); 
								p.sendMessage("");

							} else {

								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_GREEN + "Rohan " + ChatColor.DARK_AQUA + "MVP: " + ChatColor.WHITE + "none");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + 0.0 + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + "0");
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + "0" + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + "0");
								p.sendMessage("");
								p.sendMessage(ChatColor.DARK_AQUA + "You:");
								p.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + score + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + kills + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + deaths + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + playerKDR + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + assists); 
								p.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + heals + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + captures + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + supports); 
								p.sendMessage("");


							}
				}

			}


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

		double kills = SQLstats.getKills(uuid);
		double heals = SQLstats.getHeals(uuid);
		double deaths = SQLstats.getDeaths(uuid);
		double assists = SQLstats.getAssists(uuid);
		double supports = SQLstats.getSupports(uuid);
		double captures = SQLstats.getCaptures(uuid);

		double totalScore = (kills + assists + captures + (heals/2) + (supports/6) - deaths);

		return totalScore;


	}

}
