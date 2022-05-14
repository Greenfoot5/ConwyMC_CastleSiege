package me.huntifi.castlesiege.events.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Modifies the fall damage taken by players
 */
public class FallDamage implements Listener {

    /**
     * Set the custom fall damage for the player
     * Cancels damage when landed on hay bales
     * @param e The event called when a player takes falling damage
     */
    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.isCancelled() || e.getCause() != EntityDamageEvent.DamageCause.FALL ||
                !(e.getEntity() instanceof Player)) {
            return;
        }

        // Make sure that landing on the edge of a hay bale also works
        Collection<Material> landedOn = getLandedOn(e.getEntity().getLocation());

        if (landedOn.contains(Material.HAY_BLOCK)) {
            e.setCancelled(true);
        } else {
            e.setDamage(e.getDamage() * 5);
        }
    }

    /**
     * Get the square of blocks landed on
     * @param l The location where the player landed
     * @return The blocks touching the player's feet
     */
    private Collection<Material> getLandedOn(Location l) {
        Collection<Material> landedOn = new ArrayList<>();
        double baseX = l.getX();
        double baseZ = l.getZ();
        l.setY(l.getY() - 1);

        // Floor X and floor Z
        l.setX(baseX - 0.3);
        l.setZ(baseZ - 0.3);
        landedOn.add(l.getBlock().getType());

        // Floor X and ceil Z
        l.setX(baseX - 0.3);
        l.setZ(baseZ + 0.3);
        landedOn.add(l.getBlock().getType());

        // Ceil X and floor Z
        l.setX(baseX + 0.3);
        l.setZ(baseZ - 0.3);
        landedOn.add(l.getBlock().getType());

        // Ceil X and ceil Z
        l.setX(baseX + 0.3);
        l.setZ(baseZ + 0.3);
        landedOn.add(l.getBlock().getType());

        return landedOn;
    }
}
