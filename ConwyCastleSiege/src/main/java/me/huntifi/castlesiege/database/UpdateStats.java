package me.huntifi.castlesiege.database;

import java.util.UUID;

public class UpdateStats {

    /**
     * Add 1 kill to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addKill(UUID uuid) {
        ActiveData.getData(uuid).addKill();
        MVPStats.getStats(uuid).addKill();
    }

    /**
     * Add deaths to the player's data
     * @param uuid The unique ID of the player
     * @param deaths The amount of deaths to add
     */
    public static void addDeaths(UUID uuid, double deaths) {
        ActiveData.getData(uuid).addDeaths(deaths);
        MVPStats.getStats(uuid).addDeaths(deaths);
    }

    /**
     * Add 1 assist to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addAssist(UUID uuid) {
        ActiveData.getData(uuid).addAssist();
        MVPStats.getStats(uuid).addAssist();
    }

    /**
     * Add captures to the player's data
     * @param uuid The unique ID of the player
     * @param captures The amount of captures to add
     */
    public static void addCaptures(UUID uuid, double captures) {
        ActiveData.getData(uuid).addCaptures(captures);
        MVPStats.getStats(uuid).addCaptures(captures);
    }

    /**
     * Add 1 heal to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addHeal(UUID uuid) {
        ActiveData.getData(uuid).addHeal();
        MVPStats.getStats(uuid).addHeal();
    }

    /**
     * Add supports to the player's data
     * @param uuid The unique ID of the player
     * @param supports The amount of supports to add
     */
    public static void addSupports(UUID uuid, double supports) {
        ActiveData.getData(uuid).addSupports(supports);
        MVPStats.getStats(uuid).addSupports(supports);
    }
}
