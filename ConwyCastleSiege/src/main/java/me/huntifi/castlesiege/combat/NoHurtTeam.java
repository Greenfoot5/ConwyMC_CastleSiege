package me.huntifi.castlesiege.combat;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NoHurtTeam implements Listener {

	@EventHandler
	public void onHurt(EntityDamageByEntityEvent e) {
		// A player was hurt
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();

			// Hurt by teammate
			if (e.getDamager() instanceof Player &&
					MapController.getCurrentMap().getTeam(p.getUniqueId()) ==
					MapController.getCurrentMap().getTeam(e.getDamager().getUniqueId())) {
				e.setCancelled(true);
			}

			// Hurt by arrow from teammate
			if (e.getDamager() instanceof Arrow &&
					((Arrow) e.getDamager()).getShooter() instanceof Player &&
					MapController.getCurrentMap().getTeam(p.getUniqueId()) ==
							MapController.getCurrentMap().getTeam(
									((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHurtHorse(EntityDamageByEntityEvent e) {
		// A horse with owner was hurt
		if (e.getEntity() instanceof Horse &&
				((Horse) e.getEntity()).getOwner() != null) {
			Player p = (Player) ((Horse) e.getEntity()).getOwner();

			// Hurt by owner's teammate
			if (e.getDamager() instanceof Player &&
					MapController.getCurrentMap().getTeam(e.getDamager().getUniqueId()) ==
							MapController.getCurrentMap().getTeam(p.getUniqueId())) {
				e.setCancelled(true);
			}

			// Hurt by arrow from owner's teammate
			if (e.getDamager() instanceof Arrow &&
					((Arrow) e.getDamager()).getShooter() instanceof Player &&
					MapController.getCurrentMap().getTeam(p.getUniqueId()) ==
							MapController.getCurrentMap().getTeam(
									((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHurtBoat(EntityDamageByEntityEvent e) {
		// A boat with player was hurt
		if (e.getEntity() instanceof Boat &&
				!e.getEntity().getPassengers().isEmpty() &&
				e.getEntity().getPassengers().get(0) instanceof Player) {
			Player p = (Player) e.getEntity().getPassengers().get(0);

			// Hurt by player's teammate
			if (e.getDamager() instanceof Player &&
					MapController.getCurrentMap().getTeam(e.getDamager().getUniqueId()) ==
							MapController.getCurrentMap().getTeam(p.getUniqueId())) {
				e.setCancelled(true);
			}

			// Hurt by arrow from player's teammate
			if (e.getDamager() instanceof Arrow &&
					((Arrow) e.getDamager()).getShooter() instanceof Player &&
					MapController.getCurrentMap().getTeam(p.getUniqueId()) ==
							MapController.getCurrentMap().getTeam(
									((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}
}
