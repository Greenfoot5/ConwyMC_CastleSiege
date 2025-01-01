package me.greenfoot5.castlesiege.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.greenfoot5.castlesiege.advancements.displays.HiddenNodeDisplay;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplay;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplayTypes;
import me.greenfoot5.castlesiege.advancements.displays.ParentGrantedNodeDisplay;
import me.greenfoot5.castlesiege.advancements.displays.ShownNodeDisplay;
import me.greenfoot5.castlesiege.advancements.displays.VanillaNodeDisplay;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Material;

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
    // Display Type
    private NodeDisplayTypes displayType;

    // New root node
    public AdvancementNode(Material icon, AdvancementFrameType frameType, String title, String description, NodeDisplayTypes displayType) {
        this(icon, frameType, title, description, null, displayType);
    }

    // New node with parent
    public AdvancementNode(Material icon, AdvancementFrameType frameType, String title, String description, String parent) {
        this.icon = icon;
        this.frameType = frameType;
        this.title = title;
        this.key = NodeDisplay.cleanKey(title);
        this.description = description;

        this.parent = NodeDisplay.cleanKey(parent);
        this.children = new ArrayList<>();

        this.displayType = null;
    }

    // New node with parent
    public AdvancementNode(Material icon, AdvancementFrameType frameType, String title, String description, String parent, NodeDisplayTypes displayType) {
        this.icon = icon;
        this.frameType = frameType;
        this.title = title;
        this.key = NodeDisplay.cleanKey(title);
        this.description = description;

        this.parent = NodeDisplay.cleanKey(parent);
        this.children = new ArrayList<>();

        this.displayType = displayType;
    }

    // Adds a child based on their parent
    public void addChild(AdvancementNode child) {
        if (child.parent.equalsIgnoreCase(key)) {
            if (child.displayType == null) {
                child.displayType = displayType;
            }
            this.children.add(child);
        } else {
            find(child.parent).addChild(child);
        }
    }

    // Gets a node (either itself or child) based on key
    public AdvancementNode find(String key) {
        key = NodeDisplay.cleanKey(key);
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
        NodeDisplay display;
        switch (displayType) {
            case Hidden:
                display = new HiddenNodeDisplay(icon, frameType, true, frameType == AdvancementFrameType.CHALLENGE, x, y, title, description, parent);
                break;
            case ParentGranted:
                display = new ParentGrantedNodeDisplay(icon, frameType, true, frameType == AdvancementFrameType.CHALLENGE, x, y, title, description, parent);
                break;
            case Vanilla:
                display = new VanillaNodeDisplay(icon, frameType, true, frameType == AdvancementFrameType.CHALLENGE, x, y, title, description, parent);
                break;
            case Shown:
                display = new ShownNodeDisplay(icon, frameType, true, frameType == AdvancementFrameType.CHALLENGE, x, y, title, description, parent);
                break;
            default:
                throw new RuntimeException("Unknown advancement display type for " + title);
        }
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
                if (displays.get(key).getParentKey().equals(this.key)) {
                    advancements.put(key, new BaseAdvancement(key, displays.get(key), root));
                }
                // Parent is an existing advancement
                else if (advancements.containsKey(displays.get(key).getParentKey())) {
                    advancements.put(key, new BaseAdvancement(key, displays.get(key), advancements.get(displays.get(key).getParentKey())));
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
}
