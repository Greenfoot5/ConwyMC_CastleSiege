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
        if (!stats.containsKey(uuid)) {
            stats.put(uuid, new PlayerData());
        }
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

    /**
     * Add 1 kill to the player's MVP stats
     * @param uuid The unique ID of the player
     */
    public static void addKills(UUID uuid) {
        stats.get(uuid).addKills();
    }

    /**
     * Add deaths to the player's MVP stats
     * @param uuid The unique ID of the player
     * @param deaths The amount of deaths to add
     */
    public static void addDeaths(UUID uuid, Double deaths) {
        stats.get(uuid).addDeaths(deaths);
    }

    /**
     * Add captures to the player's MVP stats
     * @param uuid The unique ID of the player
     * @param captures The amount of captures to add
     */
    public static void addCaptures(UUID uuid, Double captures) {
        stats.get(uuid).addCaptures(captures);
    }

    /**
     * Add 1 heal to the player's MVP stats
     * @param uuid The unique ID of the player
     */
    public static void addHeals(UUID uuid) {
        stats.get(uuid).addHeals();
    }

    /**
     * Add supports to the player's MVP stats
     * @param uuid The unique ID of the player
     * @param supports The amount of supports to add
     */
    public static void addSupports(UUID uuid, Double supports) {
        stats.get(uuid).addSupports(supports);
    }

    /**
     * Add 1 assist to the player's MVP stats
     * @param uuid The unique ID of the player
     */
    public static void addAssists(UUID uuid) {
        stats.get(uuid).addAssists();
    }
}
