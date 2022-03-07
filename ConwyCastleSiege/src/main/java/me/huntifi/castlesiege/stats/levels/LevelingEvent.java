package me.huntifi.castlesiege.stats.levels;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LevelingEvent {

    private static int getLevel(double score) {
        int value = 0;
        int level = 0;
        while (score < value) {
            level++;
            value = 8 * (level ^ 2) - 8 * (level - 1);
        }
        return level;
    }

    private static boolean hasTen(int newLevel, int oldLevel) {
        int i = oldLevel;
        while (i < newLevel) {
            if (i % 10 == 0)
                return true;
            i++;
        }
        return false;
    }

    public static void doLeveling() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            levelPlayer(p);
        }
    }

    public static void levelPlayer(Player player)
    {
        UUID uuid = player.getUniqueId();
        int oldLevel = StatsChanging.getLevel(uuid);
        int newLevel = getLevel(StatsChanging.getScore(uuid));

        if (newLevel > oldLevel) {
            player.sendMessage(ChatColor.DARK_GREEN + "Congrats you leveled up to level: " + ChatColor.YELLOW + "10");
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            if (hasTen(newLevel, oldLevel)) {
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " has reached level 10!");
            }
            StatsChanging.setLevel(uuid, 1);
            NametagsEvent.GiveNametag(player);
        }
    }
}
