package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;


public class ArrowCollision implements Listener {

    /**
     * Make sure projectiles go through teammates
     *
     * @param e The event called when any projectile hits a teammate.
     *          We use this event to prevent our projectiles from being stopped by our team.
     *
     */
    @EventHandler
    public void projectileCollision(ProjectileHitEvent e) {
        if (e.isCancelled()) {
            return;
        }

        if(e.getEntity().getShooter() instanceof Player && e.getHitEntity() instanceof Player){
            Player attacker = (Player) e.getEntity().getShooter();
            Player defender = (Player) e.getHitEntity();

            if (TeamController.getTeam(attacker.getUniqueId()) == TeamController.getTeam(defender.getUniqueId())) {
                Vector thor = e.getEntity().getVelocity();
                Arrow a = defender.launchProjectile(Arrow.class);
                a.setShooter(attacker);
                a.setVelocity(thor);
            }

        }

    }
}