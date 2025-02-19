package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.maps.TeamController;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

/**
 * Prevents arrows from hitting teammates, instead passing through them
 */
public class ArrowCollision implements Listener {

    /**
     * Make sure projectiles go through teammates
     * @param e The event called when any projectile hits a teammate.
     *          We use this event to prevent our projectiles from being stopped by our team.
     */
    @EventHandler(ignoreCancelled = true)
    public void projectileCollision(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof AbstractArrow))
            return;
        if (InCombat.isPlayerInLobby((e.getEntity().getUniqueId())))
            return;

        if (e.getEntity().getShooter() instanceof Player attacker && e.getHitEntity() instanceof Player defender) {

            if (TeamController.getTeam(attacker.getUniqueId()) == TeamController.getTeam(defender.getUniqueId())) {
                Vector thor = e.getEntity().getVelocity();
                Arrow a = defender.launchProjectile(Arrow.class);
                a.setShooter(attacker);
                a.setVelocity(thor);
                e.setCancelled(true);
            }
        }
    }
}