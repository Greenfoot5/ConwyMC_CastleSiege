package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * Represents a door
 */
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

    /**
     * Opens the door
     */
    protected void open() {
        for (Tuple<Vector, Tuple<Material, Material>> tuple : doorBlocks) {
            // Set the block
            Location location = tuple.getFirst().toLocation(Objects.requireNonNull(centre.getWorld()));
            location.getBlock().setType(tuple.getSecond().getSecond());

            // If it's a fence, we'll need to set all the faces correctly.
            BlockData data = location.getBlock().getBlockData();
            if (data instanceof MultipleFacing) {
                location.getBlock().setBlockData(calculateFacingEdges((MultipleFacing) data, location));
            }
        }
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getSecond(), 3, 1);
    }

    /**
     * Closes the door
     */
    protected void close() {
        for (Tuple<Vector, Tuple<Material, Material>> tuple : doorBlocks) {
            // Set the block
            Location location = tuple.getFirst().toLocation(Objects.requireNonNull(centre.getWorld()));
            location.getBlock().setType(tuple.getSecond().getFirst());

            // If it's a fence, we'll need to set all the faces correctly.
            BlockData data = location.getBlock().getBlockData();
            if (data instanceof MultipleFacing) {
                location.getBlock().setBlockData(calculateFacingEdges((MultipleFacing) data, location));
            }
        }
        Objects.requireNonNull(centre.getWorld()).playSound(centre, sounds.getFirst(), 3, 1);
    }

    /**
     * *Should* fix edge connections for multi-facing blocks (i.e. fences)
     * @param data The MultipleFacing block data for the block
     * @param blockLocation The location of the block
     * @return The data with the connections fixed
     * TODO - This doesn't actually sort the connections
     */
    protected MultipleFacing calculateFacingEdges(MultipleFacing data, Location blockLocation) {
        for (BlockFace face : data.getAllowedFaces()) {
            System.out.println(face);
            Location checkLoc = blockLocation.add(face.getDirection());
            if (checkLoc.getBlock().getBlockData().getMaterial().isOccluding()) {
                data.setFace(face, true);
            }
        }
        return data;
    }
}
