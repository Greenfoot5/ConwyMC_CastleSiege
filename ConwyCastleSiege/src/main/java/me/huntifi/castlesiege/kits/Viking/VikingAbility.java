package me.huntifi.castlesiege.kits.Viking;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.huntifi.castlesiege.events.join.stats.StatsChanging;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class VikingAbility implements Listener {

	@EventHandler
	public void VikingAbility1(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Viking")) {

				if (true) {//PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {

					double armor = whoWasHit.getAttribute(Attribute.GENERIC_ARMOR).getValue();

					if (whoWasHit.getHealth() >= 5 && whoWasHit.isDead() != true) {

						if (whoWasHit.getHealth() - ((0.08 * armor) * 19.5) <= 15.5) {

							ed.setCancelled(true);
							whoWasHit.setHealth(0);


						} else if (!(whoWasHit.getHealth() - ((0.08 * armor) * 19.5) <= 15.5)) {
							
							whoWasHit.damage((0.08 * armor) * 19.5);
							
						}

					}

				} else {


					ed.setCancelled(true);
				}






			}
		}

	}





}
