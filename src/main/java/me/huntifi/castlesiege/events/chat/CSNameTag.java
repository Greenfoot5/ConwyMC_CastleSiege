package me.huntifi.castlesiege.events.chat;

import com.nametagedit.plugin.NametagEdit;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.events.nametag.UpdateNameTagEvent;
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

public class CSNameTag implements Listener {

    /**
     * Update a player's name tag
     * @param event The event called when a player's name tag should be updated
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUpdateNameTag(UpdateNameTagEvent event) {
        Player player = event.getPlayer();
        CSPlayerData data = CSActiveData.getData(player.getUniqueId());

        // Don't do anything if the player hasn't been loaded into CSActiveData or MapController
        if (data == null) {
            return;
        }

        // Get the player's wanted rank
        Component rank = data.getDisplayRank();
        String legacyRank = LegacyComponentSerializer.legacySection().serialize(rank);

        Component username;
        if (MapController.getPlayers().contains(player.getUniqueId())) {
             username = player.name().color(TeamController.getTeam(player.getUniqueId()).primaryChatColor);
        } else {
            username = player.name().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        }

        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            // Set the display name
            player.displayName(rank.append(username));
            NametagEdit.getApi().setPrefix(player, legacyRank.substring(0, legacyRank.length() - 1) + legacyColor(player));
        });
    }

    public static String legacyColor(Player p) {
        if (MapController.getPlayers().contains(p.getUniqueId())) {
            Component c = Component.text(" ").color(TeamController.getTeam(p.getUniqueId()).primaryChatColor);
            return LegacyComponentSerializer.legacySection().serialize(c);
        } else {
            return ChatColor.GRAY.toString() + ChatColor.ITALIC;
        }
    }

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
