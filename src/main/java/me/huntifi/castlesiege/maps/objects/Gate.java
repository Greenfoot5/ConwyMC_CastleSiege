package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.maps.events.RamEvent;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
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
    private Ram ram;

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
     * @return The current health of the gate
     */
    public int getHealth() {
        return this.health;
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

    private void gateBreached() {
        if (!getName().isEmpty()) {
            Messenger.broadcastWarning(getName() + " has been breached!");
        }

        World world = Bukkit.getWorld(MapController.getCurrentMap().worldName);
        if (world == null)
            return;

        SchematicSpawner.spawnSchematic(schematicLocation.toLocation(world), schematicName);
        new BukkitRunnable() {
            @Override
            public void run() {
                world.playSound(breachSoundLocation.toLocation(world),
                        Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );
            }
        }.runTask(Main.plugin);
        isBreached = true;
    }

    public boolean isBreached() {
        return isBreached;
    }

    public boolean canBreach(UUID uuid) {
        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        return flagName.isEmpty() || (flag != null
                && !Objects.equals(TeamController.getTeam(uuid).name, flag.getCurrentOwners()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Make sure the gate is on the correct map and has not been breached yet
        if (Objects.equals(mapName, MapController.getCurrentMap().name) && !isBreached) {

            // Check the player is left-clicking and the gate isn't friendly
            Player player = event.getPlayer();
            if (canBreach(player.getUniqueId()) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
                assert event.getClickedBlock() != null;
                if (isGateBlock(event.getClickedBlock())) {
                    if (!player.isSprinting()) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You need to sprint in order to bash the gate!"));
                        return;
                    }

                    if (!recentHitters.contains(player.getUniqueId())) {
                        recentHitters.add(player.getUniqueId());

                        RamEvent ramEvent = new RamEvent(getName(), getDamage(player.getUniqueId()), getHealth(), player.getUniqueId());
                        Bukkit.getPluginManager().callEvent(ramEvent);
                        if (ramEvent.isCancelled()) {
                            return;
                        }

                        dealDamage(ramEvent.getPlayerUUIDs(), ramEvent.getDamageDealt());
                        for (UUID uuid : ramEvent.getPlayerUUIDs()) {
                            UpdateStats.addSupports(uuid, 1);
                        }


                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                recentHitters.remove(player.getUniqueId());
                            }
                        }.runTaskLater(Main.plugin, 20);
                    }
                }
            }
        }
    }

    /**
     * Deal damage to this gate. Breach the gate when no health remains.
     * @param players The UUIDs of the players attacking the gate
     * @param damage The amount of damage to deal
     */
    public void dealDamage(Collection<UUID> players, int damage) {
        if (health > damage) {
            health -= damage;

            // Show all involved players the amount of remaining health
            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null)
                    continue;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        String.format("%s%sGate Health: %d", ChatColor.GRAY, ChatColor.BOLD, health)));
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    // Play an explosion sound for hitting the gate
                    World world = Bukkit.getWorld(MapController.getCurrentMap().worldName);
                    assert world != null;
                    world.playSound(breachSoundLocation.toLocation(world), Sound.ENTITY_GENERIC_EXPLODE, 2, 1);
                }
            }.runTask(Main.plugin);

        } else {
            gateBreached();
        }
    }

    /**
     * Set this gate's ram.
     * @param ram The ram object belonging to this gate
     */
    public void setRam(Ram ram) {
        this.ram = ram;
    }

    /**
     * Get this gate's ram.
     * @return The ram object belonging to this gate, null if none exists
     */
    public Ram getRam() {
        return this.ram;
    }

    private int getDamage(UUID uuid) {
        int teamSize = TeamController.getTeam(uuid).getTeamSize();
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
