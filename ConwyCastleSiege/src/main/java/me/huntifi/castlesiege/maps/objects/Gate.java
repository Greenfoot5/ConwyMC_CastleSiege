package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.util.Direction;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
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
    private String flagName;
    private String mapName;

    private Vector min;
    private Vector max;
    private String schematicName;
    private Vector schematicLocation;
    private Vector breachSoundLocation;

    private int health;
    private boolean isBreached;

    private final ArrayList<UUID> recentHitters = new ArrayList<>();

    // Ram data
    private Direction direction = Direction.WEST;
    private int ramIndex = 0;

    /**
     * Creates a new gate
     * @param displayName The display name of the gate
     */
    public Gate(String displayName) {
        this.name = displayName;
        isBreached = false;
    }

    public String getName() {
        return name;
    }

    /**
     * @param flagName The name of the flag the gate belongs to. Stops friendlies from breaking the gate
     */
    public void setFlagName(String flagName, String mapName) {
        this.flagName = flagName;
        this.mapName = mapName;
    }

    /**
     * @param min The minimum position of the part of the gate you can hit
     * @param max The maximum position of the part of the gate you can hit
     */
    public void setHitBox(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @param schematicName The name of the schematic to spawn when the gate is breached
     * @param location the vector of the location
     */
    public void setSchematic(String schematicName, Vector location) {
        this.schematicName = schematicName;
        this.schematicLocation = location;
    }

    /**
     * @param health The current health of the gate
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Sets the breach sound location
     * @param location The vector of the location
     */
    public void setBreachSoundLocation(Vector location) {
        breachSoundLocation = location;
    }

    private boolean isGateBlock(Block block) {
        if (min.getX() <= block.getX() && block.getX() <= max.getX()) {
            if (min.getY() <= block.getY() && block.getY() <= max.getY()) {
                return min.getZ() <= block.getZ() && block.getZ() <= max.getZ();
            }
        }
        return false;
    }

    private void gateBreached(World world) {
        Messenger.broadcastWarning(getName() + " has been breached!");

        try {
            SchematicSpawner.spawnSchematic(schematicLocation.toLocation(
                    Objects.requireNonNull(world)), schematicName, world.getName());
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(world).playSound(
                breachSoundLocation.toLocation(world), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );
        isBreached = true;

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        // Make sure the player is playing, and the flag is on the correct map
        if (flag != null && Objects.equals(mapName, MapController.getCurrentMap().name)) {

            // Check the player is left-clicking and the gate isn't friendly
            if(!Objects.equals(MapController.getCurrentMap().getTeam(player.getUniqueId()).name, flag.getCurrentOwners())
                    && event.getAction() == Action.LEFT_CLICK_BLOCK) {

                Location soundLoc = Objects.requireNonNull(event.getClickedBlock()).getLocation();

                if (isGateBlock(event.getClickedBlock())) {
                    if (!player.isSprinting() && !isBreached) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You need to sprint in order to bash the gate!"));
                        return;
                    }

                    int damage = getDamage(player.getUniqueId());

                    // Check there's at least 2hp left
                    if (health > damage) {

                        if (!recentHitters.contains(player.getUniqueId())) {

                            recentHitters.add(player.getUniqueId());

                            health -= damage;

                            Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> UpdateStats.addSupports(player.getUniqueId(), 0.5));

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
                            gateBreached(soundLoc.getWorld());
                        }
                    }
                }
            }
        }
    }

    private int getDamage(UUID uuid) {
        int teamSize = MapController.getCurrentMap().getTeam(uuid).getTeamSize();
        if (teamSize <= 3) {
            return 8;
        } else if (teamSize <= 6) {
            return 6;
        } else if (teamSize <= 10) {
            return 4;
        } else if (teamSize <= 15) {
            return 3;
        } else {
            return 2;
        }
    }
}
