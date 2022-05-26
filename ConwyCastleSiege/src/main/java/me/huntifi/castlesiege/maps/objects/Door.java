package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Door implements Listener {
    protected final String flagName;
    protected final Location centre;
    // Location, Closed/Open
    protected final Tuple<Vector, Tuple<Material, Material>>[] doorBlocks;
    protected final Tuple<Sound, Sound> sounds;
    protected final int timer;

    /**
     * Creates a new door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param blocks The blocks that make up the door
     * @param sounds The sounds to play when the door is closed/opened
     */
    public Door(String flagName, Location centre, Tuple<Vector, Tuple<Material, Material>>[] blocks, Tuple<Sound, Sound> sounds,
                int timer) {
        this.flagName = flagName;
        this.centre = centre;
        doorBlocks = blocks;
        this.sounds = sounds;
        this.timer = timer;
    }

    public void open() {
        for (Tuple<Vector, Tuple<Material, Material>> tuple : doorBlocks) {
            tuple.getFirst().toLocation(Objects.requireNonNull(centre.getWorld())).getBlock().setType(tuple.getSecond().getSecond());
        }
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getSecond(), 3, 1);
    }

    public void close() {
        for (Tuple<Vector, Tuple<Material, Material>> tuple : doorBlocks) {
            tuple.getFirst().toLocation(Objects.requireNonNull(centre.getWorld())).getBlock().setType(tuple.getSecond().getFirst());
        }
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getFirst(), 3, 1);
    }
}
