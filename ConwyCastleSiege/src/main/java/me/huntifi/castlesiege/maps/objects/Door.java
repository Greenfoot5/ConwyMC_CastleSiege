package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

import java.util.Objects;

/**
 * Represents a door
 */
public class Door implements Listener {
    protected final String flagName;
    protected final Location centre;
    // Location, Closed/Open
    protected final Tuple<String, String> schematicNames;
    protected final Tuple<Sound, Sound> sounds;
    protected final int timer;

    /**
     * Creates a new door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param blocks The blocks that make up the door
     * @param sounds The sounds to play when the door is closed/opened
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
     * Opens the door
     */
    protected void open() throws WorldEditException {
        SchematicSpawner.spawnSchematic(centre, schematicNames.getSecond(), centre.getWorld().getName());
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getSecond(), 3, 1);
    }

    /**
     * Closes the door
     */
    protected void close() throws WorldEditException {
        SchematicSpawner.spawnSchematic(centre, schematicNames.getFirst(), MapController.getCurrentMap().worldName);
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getFirst(), 3, 1);
    }
}
