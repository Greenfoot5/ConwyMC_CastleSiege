package me.huntifi.castlesiege.events.gameplay;

import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.donator_kits.Cavalry;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Objects;

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
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, "Cavalry")) {
                p.setCooldown(Material.WHEAT, Cavalry.HORSE_COOLDOWN);
            }
        }
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

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.HORSE) {
            Horse horse = (Horse) e.getEntity();
            Entity passenger = horse.getPassengers().get(0);
            Player p = (Player) passenger;
            if (p != null && Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, "Cavalry")) {
                p.setCooldown(Material.WHEAT, Cavalry.HORSE_COOLDOWN);
            }
        }
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
