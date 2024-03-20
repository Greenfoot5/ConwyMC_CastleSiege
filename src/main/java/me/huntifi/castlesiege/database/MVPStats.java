package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.CSStats;
import me.huntifi.castlesiege.maps.events.NextMapEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

/**
 * Tracks the stats of players during a game
 */
public class MVPStats implements Listener {

    private static final HashMap<UUID, CSStats> stats = new HashMap<>();

    /**
     * Start tracking a player if they are not being tracked yet
     * @param uuid The unique ID of the player
     */
    public static void addPlayer(UUID uuid) {
        stats.putIfAbsent(uuid, new CSStats());
    }

    /**
     * Get all MVP stats
     * @return All player data for the current game
     */
    public static HashMap<UUID, CSStats> getStats() {
        return stats;
    }

    /**
     * Get the MVP stats of a player
     * @param uuid The unique ID of the player
     * @return The player's MVP player data
     */
    public static CSStats getStats(UUID uuid) {
        return stats.get(uuid);
    }

    /**
     * Reset the tracked players
     * Should only be used at the end of a game
     */
    public static void reset(NextMapEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
        stats.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(Main.plugin, () -> addPlayer(p.getUniqueId()));
         }
       });
    }
}
