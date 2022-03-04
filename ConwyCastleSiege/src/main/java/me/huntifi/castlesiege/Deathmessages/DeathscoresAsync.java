package me.huntifi.castlesiege.Deathmessages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.stats.MVP.MVPstats;

public class DeathscoresAsync {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");


	public static void doStats(Player whoHit, Player whoWasHit) {

				MVPstats.addKills(whoHit.getUniqueId(), 1.0);
				MVPstats.addAssists(whoHit.getUniqueId(), 1.0);
				MVPstats.addKillstreak(whoHit.getUniqueId(), 1);
				MVPstats.setKillstreak(whoWasHit.getUniqueId(), 0);
				
				if (StatsChanging.getKillstreak(whoWasHit.getUniqueId()) < MVPstats.getKillstreak(whoWasHit.getUniqueId())) {
					StatsChanging.setKillstreak(whoWasHit.getUniqueId(), MVPstats.getKillstreak(whoWasHit.getUniqueId()));
				}

	}


	public static int returnKillstreak(Player whoHit) {

		return MVPstats.getKillstreak(whoHit.getUniqueId());

	}

}
