package me.huntifi.castlesiege.maps;

import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Manages the name tags of players
 */
public class NameTag implements Listener {
    /**
     * Get the player's username as a component
     * @param p The player
     * @return The player's username & colour
     */
    public static Component username(Player p) {
        if (MapController.getPlayers().contains(p.getUniqueId())) {
            return p.name().color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
        } else {
            return p.name().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        }
    }

    /**
     * Get the player's username with minimessage
     * @param p The player
     * @return The player's username with minimessage colour
     */
    public static String mmUsername(Player p) {
        String name = Messenger.mm.serialize(p.name());
        if (MapController.getPlayers().contains(p.getUniqueId())) {
//            if (hideTeamColour || hideBoth)
//                return "<white>" + name + "</white>";
            String tag = "color:" + TeamController.getTeam(p.getUniqueId()).primaryChatColor.asHexString();
            return "<" + tag + ">" + name + "</" + tag + ">";
        } else {
            return "<gray><i>" + name + "</gray>";
        }
    }

    public static Component level(int senderLevel, int viewerLevel) {
        // Sender is 11+ levels below the viewer
        if (senderLevel + 10 < viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.DARK_GREEN);
        }
        // Sender is 6-10 levels below the viewer
        else if (senderLevel + 5 < viewerLevel && senderLevel + 10 >= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.GREEN);
        }
        // Sender is within 5 levels of the viewer
        else if (senderLevel - 5 <= viewerLevel && senderLevel + 5 >= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.YELLOW);
        }
        // Sender is 6-10 levels above the viewer
        else if (senderLevel - 5 > viewerLevel && senderLevel - 10 <= viewerLevel) {
            return Component.text(senderLevel + " ").color(NamedTextColor.RED);
        }
        // Sender is 11+ levels above the viewer
        else {
            return Component.text(senderLevel + " ").color(NamedTextColor.DARK_RED);
        }
    }
}
