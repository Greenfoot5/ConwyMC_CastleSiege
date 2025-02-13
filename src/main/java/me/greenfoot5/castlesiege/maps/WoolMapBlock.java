package me.greenfoot5.castlesiege.maps;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.objects.Core;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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
        if (player == null || !TeamController.isPlaying(player)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            Team team = TeamController.getTeam(uuid);
            Kit kit = Kit.equippedKits.get(uuid);

            if (MapController.getCurrentMap() instanceof CoreMap coreMap) {
                Core core = coreMap.getCore(coreName);
                if (team != null && team.hasPlayer(uuid)) {
                    if (!Objects.equals(core.getOwners(), team.getName())) {
                        Messenger.sendActionError("Your team does not own this core.", player);
                        return;

                    } else if (kit == null) {
                        Messenger.sendError("You can't join the battlefield without a kit/class!", player);
                        Messenger.sendError("Choose a kit/class with the command <red>/kit</red>!", player);
                        return;

                    } else {
                        Bukkit.getScheduler().runTask(Main.plugin, () -> {
                            // Remove mount
                            if (player.isInsideVehicle())
                                Objects.requireNonNull(player.getVehicle()).remove();
                            // Set kit items
                            Kit.equippedKits.get(uuid).setItems(true);
                            // Spawn player
                            player.teleport(core.getSpawnPoint());
                            // Remove mount
                            if (player.isInsideVehicle()) {
                                Objects.requireNonNull(player.getVehicle()).remove();
                            }
                            InCombat.playerSpawned(uuid);
                        });
                        Messenger.sendActionSpawn(core.name, team.primaryChatColor, team.secondaryChatColor, player);
                    }
                }
            }

            Flag flag = MapController.getCurrentMap().getFlag(flagName);

            if (flag == null) {
                return;
            }

            if (team != null && TeamController.getTeam(uuid).getName().equals(team.getName())) {
                if (!Objects.equals(flag.getCurrentOwners(), team.getName())) {
                    Messenger.sendActionError("Your team does not own this flag at the moment.", player);

                } else if (flag.underAttack()) {
                    Messenger.sendActionError("You can't spawn here. This flag is under attack!", player);

                } else if (kit == null) {
                    Messenger.sendError("You can't join the battlefield without a kit/class!", player);
                    Messenger.sendError("Choose a kit/class with the command <red>/kit</red>!", player);

                } else {
                    Bukkit.getScheduler().runTask(Main.plugin, () -> {
                        // Remove mount
                        if (player.isInsideVehicle())
                            Objects.requireNonNull(player.getVehicle()).remove();
                        // Set kit items
                        Kit.equippedKits.get(uuid).setItems(true);
                        // Spawn player
                        player.teleport(flag.getSpawnPoint(team.getName()));
                        // Remove mount
                        if (player.isInsideVehicle()) {
                            Objects.requireNonNull(player.getVehicle()).remove();
                        }
                        InCombat.playerSpawned(uuid);
                        player.getWorld().playSound(flag.getSpawnPoint(team.getName()), Sound.BLOCK_ENCHANTMENT_TABLE_USE , 1, 1.1f);
                    });
                    Messenger.sendActionSpawn(flagName, team.primaryChatColor, team.secondaryChatColor, player);
                }
            }
        });
    }
}
