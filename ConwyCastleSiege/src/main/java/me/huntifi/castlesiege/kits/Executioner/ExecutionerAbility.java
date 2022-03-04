package me.huntifi.castlesiege.kits.Executioner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class ExecutionerAbility implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void Exe1(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Executioner")) {

				if (whoHit.getItemInHand().getType() == Material.IRON_AXE) {

					if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {
						
						AttributeInstance healthAttribute = whoWasHit.getAttribute(Attribute.GENERIC_MAX_HEALTH);

						if (whoWasHit.getHealth() < ((healthAttribute.getBaseValue() * 0.01) * 37)) {
							ed.setCancelled(true);
							whoWasHit.setHealth(0); 
							Location loc = whoWasHit.getLocation();
							whoWasHit.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_DEATH , 1, 1);


						}
					} else {
						ed.isCancelled();
						ed.setCancelled(true);
					}

				} else {
					
					ed.isCancelled();
					ed.setCancelled(true);
					
					if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {
						ed.setCancelled(false);
					}
				}
			}
		}


	}
}
