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
public class DamageBalance implements Listener {

    /**
     * Increases damage for non-customised sources as player health is increased
     * @param e The event called when a player takes falling damage
     */
    @EventHandler
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
            case FIRE:
            case FIRE_TICK:
            case HOT_FLOOR:
            case LAVA:
            case LIGHTNING:
            case MAGIC:
            case MELTING:
            case POISON:
            case SUFFOCATION:
            case THORNS:
            case WITHER:
                e.setDamage(e.getDamage() * 5);
        }
    }
}
