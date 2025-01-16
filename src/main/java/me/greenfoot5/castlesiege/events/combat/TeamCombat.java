package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.commands.donator.DuelCommand;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Makes sure a player can't hurt a team member or their horse/boat
 */
public class TeamCombat implements Listener {

	/**
	 * Player has attempted to attack a player, horse, or boat.
	 * Cancels event if the corresponding players are on the same team.
	 * @param event The event called when a player attacks another entity
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onHurt(EntityDamageByEntityEvent event) {
		if (!MapController.isOngoing()) {
			event.setCancelled(true);
			return;
		}

		// A player was hurt
		if (event.getEntity() instanceof Player player) {
            sameTeam(event, player);
		}

		// A horse with owner was hurt
		else if (event.getEntity() instanceof Horse &&
                ((Horse) event.getEntity()).getOwner() instanceof Player player) {
            sameTeam(event, player);
		}

		// A boat with player was hurt
		else if (event.getEntity() instanceof Boat &&
				!event.getEntity().getPassengers().isEmpty() &&
                event.getEntity().getPassengers().getFirst() instanceof Player player) {
            sameTeam(event, player);
		}
	}

	/**
	 * Cancels an attack if two entities are considered on the same team
	 */
	private void sameTeam(EntityDamageByEntityEvent e, Player p) {

		if (DuelCommand.isDueling(p.getUniqueId()))
			return;

		// Hurt by teammate
		if (e.getDamager() instanceof Player &&
				TeamController.getTeam(p.getUniqueId()) == TeamController.getTeam(e.getDamager().getUniqueId())) {
			e.setCancelled(true);
		}

		// Hurt by projectile from teammate
		else if (e.getDamager() instanceof Projectile &&
				((Projectile) e.getDamager()).getShooter() instanceof Player &&
				TeamController.getTeam(p.getUniqueId()) == TeamController.getTeam(
						((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId())) {
			e.setCancelled(true);
		}
	}
}
