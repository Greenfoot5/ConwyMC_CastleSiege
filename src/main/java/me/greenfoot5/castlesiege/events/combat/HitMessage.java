package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Displays the hit message and sound when a player hits a player or animal with an arrow
 */
public class HitMessage implements Listener {

	/**
	 * Notifies the shooter when they hit a player or animal
     * @param e The ProjectileHitEvent
     */
	@EventHandler(ignoreCancelled = true)
	public void onHit(ProjectileHitEvent e) {
		if (!CSActiveData.hasPlayer(e.getEntity().getOwnerUniqueId()))
			return;

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			// Check it was a player that fired an arrow
			if (e.getEntity() instanceof Arrow arrow && e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();

				// The player hit another player or an animal
				if (e.getHitEntity() instanceof Player) {
					if (TeamController.getTeam(player.getUniqueId()) != TeamController.getTeam(e.getHitEntity().getUniqueId())) {
						notifyHit(player, e.getHitEntity().getName());
					}
				} else if (e.getHitEntity() instanceof Horse && ((Horse) e.getHitEntity()).getOwner() != null) {
					if (TeamController.getTeam(player.getUniqueId()) != TeamController.getTeam(((Horse) e.getHitEntity()).getOwner().getUniqueId())) {
						notifyHit(player, ((Horse) e.getHitEntity()).getOwner().getName() + "'s Horse");
					}
				}else if (e.getHitEntity() instanceof Animals || e.getHitEntity() instanceof Bat) {
					notifyHit(player, e.getHitEntity().getType().toString());
				}
			}
		});
	}

	/**
	 * Notify the player about hitting an entity and add them to combat
	 * @param player The player that shot the arrow
	 * @param hitName The name to display for the hit entity
	 */
	private void notifyHit(Player player, String hitName) {
		Messenger.sendActionHit(hitName, player);
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0.5f);

		// The shooter has interacted with the game
		InCombat.addPlayerToCombat(player.getUniqueId());
	}
}
