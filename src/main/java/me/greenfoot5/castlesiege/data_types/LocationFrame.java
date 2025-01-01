package me.greenfoot5.castlesiege.data_types;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single frame of blockAnimation
 */
public class LocationFrame extends Frame {
    public final List<Vector> primary_blocks;
    public final List<Vector> secondary_blocks;
    public final List<Vector> air;

    /**
     * Creates an empty frame
     */
    public LocationFrame() {
        super();
        primary_blocks = new ArrayList<>();
        secondary_blocks = new ArrayList<>();
        air = new ArrayList<>();
    }
}
