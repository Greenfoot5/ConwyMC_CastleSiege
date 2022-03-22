package me.huntifi.castlesiege.data_types;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frame implements ConfigurationSerializable {
    public List<Vector> primary_blocks;
    public List<Vector> secondary_blocks;

    @Override
    public @NotNull Map<String, Object> serialize() {
        java.util.Map<String, Object> map = new HashMap<>();
        map.put("primary_blocks", primary_blocks);
        map.put("secondary_blocks", secondary_blocks);
        return map;
    }

    public static @NotNull Frame deserialize(@NotNull java.util.Map<String, Object> map) {
        Frame frame = new Frame();
        frame.primary_blocks = (List<Vector>) map.get("primary_blocks");
        frame.secondary_blocks = (List<Vector>) map.get("secondary_blocks");
        return frame;
    }
}
