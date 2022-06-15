package me.huntifi.castlesiege.events.combat;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Modifies the fall damage taken by players
 */
public class DamageBalance implements Listener {

    /**
     * Increases damage for non-customised sources as player health is increased
     * @param e The event called when a player takes falling damage
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
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
                break;
            case SUFFOCATION:
            case THORNS:
            case WITHER:
                e.setDamage(e.getDamage() * 7);
                break;
            case ENTITY_EXPLOSION:
                e.setDamage(150);
                break;
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                e.setDamage(e.getDamage() * 4);
                break;
        }
    }
}
