package me.huntifi.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.huntifi.conwymc.advancements.AdventureAdvancementDisplay;
import me.huntifi.conwymc.data_types.Tuple;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvancementNode {
    // AdvancementDisplay
    private final Material icon;
    private final AdvancementFrameType frameType;
    private final String title;
    private final String key;
    private final String description;
    // DAG Node
    private final String parent;
    private final List<AdvancementNode> children;

    // New root node
    public AdvancementNode(Material icon, AdvancementFrameType frameType, String title, String description) {
        this(icon, frameType, title, description, null);
    }

    // New node with parent
    public AdvancementNode(Material icon, AdvancementFrameType frameType, String title, String description, String parent) {
        this.icon = icon;
        this.frameType = frameType;
        this.title = title;
        this.key = cleanKey(title);
        this.description = description;

        this.parent = cleanKey(parent);
        this.children = new ArrayList<>();
    }

    // Adds a child based on their parent
    public void addChild(AdvancementNode child) {
        if (child.parent.equalsIgnoreCase(key))
            this.children.add(child);
        else
            find(child.parent).addChild(child);
    }

    // Gets a node (either itself or child) based on key
    public AdvancementNode find(String key) {
        key = cleanKey(key);
        if (this.key.equalsIgnoreCase(key)) {
            return this;
        }
        // Possibly in a child
        for (AdvancementNode child : children) {
            if (child.key.equalsIgnoreCase(key))
                return child;
            else {
                AdvancementNode found = child.find(key);
                if (found != null) return found;
            }
        }
        return null;
    }

    // Gets the displays for the current node & children
    private HashMap<String, NodeDisplay> getDisplays() {
        return generateDisplays(0, 0).getFirst();
    }

    // Generates the displays for each of the nodes in the list
    private Tuple<HashMap<String, NodeDisplay>, Integer> generateDisplays(int x, int min_y) {
        int y_size = 0;
        int y = 0;
        HashMap<String, NodeDisplay> displays = new HashMap<>();
        // Base Case
        if (children.isEmpty()) {
            y = y_size + min_y;
            y_size += 1;
        } else {
            for (AdvancementNode node : this.children) {
                Tuple<HashMap<String, NodeDisplay>, Integer> value = node.generateDisplays(x + 1, min_y + y_size);
                displays.putAll(value.getFirst());
                y_size += value.getSecond();
            }
            y = (int) Math.round(min_y + (y_size - 1.0) / 2);
        }
        NodeDisplay display = new NodeDisplay(icon, frameType, true,
                frameType == AdvancementFrameType.CHALLENGE, x, y, title, description, parent);
        displays.put(key, display);
        return new Tuple<>(displays, y_size);
    }

    public Tuple<RootAdvancement, BaseAdvancement[]> asAdvancementList(AdvancementTab tab, String background) {
        HashMap<String, NodeDisplay> displays = getDisplays();

        RootAdvancement root = new RootAdvancement(tab, key, displays.get(key), background);
        displays.remove(key);

        HashMap<String, BaseAdvancement> advancements = new HashMap<>();
        int i = 0; // Prevent ∞ loop
        while (!displays.isEmpty() && i < 10) {
            HashMap<String, NodeDisplay> retryDisplays = new HashMap<>();
            // Add the advancements
            for (String key : displays.keySet()) {
                // Parent is root
                if (displays.get(key).parentKey.equals(this.key)) {
                    advancements.put(key, new BaseAdvancement(key, displays.get(key), root));
                }
                // Parent is an existing advancement
                else if (advancements.containsKey(displays.get(key).parentKey)) {
                    advancements.put(key, new BaseAdvancement(key, displays.get(key), advancements.get(displays.get(key).parentKey)));
                }
                // Parent doesn't exist yet
                else {
                    retryDisplays.put(key, displays.get(key));
                }
            }
            displays = retryDisplays;
            i++;
        }
        return new Tuple<>(root, advancements.values().toArray(new BaseAdvancement[0]));
    }

    /**
     * Remove invalid characters for a key
     * @param input The key to clean
     * @return A cleaned key allowing [a-zA-Z0-9|._\-\/]
     */
    private String cleanKey(String input) {
        if (input == null) return null;
        // Remove MiniMessage
        input = input.toLowerCase().replaceAll("<.*?>", "");
        // Remove illegal characters
        return input.toLowerCase().replaceAll("[^a-zA-Z0-9|._\\-/]","");
    }

    private class NodeDisplay extends AdventureAdvancementDisplay {
        public String parentKey;

        public NodeDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y, @NotNull String title, @NotNull String description, String parent) {
            super(icon, frame, showToast, announceChat, x, y, title, description);
            this.parentKey = cleanKey(parent);
        }
    }
}
