package me.greenfoot5.advancements.api.nodes;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.displays.NodeDisplay;
import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A node allowing automatic display of advancements
 */
public class AdvancementNode {
    private final String key;

    // AdvancementDisplay Required
    private final ItemStack icon;
    private final AdvancementFrameType frameType;
    private final String title;
    private final String description;

    private final String compactDescription;

    // DAG Node
    private final String parent;
    private final List<AdvancementNode> children;

    // Additionals
    private final int maxProgress;

    // References
    StandardAdvancement advancement;
    NodeDisplay display;

    protected AdvancementNode(AdvancementNodeBuilder builder) {
        this.key = builder.key;
        this.parent = builder.parent;
        this.children = new ArrayList<>();

        this.icon = builder.icon;
        this.frameType = builder.frameType;
        this.title = builder.title;
        this.description = builder.description;

        this.compactDescription = builder.compactDescription;

        this.maxProgress = builder.maxProgress;
    }

    /**
     * Gets the AdvancementKey
     * @param tab The tab to get the key using
     * @return The key for the advancement
     */
    public AdvancementKey getKey(AdvancementTab tab) {
        return new AdvancementKey(tab.getNamespace(), key);
    }

    /**
     * Adds a new child to the node
     * @param child The child node to add
     */
    public void addChild(AdvancementNode child) {
        if (child.parent.equalsIgnoreCase(key)) {
            this.children.add(child);
        } else {
            find(child.parent).addChild(child);
        }
    }

    /**
     * Finds a node in the node or it's parents
     * @param key The key to use when searching for the node
     * @return The first node with the specified key
     */
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


    private HashMap<String, NodeDisplay> getDisplays() {
        return generateDisplays(0, 0).getFirst();
    }

    // Generates the displays for each of the nodes in the list
    private Tuple<HashMap<String, NodeDisplay>, Integer> generateDisplays(int x, int min_y) {
        int y_size = 0;
        int y;
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
        // Other Display Options
        NodeDisplay display = new NodeDisplay(icon, frameType, false, false, x, y, title, description, maxProgress);
        display.setParentKey(parent);
        displays.put(key, display);
        return new Tuple<>(displays, y_size);
    }

    /**
     * Creates advancements from the node
     * @param tab The tab use create the advancements in
     * @param background The background image to use
     * @return The created advancements
     */
    public Tuple<RootAdvancement, StandardAdvancement[]> asAdvancementList(AdvancementTab tab, String background) {
        HashMap<String, NodeDisplay> displays = getDisplays();

        RootAdvancement root = new RootAdvancement(tab, key, displays.get(key), background);
        displays.remove(key);

        HashMap<String, StandardAdvancement> advancements = new HashMap<>();

        int i = 0; // Prevent ∞ loop
        while (!displays.isEmpty() && i < 10) {
            HashMap<String, NodeDisplay> retryDisplays = new HashMap<>();
            // Add the advancements
            for (String key : displays.keySet()) {
                // Parent is root
                if (displays.get(key).getParentKey().equals(this.key)) {
                    advancements.put(key, find(key).getAdvancement(key, displays.get(key), root));
                }
                // Parent is an existing advancement
                else if (advancements.containsKey(displays.get(key).getParentKey())) {
                    advancements.put(key, find(key).getAdvancement(key, displays.get(key), advancements.get(displays.get(key).getParentKey())));
                }
                // Parent doesn't exist yet
                else {
                    retryDisplays.put(key, displays.get(key));
                }
            }
            displays = retryDisplays;
            i++;
        }
        return new Tuple<>(root, advancements.values().toArray(new StandardAdvancement[0]));
    }

    protected StandardAdvancement getAdvancement(String advKey, NodeDisplay display, Advancement parent) {
        if (display.maxProgression > 0) {
            return new StandardAdvancement(advKey, display, parent, display.maxProgression);
        } else {
            return new StandardAdvancement(advKey, display, parent);
        }
    }

    /**
     * Builds new advancement nodes
     */
    public static class AdvancementNodeBuilder {
        private final String key;

        // AdvancementDisplay Required
        private Material material = Material.TURTLE_EGG;
        private ItemStack icon = null;
        private AdvancementFrameType frameType = AdvancementFrameType.TASK;
        private String title = "Agent: P";
        private String description = "Curse you Perry the Platypus!";

        // Description
        private String[] requirements = null;
        private String reward = null;



        // Other Display Options
        private String compactDescription = "Curse Perry";

        // DAG Node
        private final String parent;

        // Additionals
        private int maxProgress = 1;

        /**
         * Create a new advancement
         * @param key The key of the new node
         */
        public AdvancementNodeBuilder(String key) {
            this(key, null);
        }

        /**
         * Create a new advancement
         * @param key The key of the new node
         * @param parent The key of the parent node
         */
        public AdvancementNodeBuilder(String key, String parent) {
            this.key = key;
            this.parent = parent;
        }

        /**
         * Sets the title text of the node
         * @param title MM string of the title
         * @return The node builder with the title set
         */
        public AdvancementNodeBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the description of the node
         * @param description The MM description
         * @return The node builder with the description set
         */
        public AdvancementNodeBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the requirements to allow auto-creation of description
         * @param requirements The MM requirements
         * @return The node builder with the requirements set
         */
        public AdvancementNodeBuilder setRequirements(String[] requirements) {
            this.requirements = requirements;
            return this;
        }

        /**
         * Sets the reward to allow auto-creation of description
         * @param reward  The MM reward text
         * @return The node builder with the reward set
         */
        public AdvancementNodeBuilder setReward(String reward) {
            this.reward = reward;
            return this;
        }

        /**
         * Sets the material for the advancement
         * @param material The Material item to use as an icon
         * @return The node builder with the material set
         */
        public AdvancementNodeBuilder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        /**
         * Sets the icon for the advancement
         * @param icon The item stack to use as an icon
         * @return The node builder with the icon set
         */
        public AdvancementNodeBuilder setIcon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Sets the frame
         * @param frameType The frame to use on the advancement
         * @return The node builder with the frame type set
         */
        public AdvancementNodeBuilder setFrameType(AdvancementFrameType frameType) {
            this.frameType = frameType;
            return this;
        }

        /**
         * Sets the maximum progress of the advancement
         * @param maxProgress The maximum progress the advancement requires
         * @return The node builder with the maximum progress set
         */
        public AdvancementNodeBuilder setMaxProgress(int maxProgress) {
            this.maxProgress = maxProgress;
            return this;
        }

        /**
         * Sets the compact description of the advancement
         * @param compactDescription The string compact description
         * @return The node builder with the compact description set
         */
        public AdvancementNodeBuilder setCompactDescription(String compactDescription) {
            this.compactDescription = compactDescription;
            return this;
        }

        protected void constructDescription() {
            if (requirements != null) {
                StringBuilder description = new StringBuilder("<white><b>Requirements:</b></white>");
                for (String requirement : requirements) {
                    description.append("<br>").append(requirement);
                }
                if (reward != null) {
                    description.append("<br><br><gold><b>Reward: </gold><br>").append(reward);
                }

                this.description = description.toString();
            }
        }

        protected void verifyIcon() {
            if (icon == null) {
                icon = new ItemStack(this.material);
            }
        }

        /**
         * Builds the advancement node
         * @return The built advancement node
         */
        public AdvancementNode build() {
            constructDescription();
            verifyIcon();

            return new AdvancementNode(this);
        }
    }
}
