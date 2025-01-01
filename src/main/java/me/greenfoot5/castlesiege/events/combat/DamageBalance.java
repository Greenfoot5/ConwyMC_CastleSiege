package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Damageable;
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
     * Increases damage for non-customised sources as some are increased (player and horse health)
     * @param e The event called when an entity takes damage
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player || e.getEntity() instanceof AbstractHorse))
            return;

        if (e.getEntity() instanceof Player && !CSActiveData.hasPlayer(e.getEntity().getUniqueId()))
            return;

        // Removes all damage if the map isn't ongoing
        if (!MapController.isOngoing()) {
            e.setCancelled(true);
            return;
        }

        // All damage types are listed, but can be changed later
        switch (e.getCause()) {
            case POISON:
                e.setDamage(e.getDamage() * 8);
                if (((Damageable) e.getEntity()).getHealth() <= e.getDamage())
                {
                    e.setDamage(((Damageable) e.getEntity()).getHealth() - 1);
                }
                break;
            case CONTACT:
            case DRAGON_BREATH:
            case DROWNING:
            case DRYOUT:
            case FALL:
            case FALLING_BLOCK:
            case HOT_FLOOR:
            case LIGHTNING:
            case MAGIC:
            case MELTING:
            case SUFFOCATION:
            case THORNS:
            case WITHER:
                e.setDamage(e.getDamage() * 10);
                break;
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                e.setDamage(e.getDamage() * 11);
                break;
            case ENTITY_EXPLOSION:
                e.setDamage(e.getDamage() * 15);
                break;
            case VOID:
                e.setDamage(e.getDamage() * 30);
                break;
        }
    }
}
