package me.huntifi.castlesiege.data_types;

import org.bukkit.util.Vector;
import me.huntifi.conwymc.data_types.Tuple;

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
