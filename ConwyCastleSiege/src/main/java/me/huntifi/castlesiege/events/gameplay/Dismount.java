package me.huntifi.castlesiege.events.gameplay;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

/**
 * Handles dismount events
 */
public class Dismount implements Listener {

    /**
     * Remove the horse when its rider dismounts
     * @param e The event called when dismounting a horse
     */
    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        removeHorse(e.getDismounted());
    }

    /**
     * Remove the horse when its rider dies
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        removeHorse(e.getEntity().getVehicle());
    }

    /**
     * Remove the horse when its rider leaves the game
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        removeHorse(e.getPlayer().getVehicle());
    }

    /**
     * Remove the horse
     * @param e The horse that is to be removed
     */
    private void removeHorse(Entity e) {
        if (e instanceof Horse) {
            e.remove();
        }
    }
}
