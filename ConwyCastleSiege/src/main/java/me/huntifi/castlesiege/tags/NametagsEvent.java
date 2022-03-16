package me.huntifi.castlesiege.tags;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.commands.togglerankCommand;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class NametagsEvent {

    public static void GiveNametag(Player p) {

        if (p == null) {
            return;
        }

        int level = StatsChanging.getLevel(p.getUniqueId());
        String tagColor = "";
        String tag = "";
        ChatColor nameColor = MapController.getCurrentMap().getTeam(p.getUniqueId()).primaryChatColor;

        // Get the player's staff tag
        String staffRank = StatsChanging.getStaffRank(p.getUniqueId());
        switch (staffRank.toLowerCase()) {
            case "chatmod":
                tagColor = "§9 ";
                tag = "ChatMod";
                break;
            case "chatmod+":
                tagColor = "§9 ";
                tag = "ChatMod§a+";
                break;
            case "moderator":
                tagColor = "§a§l ";
                tag = "Mod";
                break;
            case "developer":
                tagColor = "§2§l ";
                tag = "Dev";
                break;
            case "admin":
                tagColor = "§c§l ";
                tag = "Admin";
                break;
        }

        // Player has and wants to display a staff tag
        if (!togglerankCommand.rankers.contains(p) && staffRank.equalsIgnoreCase("none")) {

            p.setDisplayName("§e" + level + tagColor + tag + " " + nameColor + p.getName());
            NametagEdit.getApi().setPrefix(p, tag + nameColor + " ");
            return;
        }

        // Get the player's donator rank
        String donorRank = StatsChanging.getRank(p.getUniqueId());
        switch (donorRank.toLowerCase()) {
            case "esquire":
                tagColor = "§3 ";
                tag = "Equire";
                break;
            case "noble":
                tagColor = "§a ";
                tag = "Noble";
                break;
            case "count":
                tagColor = "§6 ";
                tag = "Count";
                break;
            case "baron":
                tagColor = "§5 ";
                tag = "Baron";
                break;
            case "duke":
                tagColor = "§4 ";
                tag = "Duke";
                break;
            case "king":
                tagColor = "§e ";
                tag = "King";
                break;
            case "highking":
                tagColor = "§e ";
                tag = "High King";
                break;
        }

        // Sets the player's tags
        p.setDisplayName("§e" + level + tagColor + tag + " " + nameColor + p.getName());
        NametagEdit.getApi().setPrefix(p, tag + nameColor + " ");
    }

    public static ChatColor color(Player p) {
        return MapController.getCurrentMap().getTeam(p.getUniqueId()).primaryChatColor;
    }
}
