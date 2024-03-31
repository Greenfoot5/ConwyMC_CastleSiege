package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Stores the data of online players
 */
public class CSActiveData extends ActiveData {

    /**
     * Add a player's data to the active storage
     * @param uuid The unique ID of the player
     * @param data The player's data
     */
    public static void addPlayer(UUID uuid, CSPlayerData data) {
        playerData.put(uuid, data);
    }

    /**
     * Check whether the active storage contains data of a player
     * @param uuid The unique ID of the player
     * @return Whether there is data stored for the player
     */
    public static boolean hasPlayer(UUID uuid) {
        PlayerData data = playerData.get(uuid);
        return data instanceof CSPlayerData;
    }

    /**
     * Get the data of a player
     * @param uuid The unique ID of the player
     * @return The player's data
     */
    public static CSPlayerData getData(UUID uuid) {
        PlayerData data = playerData.get(uuid);
        if (data instanceof CSPlayerData)
            return (CSPlayerData) playerData.get(uuid);
        return null;
    }

    public static Collection<UUID> getPlayers() {
        List<UUID> uuids = new ArrayList<>();
        for (UUID uuid : playerData.keySet()) {
            if (playerData.get(uuid) instanceof CSPlayerData) {
                uuids.add(uuid);
            }
        }
        return uuids;
    }
}
