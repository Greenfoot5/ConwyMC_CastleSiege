package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Tracks damage dealt to grant assists
 */
public class AssistKill implements Listener {

    private static final HashMap<UUID, HashMap<UUID, Double>> damageMap = new HashMap<>();
    private static final HashMap<UUID, List<BukkitRunnable>> brMap = new HashMap<>();

    /**
     * Track the attacker and amount of damage when a player takes damages
     * @param e The event called when a player is hit by an entity
     */
    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            if (!(e.getEntity() instanceof Player)) {
                return;
            }

            // Differentiate between arrow shot by player and melee hit from player
            if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
                UUID attacker = ((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId();
                addDamager(e.getEntity().getUniqueId(), attacker, e.getFinalDamage());
            } else if (e.getDamager() instanceof Player) {
                addDamager(e.getEntity().getUniqueId(), e.getDamager().getUniqueId(), e.getFinalDamage());
            }
        });
    }

    /**
     * Create an entry for the target if absent.
     * Add damage done by attacker to target's entry.
     * @param target The player that was attacked
     * @param attacker The attacker
     * @param damage The final damage that was dealt
     */
    public static void addDamager(UUID target, UUID attacker, double damage) {
        damageMap.putIfAbsent(target, new HashMap<>());
        damageMap.get(target).merge(attacker, damage, Double::sum);

        // Damage should no longer be counted towards an assist after 1 minute
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                damageMap.get(target).merge(attacker, -damage, Double::sum);
            }
        };
        brMap.putIfAbsent(target, new ArrayList<>());
        brMap.get(target).add(br);
        br.runTaskLater(Main.plugin, 1200);
    }

    /**
     * Remove damage done by attacker to target's entry.
     * @param target The player that was attacked
     * @param attacker The attacker
     */
    public static void removeDamager(UUID target, UUID attacker) {
        if (damageMap.containsKey(target))
            damageMap.get(target).remove(attacker);
    }

    /**
     * Get the player that dealt the most damage to the specified player.
     * Stop tracking the specified player.
     * @param uuid The unique ID of the killed player
     * @return The unique ID of the max damage dealer, null if never attacked by a player
     */
    public static UUID get(UUID uuid) {
        List<BukkitRunnable> brList = brMap.getOrDefault(uuid , new ArrayList<>());
        cancelRunnables(brList);
        brList.clear();

        HashMap<UUID, Double> damagers = damageMap.get(uuid);
        if (damagers == null || damagers.isEmpty()) {
            return null;
        }

        damageMap.remove(uuid);
        return damagers.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    /**
     * Remove all tracked damage.
     */
    public static void reset() {
        for (List<BukkitRunnable> brList : brMap.values()) {
            cancelRunnables(brList);
        }
        brMap.clear();
        damageMap.clear();
    }

    /**
     * Cancels bukkit runnables if they haven't been cancelled yet
     * @param brList A list of bukkit runnables that should be cancelled
     */
    private static void cancelRunnables(List<BukkitRunnable> brList) {
        for (BukkitRunnable br : brList) {
            if (!br.isCancelled()) {
                br.cancel();
            }
        }
    }
}
