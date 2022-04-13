package me.huntifi.castlesiege.kits.Warhound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class WarhoundDeath implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){

		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {

			Player whoWasHit = e.getEntity();
			Player whoHit = e.getEntity().getKiller();

			/*if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Warhound")) {

				if (PlayerTeam.playerIsInTeam(whoHit, 1)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your were bitten to death by " +NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You bit " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " to death" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your were bitten to death by " +NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You bit " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " to death" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					}

				}

				if (PlayerTeam.playerIsInTeam(whoHit, 2)) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your were bitten to death by " +NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You bit " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " to death" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					} else {

						DeathscoresAsync.doStats(whoHit, whoWasHit);

						whoWasHit.sendMessage("Your were bitten to death by " +NametagsEvent.color(whoHit) + whoHit.getName());
						whoHit.sendMessage("You bit " + NametagsEvent.color(whoWasHit) + whoWasHit.getName() + ChatColor.RESET + " to death" + ChatColor.GRAY + " (" + DeathscoresAsync.returnKillstreak(whoHit) + ")");

					}

				}

			}*/

		}

	}

}
