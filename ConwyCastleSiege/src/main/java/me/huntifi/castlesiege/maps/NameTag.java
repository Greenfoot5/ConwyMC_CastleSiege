package me.huntifi.castlesiege.maps;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

        // Get the player's wanted rank
        PlayerData data = ActiveData.getData(p.getUniqueId());
        String rank;
        if (p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)) {
            rank = convertRank(data.getStaffRank());
        } else {
            rank = convertRank(data.getRank());
        }

        // Set the player's tags
        p.setDisplayName("§e" + data.getLevel() + " " + rank + color(p) + p.getName());
        NametagEdit.getApi().setPrefix(p, rank + color(p));
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
    private static String convertRank(String rank) {
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
            case "count":
                return "§6Count ";
            case "baron":
                return "§5Baron ";
            case "duke":
                return "§4Duke ";
            case "king":
                return "§eKing ";
            case "high_king":
                return "§eHigh King ";
            default:
                return "";
        }
    }
}
