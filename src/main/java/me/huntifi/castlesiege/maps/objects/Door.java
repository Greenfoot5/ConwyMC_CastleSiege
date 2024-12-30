package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import me.huntifi.conwymc.data_types.Tuple;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a door
 */
public abstract class Door implements Listener {
    protected final String flagName;
    protected final Location centre;
    // Location, Closed/Open
    protected final Tuple<String, String> schematicNames;
    protected final Tuple<Sound, Sound> sounds;
    protected final int timer;
    protected final AtomicInteger openCounts = new AtomicInteger(0);

    /**
     * Creates a new door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param schematics The blocks that make up the door
     * @param sounds The sounds to play when the door is closed/opened
     * @param timer How long the door stays open before automatically closing in ticks
     */
    public Door(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds,
                int timer) {
        this.flagName = flagName;
        this.centre = centre;
        schematicNames = schematics;
        this.sounds = sounds;
        this.timer = timer;
    }

    /**
     * Handles the opening and then closing of the door
     * @param event Called when a player interacts with an object or air
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isCorrectInteraction(event) && isControlled(event)) {
            activate(event);
        }
    }

    /**
     * Handles the door opening mechanism being activated.
     * @param event Called when a player interacts with an object or air
     */
    protected void activate(PlayerInteractEvent event) {
        // Open the door if it is currently closed
        if (openCounts.getAndIncrement() == 0)
            open();

        // Close the door after the open time if no other activation is keeping it open
        Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
            if (openCounts.get() > 0 && openCounts.decrementAndGet() == 0)
                close();
        }, timer);
    }

    /**
     * Opens the door
     */
    protected void open() {
        SchematicSpawner.spawnSchematic(centre, schematicNames.getSecond());
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getSecond(), 3, 1);
    }

    /**
     * Closes the door
     */
    protected void close() {
        SchematicSpawner.spawnSchematic(centre, schematicNames.getFirst());
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getFirst(), 3, 1);
    }

    /**
     * Checks whether a correct interaction is performed to open the door
     * @param event Called when a player interacts with an object or air
     * @return Whether a correct interaction is performed to open the door
     */
    protected boolean isCorrectInteraction(PlayerInteractEvent event) {
        return isCorrectAction(event.getAction()) && isCorrectBlockType(event.getClickedBlock());
    }

    /**
     * Checks whether the flag to which the door belongs is under control of the player's team.
     * @param event Called when a player interacts with an object or air
     * @return Whether the door is controlled by the player's team
     */
    private boolean isControlled(PlayerInteractEvent event) {
        // The door does not belong to a flag
        if (Objects.equals(flagName, MapController.getCurrentMap().name))
            return true;

        // The door belongs to a flag
        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        Player player = event.getPlayer();
        if (!Objects.equals(flag.getCurrentOwners(), TeamController.getTeam(player.getUniqueId()).getName())) {
            Messenger.sendActionError(
                    "Your team does not control this door. You need to capture " + flagName + " first!",
                    player
            );
            event.setCancelled(true);
            return false;
        }
        return true;
    }

    /**
     * Checks whether a correct action was performed to open the door.
     * @param action The action
     * @return Whether an incorrect action was performed
     */
    protected abstract boolean isCorrectAction(Action action);

    /**
     * Checks whether a correct block type was interacted with to open the door.
     * @param block The block
     * @return Whether an incorrect block type was interacted with
     */
    protected abstract boolean isCorrectBlockType(Block block);
}
