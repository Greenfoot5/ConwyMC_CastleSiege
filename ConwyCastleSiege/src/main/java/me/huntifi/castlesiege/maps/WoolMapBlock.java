package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.objects.Flag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a single block on the wool map in a lobby
 */
public class WoolMapBlock {
    public String flagName;
    public Location blockLocation;
    public Location signLocation;

    /**
     * Attempts to spawn a player at a flag
     *
     * @param uuid The uuid of the player
     */
    public void spawnPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {

        Team team = MapController.getCurrentMap().getTeam(uuid);
        Flag flag = MapController.getCurrentMap().getFlag(flagName);

        if (team != null && team.hasPlayer(uuid)) {
            if (!Objects.equals(flag.getCurrentOwners(), team.name)) {
                Messenger.sendActionError("Your team does not own this flag at the moment.", player);
            } else if (flag.underAttack()) {
                Messenger.sendActionError("You can't spawn here. This flag is under attack!", player);
            } else if (Kit.equippedKits.get(uuid) == null) {
                Messenger.sendError("You can't join the battlefield without a kit/class!", player);
                Messenger.sendError("Choose a kit/class with the command " + ChatColor.RED + "/kit" + ChatColor.DARK_RED + "!", player);
            } else {
                Bukkit.getScheduler().runTask(Main.plugin, () -> {
                    // Remove mount
                    if (player.isInsideVehicle())
                        Objects.requireNonNull(player.getVehicle()).remove();
                    // Set kit items
                    Kit.equippedKits.get(uuid).setItems(uuid);
                    // Spawn player
                    player.teleport(flag.spawnPoint);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(flag.getSpawnMessage()));
                });
                InCombat.playerSpawned(uuid);
            }
        }
      });
    }
}
