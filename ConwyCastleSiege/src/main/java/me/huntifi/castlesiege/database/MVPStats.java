package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.data_types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Tracks the stats of players during a game
 */
public class MVPStats {

    private static final HashMap<UUID, PlayerData> stats = new HashMap<>();

    /**
     * Start tracking a player if they are not being tracked yet
     * @param uuid The unique ID of the player
     */
    public static void addPlayer(UUID uuid) {
        stats.putIfAbsent(uuid, new PlayerData());
    }

    /**
     * Get the MVP stats of a player
     * @param uuid The unique ID of the player
     * @return The player's MVP player data
     */
    public static PlayerData getStats(UUID uuid) {
        return stats.get(uuid);
    }

    /**
     * Reset the tracked players
     * Should only be used at the end of a game
     */
    public static void reset() {
        stats.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            addPlayer(p.getUniqueId());
        }
    }
}
