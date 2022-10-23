package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Modifies the damage taken by players
 */
public class DamageBalance implements Listener {

    /**
     * Increases damage for non-customised sources as player health is increased
     * @param e The event called when a player takes damage
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        // Removes all damage if the map isn't ongoing
        if (!MapController.isOngoing()) {
            e.setCancelled(true);
            return;
        }

        switch (e.getCause()) {
            // All damage types are listed, but can be changed later
            case CONTACT:
            case DRAGON_BREATH:
            case DROWNING:
            case DRYOUT:
            case FALLING_BLOCK:
            case HOT_FLOOR:
            case LIGHTNING:
            case MAGIC:
            case MELTING:
            case POISON:
                e.setDamage(e.getDamage() * 8);
                if (((Player) e.getEntity()).getHealth() <= e.getDamage())
                {
                    e.setDamage(((Player) e.getEntity()).getHealth() - 1);
                }
                break;
            case SUFFOCATION:
            case THORNS:
            case WITHER:
            case ENTITY_EXPLOSION:
                e.setDamage(e.getDamage() * 11);
                break;
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                e.setDamage(e.getDamage() * 9.5);
                break;
        }
    }
}
