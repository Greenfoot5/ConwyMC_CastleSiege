package me.huntifi.castlesiege.kits.Ranger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.events.join.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class DeathRanger implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {

			Player whoWasHit = e.getEntity();
			Player whoHit = e.getEntity().getKiller();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Ranger")) {

				//if (PlayerTeam.playerIsInTeam(whoHit, 1)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("You were turned into a porcupine by " + NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You turned " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " into a porcupine" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("You were turned into a porcupine by " + NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You turned " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " into a porcupine" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");
					}

				//}

				//if (PlayerTeam.playerIsInTeam(whoHit, 2)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);
						
						whoWasHit.sendMessage("You were turned into a porcupine by " + NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You turned " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " into a porcupine" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("You were turned into a porcupine by " + NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You turned " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " into a porcupine" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");


					}

				//}

			}

		}

	}

}
