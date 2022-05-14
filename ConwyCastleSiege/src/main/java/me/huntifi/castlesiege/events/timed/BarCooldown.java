package me.huntifi.castlesiege.events.timed;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Animates cooldowns using the player's exp bar
 */
public class BarCooldown implements Runnable {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    /**
     * Decrease the cooldown of all players by 1 tick
     */
    @Override
    public void run() {
        for (UUID uuid : cooldowns.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null || !p.isOnline() || p.getExp() == 0) {
                remove(uuid);
                return;
            }

            p.setExp(Math.max(0, p.getExp() - 1F / cooldowns.get(uuid)));
        }
    }

    /**
     * Set a player's exp bar cooldown
     * @param uuid The unique ID of the player
     * @param cooldown The cooldown in ticks
     */
    public static void add(UUID uuid, long cooldown) {
        if (cooldown <= 0) {
            return;
        }

        Player p = Bukkit.getPlayer(uuid);
        if (p == null || !p.isOnline()) {
            return;
        }

        p.setExp(1);
        cooldowns.put(uuid, cooldown);
    }

    /**
     * Remove the player's exp bar cooldown
     * @param uuid The unique ID of the player
     */
    public static void remove(UUID uuid) {
        cooldowns.remove(uuid);

        Player p = Bukkit.getPlayer(uuid);
        if (p != null && p.isOnline()) {
            p.setExp(0);
        }
    }
}
