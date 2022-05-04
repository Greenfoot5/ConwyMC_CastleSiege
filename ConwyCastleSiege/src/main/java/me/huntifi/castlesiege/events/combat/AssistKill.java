package me.huntifi.castlesiege.events.combat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Tracks damage dealt to grant assists
 */
public class AssistKill implements Listener {

    private static final HashMap<UUID, HashMap<UUID, Double>> damageMap = new HashMap<>();

    /**
     * Track the attacker and amount of damage when a player takes damages
     * @param e The event called when a player is hit by an entity
     */
    @EventHandler (priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        // Differentiate between arrow shot by player and melee hit from player
        if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            UUID attacker = ((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId();
            addDamager(e.getEntity().getUniqueId(), attacker, e.getFinalDamage());
        } else if (e.getDamager() instanceof Player) {
            addDamager(e.getEntity().getUniqueId(), e.getDamager().getUniqueId(), e.getFinalDamage());
        }
    }

    /**
     * Create an entry for the target if absent
     * Add damage done by attacker to target's entry
     * @param target The player that was attacked
     * @param attacker The attacker
     * @param damage The final damage that was dealt
     */
    private void addDamager(UUID target, UUID attacker, double damage) {
        damageMap.putIfAbsent(target, new HashMap<>());
        damageMap.get(target).merge(attacker, damage, Double::sum);
    }

    /**
     * Get the player that dealt the most damage to the specified player
     * Stop tracking the specified player
     * @param uuid The unique ID of the killed player
     * @return The unique ID of the max damage dealer, null if never attacked by a player
     */
    public static UUID get(UUID uuid) {
        HashMap<UUID, Double> damagers = damageMap.get(uuid);
        if (damagers == null) {
            return null;
        }

        damageMap.remove(uuid);
        return damagers.entrySet().stream().max((x, y) -> x.getValue() > y.getValue() ? 1 : -1).get().getKey();
    }
}
