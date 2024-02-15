package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.objects.Core;
import me.huntifi.castlesiege.maps.objects.Flag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a single block on the wool map in a lobby
 */
public class WoolMapBlock {
    public String flagName;
    public String coreName;
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
            Team team = TeamController.getTeam(uuid);
            Kit kit = Kit.equippedKits.get(uuid);
                if (MapController.getCurrentMap() instanceof CoreMap) {
                    CoreMap coreMap = (CoreMap) MapController.getCurrentMap();
                    Core core = coreMap.getCore(coreName);
                    if (team != null && team.hasPlayer(uuid)) {
                        if (!Objects.equals(core.getOwners(), team.name)) {
                            Messenger.sendActionError("Your team does not own this core.", player);
                            return;
                        } else if (kit == null) {
                            Messenger.sendError("You can't join the battlefield without a kit/class!", player);
                            Messenger.sendError("Choose a kit/class with the command " + ChatColor.RED + "/kit" + ChatColor.DARK_RED + "!", player);
                            return;
                        } else {
                            Bukkit.getScheduler().runTask(Main.plugin, () -> {
                                // Remove mount
                                if (player.isInsideVehicle())
                                    Objects.requireNonNull(player.getVehicle()).remove();
                                // Set kit items
                                Kit.equippedKits.get(uuid).setItems(uuid, true);
                                // Spawn player
                                player.teleport(core.getSpawnPoint(team.name));
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(core.getSpawnMessage()));
                                // Remove mount
                                if (player.isInsideVehicle()) {
                                    Objects.requireNonNull(player.getVehicle()).remove();
                                }
                                InCombat.playerSpawned(uuid);
                            });
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(core.getSpawnMessage()));
                        }
                    }
                }
                if (MapController.getCurrentMap().getFlag(flagName) == null) {
                    return;
                }
                Flag flag = MapController.getCurrentMap().getFlag(flagName);
                if (team != null && team.hasPlayer(uuid)) {
                    if (!Objects.equals(flag.getCurrentOwners(), team.name)) {
                        Messenger.sendActionError("Your team does not own this flag at the moment.", player);
                    } else if (flag.underAttack()) {
                        Messenger.sendActionError("You can't spawn here. This flag is under attack!", player);
                    } else if (kit == null) {
                        Messenger.sendError("You can't join the battlefield without a kit/class!", player);
                        Messenger.sendError("Choose a kit/class with the command " + ChatColor.RED + "/kit" + ChatColor.DARK_RED + "!", player);
                    } else {
                        Bukkit.getScheduler().runTask(Main.plugin, () -> {
                            // Remove mount
                            if (player.isInsideVehicle())
                                Objects.requireNonNull(player.getVehicle()).remove();
                            // Set kit items
                            Kit.equippedKits.get(uuid).setItems(uuid, true);
                            // Spawn player
                            player.teleport(flag.getSpawnPoint(team.name));
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(flag.getSpawnMessage()));
                            // Remove mount
                            if (player.isInsideVehicle()) {
                                Objects.requireNonNull(player.getVehicle()).remove();
                            }
                            InCombat.playerSpawned(uuid);
                        });
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(flag.getSpawnMessage()));
                    }
                }
        });
    }
}
