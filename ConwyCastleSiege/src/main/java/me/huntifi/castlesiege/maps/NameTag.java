package me.huntifi.castlesiege.maps;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Manages the name tags of players
 */
public class NameTag {

    /**
     * Give a player their name tag
     * @param p The player
     */
    public static void give(Player p) {
        if (p == null) {
            return;
        }

        // Only set name tag color if data has not been loaded yet
        PlayerData data = ActiveData.getData(p.getUniqueId());
        if (data == null) {
            NametagEdit.getApi().setPrefix(p, color(p).toString());
            return;
        }

        // Get the player's wanted rank
        String rank;
        if (p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)) {
            rank = convertRank(data.getStaffRank());
        } else {
            rank = convertRank(data.getRank());
        }

        // Set the player's tags
        p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                NametagEdit.getApi().setPrefix(p, rank + color(p));
            }
        }.runTask(Main.plugin);
        
    }

    /**
     * Get the player's primary chat color
     * @param p The player
     * @return The player's chat color
     */
    public static ChatColor color(Player p) {
        return MapController.getCurrentMap().getTeam(p.getUniqueId()).primaryChatColor;
    }

    /**
     * Get the pretty representation of the player's staff or donator tag
     * @param rank The rank to convert
     * @return A formatted rank or an empty string if invalid
     */
    public static String convertRank(String rank) {
        switch (rank.toLowerCase()) {
            // Staff Ranks
            case "chatmod":
                return "§9ChatMod ";
            case "chatmod+":
                return "§9ChatMod§a+ ";
            case "moderator":
                return "§a§lMod ";
            case "developer":
                return "§2§lDev ";
            case "admin":
                return "§c§lAdmin ";
            // Donator Ranks
            case "esquire":
                return "§3Esquire ";
            case "noble":
                return "§aNoble ";
            case "baron":
                return "§5Baron ";
            case "count":
                return "§6Count ";
            case "duke":
                return "§4Duke ";
            case "viceroy":
                return "§dViceroy ";
            case "king":
                return "§eKing ";
            case "high_king":
                return "§eHigh King ";
            default:
                return "";
        }
    }
}
