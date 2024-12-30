package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.database.CSActiveData;
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
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        if (!CSActiveData.hasPlayer(e.getEntity().getUniqueId()))
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
                if (((Player) e.getEntity()).getHealth() <= e.getDamage())
                {
                    e.setDamage(((Player) e.getEntity()).getHealth() - 1);
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
