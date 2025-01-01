package me.greenfoot5.castlesiege.data_types;

import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * A type of frame utilising
 */
public class SchematicFrame extends Frame{

    public final List<Tuple<String, Vector>> schematics;

    /**
     * Creates a new Schematic Frame
     */
    public SchematicFrame() {
        super();
        schematics = new ArrayList<>();
    }
}
