package me.huntifi.castlesiege.combat;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Makes sure a player can't hurt a team member or their horse/boat
 */
public class NoHurtTeam implements Listener {

	/**
	 * Player has attempted to attack a player
	 * Cancels event if they are on the same team
	 */
	@EventHandler
	public void onHurt(EntityDamageByEntityEvent e) {
		// A player was hurt
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();

			sameTeam(e, p);
		}
	}

	/**
	 * Player has attempted to attack a horse
	 * Cancels event if they are on the same team
	 */
	@EventHandler
	public void onHurtHorse(EntityDamageByEntityEvent e) {
		// A horse with owner was hurt
		if (e.getEntity() instanceof Horse &&
				((Horse) e.getEntity()).getOwner() != null) {
			Player p = (Player) ((Horse) e.getEntity()).getOwner();

			sameTeam(e, p);
		}
	}

	/**
	 * Player has attempted to attack a boat
	 * Checks if there is a player in it on their team
	 */
	@EventHandler
	public void onHurtBoat(EntityDamageByEntityEvent e) {
		// A boat with player was hurt
		if (e.getEntity() instanceof Boat &&
				!e.getEntity().getPassengers().isEmpty() &&
				e.getEntity().getPassengers().get(0) instanceof Player) {
			Player p = (Player) e.getEntity().getPassengers().get(0);

			sameTeam(e, p);
		}
	}

	/**
	 * Cancels an attack if two entities are considered on the same team
	 */
	private void sameTeam(EntityDamageByEntityEvent e, Player p) {
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
