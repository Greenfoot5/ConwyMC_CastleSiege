package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a destructible/rammable gate
 */
public class Gate implements Listener {
    private final String name;
    private final String flagName;

    private final Vector min;
    private final Vector max;
    private final String schematicName;
    private Vector schematicLocation;
    private Vector breachSoundLocation;

    private int health;
    private boolean isBreached;

    private final ArrayList<UUID> recentHitters = new ArrayList<>();

    /**
     * Creates a new gate
     * @param displayName The display name of the gate
     * @param flagName The name of the flag the gate belongs to. Stops friendlies from breaking the gate
     * @param min The minimum position of the part of the gate you can hit
     * @param max The maximum position of the part of the gate you can hit
     * @param schematicName The name of the schematic to spawn when the gate is breached
     * @param startingHealth The starting health of the gate
     */
    public Gate(String displayName, String flagName, Vector min, Vector max, String schematicName, int startingHealth) {
        this.name = displayName;
        this.flagName = flagName;
        this.min = min;
        this.max = max;
        this.schematicName = schematicName;
        this.health = startingHealth;
        isBreached = false;
    }

    /**
     * Sets the breach sound location
     * @param location The vector of the location
     */
    public void setBreachSoundLocation(Vector location) {
        breachSoundLocation = location;
    }
    public void setSchematicLocation(Vector location) {
        schematicLocation = location;
    }

    private boolean isGateBlock(Block block) {
        Location location = block.getLocation();
        if (min.getX() <= block.getX() && block.getX() <= max.getX()) {
            if (min.getY() <= block.getY() && block.getY() <= max.getY()) {
                return min.getZ() <= block.getZ() && block.getZ() <= max.getZ();
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        // Make sure the player is playing, and the flag is on the correct map
        if (flag != null) {

            // Check the player is left-clicking and the gate isn't friendly
            if(!Objects.equals(MapController.getCurrentMap().getTeam(player.getUniqueId()).name, flag.currentOwners)
                    && event.getAction() == Action.LEFT_CLICK_BLOCK) {

                Location soundLoc = Objects.requireNonNull(event.getClickedBlock()).getLocation();

                if (isGateBlock(event.getClickedBlock())) {

                    if (!player.isSprinting()) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You need to sprint in order to bash the gate!"));
                        return;
                    }

                    // Check there's at least 2hp left
                    if (health > 2) {

                        if (!recentHitters.contains(player.getUniqueId())) {

                            recentHitters.add(player.getUniqueId());

                            health -= 2;

                            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
                                UpdateStats.addSupports(player.getUniqueId(), 0.5);
                            });

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + health));

                            Objects.requireNonNull(soundLoc.getWorld()).playSound(soundLoc, Sound.ENTITY_GENERIC_EXPLODE , 2, 1 );

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    recentHitters.remove(player.getUniqueId());
                                }
                            }.runTaskLater(Main.plugin, 20);
                        }
                    } else {
                        if (!isBreached) {
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(ChatColor.RED + name + "has been breached!");
                            }

                            try {
                                SchematicSpawner.spawnSchematic(schematicLocation.toLocation(
                                        Objects.requireNonNull(soundLoc.getWorld())), schematicName, soundLoc.getWorld().getName());
                            } catch (WorldEditException e) {
                                e.printStackTrace();
                            }

                            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(
                                    breachSoundLocation.toLocation(soundLoc.getWorld()), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );
                            isBreached = true;
                        }
                    }
                }
            }
        }
    }
}
