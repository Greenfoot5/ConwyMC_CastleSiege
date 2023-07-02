package me.huntifi.castlesiege.data_types;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * A type of frame utilising
 */
public class SchematicFrame extends Frame{

    public final List<Tuple<String, Vector>> schematics;


    public SchematicFrame() {
        super();
        schematics = new ArrayList<>();
    }
}
