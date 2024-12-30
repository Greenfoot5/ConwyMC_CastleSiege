package me.huntifi.castlesiege.events.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.DuelCommand;
import me.huntifi.castlesiege.data_types.MapBorder;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Prevents players from venturing too far from the battlefield
 */
public class LeaveMapBorder implements Listener {

    // The amount of blocks between the warning message and being killed
    private static final double MARGIN = 20;

    // The players that are on cooldown before being shown the warning again
    private final ArrayList<UUID> onCooldown = new ArrayList<>();

    /**
     * Warn the player when they leave the map border.
     * Kill the player when they exceed the margin as well.
     * @param e The event called when a player moves
     */
    @EventHandler
    public void onLeaveBorder(PlayerMoveEvent e) {
        if (!CSActiveData.hasPlayer(e.getPlayer().getUniqueId()))
            return;

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Check if the player should be held accountable
            Player player = e.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                    || InCombat.isPlayerInLobby(player.getUniqueId()) || DuelCommand.isDueling(player.getUniqueId()))
                return;

            // Check if a map border has been set
            MapBorder mapBorder = MapController.getCurrentMap().getMapBorder();
            if (mapBorder == null)
                return;

            // Check if the players has crossed the map border
            Location location = player.getLocation();
            checkLocation(player, false, location.getZ(), mapBorder.north);
            checkLocation(player, true, location.getX(), mapBorder.east);
            checkLocation(player, true, location.getZ(), mapBorder.south);
            checkLocation(player, false, location.getX(), mapBorder.west);
        });
    }

    /**
     * Check if the player has crossed the map border.
     * @param player The player
     * @param positiveDirection Boolean indication of the direction
     * @param playerLoc The player's location
     * @param borderLoc The border's location
     */
    private void checkLocation(Player player, boolean positiveDirection, double playerLoc, double borderLoc) {
        if (positiveDirection) {
            if (playerLoc > borderLoc + MARGIN)
                killPlayer(player);
            else if (playerLoc > borderLoc)
                sendWarning(player);
        } else {
            if (playerLoc < borderLoc - MARGIN)
                killPlayer(player);
            else if (playerLoc < borderLoc)
                sendWarning(player);
        }
    }

    /**
     * Warn the player that they have left the battlefield.
     * @param player The player
     */
    private void sendWarning(Player player) {
        UUID uuid = player.getUniqueId();
        if (!onCooldown.contains(uuid)) {
            onCooldown.add(uuid);
            Title.Times times = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(2000), Duration.ofMillis(500));
            Title title = Title.title(Component.text("Deserters will be beheaded!", NamedTextColor.DARK_RED),
                    Component.text("Turn back now!", NamedTextColor.RED), times);
            player.showTitle(title);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> onCooldown.remove(uuid), 70);
        }
    }

    /**
     * Kill the player.
     * @param player The player
     */
    private void killPlayer(Player player) {
        Bukkit.getScheduler().runTask(Main.plugin, () -> {
            if (player.getHealth() > 0)
                player.setHealth(0);
        });
    }
}
