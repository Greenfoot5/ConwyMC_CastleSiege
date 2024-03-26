package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Updated a player's stats for both current game and all-time
 */
public class UpdateStats {

    /**
     * Add 1 kill to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addKill(UUID uuid) {
        CSActiveData.getData(uuid).addKill();
        MVPStats.getStats(uuid).addKill();
        level(uuid);
    }

    /**
     * Add deaths to the player's data
     * @param uuid The unique ID of the player
     * @param deaths The amount of deaths to add
     */
    public static void addDeaths(UUID uuid, double deaths) {
        if (uuid == null || CSActiveData.getData(uuid) == null)
            return;

        CSActiveData.getData(uuid).addDeaths(deaths);
        MVPStats.getStats(uuid).addDeaths(deaths);
    }

    /**
     * Add 1 assist to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addAssist(UUID uuid) {
        // Return if the player is offline
        if (CSActiveData.getData(uuid) == null)
            return;

        CSActiveData.getData(uuid).addAssist();
        MVPStats.getStats(uuid).addAssist();
        level(uuid);
    }

    /**
     * Add captures to the player's data
     * @param uuid The unique ID of the player
     * @param captures The amount of captures to add
     */
    public static void addCaptures(UUID uuid, double captures) {
        CSActiveData.getData(uuid).addCaptures(captures);
        MVPStats.getStats(uuid).addCaptures(captures);
        level(uuid);
    }

    /**
     * Add heals to the player's data
     * @param uuid The unique ID of the player
     * @param heals The amount of heal to add
     */
    public static void addHeals(UUID uuid, double heals) {
        CSActiveData.getData(uuid).addHeals(heals);
        MVPStats.getStats(uuid).addHeals(heals);
        level(uuid);
    }

    /**
     * Add supports to the player's data
     * @param uuid The unique ID of the player
     * @param supports The amount of supports to add
     */
    public static void addSupports(UUID uuid, double supports) {
        CSActiveData.getData(uuid).addSupports(supports);
        MVPStats.getStats(uuid).addSupports(supports);
        level(uuid);
    }

    /**
     * Attempt to level up the player
     * @param uuid The unique ID of the player
     */
    private static void level(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                CSPlayerData data = CSActiveData.getData(uuid);
                int level = data.getLevel() + 1;
                if (data.getScore() >= levelScore(level)) {
                    data.addLevel();

                    Player p = Bukkit.getPlayer(uuid);
                    assert p != null;
                    Messenger.sendCongrats("Congratulations, you leveled up to level: <yellow>" + level, p);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    p.setLevel(level);
                    Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(p));

                    // Announce every 5th level
                    if (level % 5 == 0) {
                        Messenger.broadcastCongrats(p.getName() + " has reached level " + level + "!");
                    }
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Recursive function to calculate the score required for a level
     * @param lvl The level
     * @return The score required to reach the level
     */
    public static double levelScore(int lvl) {

        if (lvl >= 65) {
            return 1.5 * lvl * lvl - 1.5 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl >= 60) {
            return 2 * lvl * lvl - 3 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl >= 48) {
            return 3 * lvl * lvl - 4 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl >= 36) {
            return 4 * lvl * lvl - 5 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl >= 24) {
            return 5 * lvl * lvl - 6 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl >= 12) {
            return 6 * lvl * lvl - 7 * (lvl - 1) + levelScore(lvl -1);
        } else if (lvl > 0) {
            return 8 * lvl * lvl - 8 * (lvl - 1) + levelScore(lvl -1);
        }

        return 0;
    }
}
