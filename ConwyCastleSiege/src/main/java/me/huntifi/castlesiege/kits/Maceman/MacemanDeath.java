package me.huntifi.castlesiege.kits.Maceman;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.huntifi.castlesiege.Deathmessages.DeathscoresAsync;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class MacemanDeath implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {

			Player whoWasHit = e.getEntity();
			Player whoHit = e.getEntity().getKiller();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Maceman")) {

				if (PlayerTeam.playerIsInTeam(whoHit, 1)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your skull was cracked by " +NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + "'s mace");
						whoHit.sendMessage("You cracked " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + "'s skull" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your skull was cracked by " +NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + "'s mace");
						whoHit.sendMessage("You cracked " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + "'s skull" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					}

				}

				if (PlayerTeam.playerIsInTeam(whoHit, 2)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your skull was cracked by " +NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + "'s mace");
						whoHit.sendMessage("You cracked " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + "'s skull" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your skull was cracked by " +NametagsEvent.color(whoHit) + whoHit.getName() + ChatColor.RESET + "'s mace");
						whoHit.sendMessage("You cracked " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + "'s skull" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					}

				}

			}

		}

	}

}
