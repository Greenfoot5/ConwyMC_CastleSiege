package me.huntifi.castlesiege.data_types;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single frame of animation
 */
public class Frame {
    public List<Vector> primary_blocks;
    public List<Vector> secondary_blocks;
    public List<Vector> air;

    /**
     * Creates an empty frame
     */
    public Frame() {
        primary_blocks = new ArrayList<>();
        secondary_blocks = new ArrayList<>();
        air = new ArrayList<>();
    }
}
