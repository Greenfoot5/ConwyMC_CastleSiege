package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.data_types.PlayerData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Stores the data of online players
 */
public class ActiveData {

    private static final HashMap<UUID, PlayerData> playerData = new HashMap<>();

    /**
     * Add a player's data to the active storage
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    public static void addPlayer(UUID uuid, PlayerData data) {
        playerData.put(uuid, data);
    }

    /**
     * Check whether the active storage contains data of a player
     * @param uuid The unique ID of the player
     * @return Whether there is data stored for the player
     */
    public static boolean hasPlayer(UUID uuid) {
        return playerData.containsKey(uuid);
    }

    /**
     * Get the unique ID of all players whose stats are actively stored
     * @return All keys in playerData
     */
    public static Collection<UUID> getPlayers() {
        return playerData.keySet();
    }

    /**
     * Get the data of a player
     * @param uuid The unique ID of the player
     * @return The player's data
     */
    public static PlayerData getData(UUID uuid) {
        return playerData.get(uuid);
    }
}
