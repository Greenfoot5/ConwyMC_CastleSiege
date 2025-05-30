package me.greenfoot5.castlesiege.misc;

import com.nametagedit.plugin.NametagEdit;
import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.events.nametag.UpdateNameTagEvent;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Handles Names for Castle Siege
 */
public class CSNameTag implements Listener {

    /**
     * Update a player's name tag
     * @param event The event called when a player's name tag should be updated
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUpdateNameTag(UpdateNameTagEvent event) {
        // Don't do anything if the player isn't on CastleSiege
        if (!TeamController.getEveryone().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        Player player = event.getPlayer();
        CSPlayerData data = CSActiveData.getData(player.getUniqueId());
        assert data != null;

        Component title = event.getTitle() == null ? Component.empty() : event.getTitle().append(Component.text(" "));
        Component rank = data.getDisplayRank();
        String legacyRank = LegacyComponentSerializer.legacySection().serialize(rank);

        Component username;
        if (TeamController.getPlayers().contains(player.getUniqueId())) {
            username = player.name().color(TeamController.getTeam(player.getUniqueId()).primaryChatColor);
        } else {
            username = player.name().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        }

        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            // Set the display name
            player.displayName(Component.empty().append(title).append(rank).append(username));
            if (!legacyRank.isEmpty())
                NametagEdit.getApi().setPrefix(player, legacyRank.substring(0, legacyRank.length() - 1) + legacyColor(player, true));
            else
                NametagEdit.getApi().setPrefix(player, legacyColor(player, false));
        });
    }

    @SuppressWarnings("deprecation")
    private static String legacyColor(Player p, boolean addSpace) {
        if (TeamController.getPlayers().contains(p.getUniqueId())) {
            Component c = Component.text(" ").color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
            if (addSpace)
                return LegacyComponentSerializer.legacySection().serialize(c);
            else
                return LegacyComponentSerializer.legacySection().serialize(c).substring(0, 2);
        } else {
            return ChatColor.GRAY.toString() + ChatColor.ITALIC;
        }
    }

    /**
     * Get the player's username with minimessage
     * @param p The player
     * @return The player's username with minimessage colour
     */
    public static String mmUsername(Player p) {
        String name = Messenger.mm.serialize(p.name());
        if (TeamController.getPlayers().contains(p.getUniqueId())) {
            String tag = "color:" + TeamController.getTeam(p.getUniqueId()).primaryChatColor.asHexString();
            return "<" + tag + ">" + name + "</" + tag + ">";
        } else {
            return "<gray><i>" + name + "</gray>";
        }
    }

    /**
     * Get the player's username as a component
     * @param p The player
     * @return The player's username &amp; colour
     */
    public static Component username(Player p) {
        if (TeamController.isPlaying(p)) {
            return p.name().color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
        } else {
            return p.name().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        }
    }

    /**
     * @param sender The player whose level is being displayed
     * @param viewer The player viewing the level
     * @return The level with the correct colour
     */
    public static Component level(Player sender, Audience viewer) {
        CSPlayerData senderData = CSActiveData.getData(sender.getUniqueId());
        assert senderData != null;
        if (viewer.get(Identity.UUID).isEmpty() || CSActiveData.getData(viewer.get(Identity.UUID).get()) == null) {
            return Component.text(senderData.getLevel() + " ", NamedTextColor.YELLOW);
        }
        CSPlayerData viewerData = CSActiveData.getData(viewer.get(Identity.UUID).get());
        assert viewerData != null;
        int senderLevel = senderData.getLevel();
        int viewerLevel = viewerData.getLevel();
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
