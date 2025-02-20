package me.greenfoot5.castlesiege.database;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.events.LevelUpEvent;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Updated a player's stats for both current game and all-time
 */
public class UpdateStats {

    public static int[] levelRequirements = new int[] {
        0,
            0, 50, 200, 400, 650,
            950, 1_300, 1_700, 2_150, 2_650,
            3_250, 3_950, 4_750, 5_650, 6_650,
            7_750, 8_950, 10_250, 11_700, 13_300,
            15_050, 16_950, 19_000, 21_200, 23_600,
            26_200, 29_000, 32_000, 35_200, 38_600,
            42_200, 46_000, 50_000, 54_250, 58_750,
            63_500, 68_500, 73_750, 79_250, 85_000,
            // Current Cap is 40
    };

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
                    Bukkit.getScheduler().runTask(Main.plugin,
                            () -> {
                                Bukkit.getPluginManager().callEvent(new LevelUpEvent(p, level));
                                Bukkit.getPluginManager().callEvent(new UpdateNameTagEvent(p));
                            });
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
        // Clamp to avoid out of bounds
        lvl = Math.clamp(lvl, 0, 40);
        return levelRequirements[lvl];
    }
}
