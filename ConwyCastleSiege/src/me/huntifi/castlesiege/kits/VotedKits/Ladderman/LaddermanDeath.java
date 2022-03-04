package me.huntifi.castlesiege.kits.VotedKits.Ladderman;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class LaddermanDeath implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {

			Player whoWasHit = e.getEntity();
			Player whoHit = e.getEntity().getKiller();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Ladderman")) {

				if (PlayerTeam.playerIsInTeam(whoHit, 1)) {

					DeathscoresAsync.doStats(whoHit, whoWasHit);

					whoWasHit.sendMessage("You were killed by " + NametagsEvent.colour(whoHit) + whoHit.getName());
					whoHit.sendMessage("You killed " + NametagsEvent.colour(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");



				}

				if (PlayerTeam.playerIsInTeam(whoHit, 2)) {

					DeathscoresAsync.doStats(whoHit, whoWasHit);

					whoWasHit.sendMessage("You were killed by " + NametagsEvent.colour(whoHit) + whoHit.getName());
					whoHit.sendMessage("You killed " + NametagsEvent.colour(whoWasHit) + whoWasHit.getName() + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");


				}

			}
		}

	}

}
