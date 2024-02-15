package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;


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

                    defender.setCollidable(false);
                    CraftLivingEntity craftEntityLiving = (CraftLivingEntity) e.getEntity();
                    craftEntityLiving.setCollidable(false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            defender.setCollidable(true);
                            craftEntityLiving.setCollidable(true);
                        }
                    }.runTaskLater(Main.plugin, 5);
                }

            }

    }
}
