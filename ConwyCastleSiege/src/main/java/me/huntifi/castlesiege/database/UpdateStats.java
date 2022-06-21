package me.huntifi.castlesiege.database;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class UpdateStats {

    /**
     * Add 1 kill to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addKill(UUID uuid) {
        ActiveData.getData(uuid).addKill();
        MVPStats.getStats(uuid).addKill();
        level(uuid);
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
        level(uuid);
    }

    /**
     * Add captures to the player's data
     * @param uuid The unique ID of the player
     * @param captures The amount of captures to add
     */
    public static void addCaptures(UUID uuid, double captures) {
        ActiveData.getData(uuid).addCaptures(captures);
        MVPStats.getStats(uuid).addCaptures(captures);
        level(uuid);
    }

    /**
     * Add 1 heal to the player's data
     * @param uuid The unique ID of the player
     */
    public static void addHeal(UUID uuid) {
        ActiveData.getData(uuid).addHeal();
        MVPStats.getStats(uuid).addHeal();
        level(uuid);
    }

    /**
     * Add supports to the player's data
     * @param uuid The unique ID of the player
     * @param supports The amount of supports to add
     */
    public static void addSupports(UUID uuid, double supports) {
        ActiveData.getData(uuid).addSupports(supports);
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
                PlayerData data = ActiveData.getData(uuid);
                int level = data.getLevel() + 1;
                if (data.getScore() >= levelScore(level)) {
                    data.addLevel();

                    Player p = Bukkit.getPlayer(uuid);
                    assert p != null;
                    p.sendMessage(ChatColor.GOLD + "[+] " + ChatColor.DARK_GREEN + "Congratulations, you leveled up to level: " + ChatColor.YELLOW + level);
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    NameTag.give(p);

                    // Announce every 10th level
                    if (level % 10 == 0) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[+] " + ChatColor.DARK_GREEN + p.getName() + " has reached level " + level + "!");
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
        if (lvl > 0) {
            return 8 * lvl * lvl - 8 * (lvl - 1) + levelScore(lvl -1);
        }

        return 0;
    }
}
