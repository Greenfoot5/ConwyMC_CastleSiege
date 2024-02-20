package me.huntifi.castlesiege.events.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.data_types.MapBorder;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Check if the player should be held accountable
            Player player = e.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                    || InCombat.isPlayerInLobby(player.getUniqueId()) || DuelCommand.isDueling(player))
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
            player.sendTitle(ChatColor.DARK_RED + "Deserters will be beheaded!",
                    ChatColor.RED + "Turn back now!", 20, 40, 10);
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
