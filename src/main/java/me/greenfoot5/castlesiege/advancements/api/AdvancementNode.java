package me.greenfoot5.castlesiege.advancements.api;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.greenfoot5.castlesiege.advancements.displays.NodeDisplay;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A node allowing automatic display of advancements
 */
@SuppressWarnings("deprecation")
public class AdvancementNode {
    private final String key;

    // AdvancementDisplay Required
    private final Material icon;
    private final AdvancementFrameType frameType;
    private final String title;
    private final String description;

    // Other Display Options
    private final BaseComponent[] announceMessage;
    private boolean announceInChat;
    private final String compactDescription;
    private boolean showToast;

    // DAG Node
    private final String parent;
    private final List<AdvancementNode> children;

    // Additionals
    private final int maxProgress;

    // References
    BaseAdvancement advancement;
    NodeDisplay display;

    protected AdvancementNode(AdvancementNodeBuilder builder) {
        this.key = builder.key;
        this.parent = builder.parent;
        this.children = new ArrayList<>();

        this.icon = builder.material;
        this.frameType = builder.frameType;
        this.title = builder.title;
        this.description = builder.description;

        this.announceMessage = builder.announceMessage;
        this.announceInChat = builder.announceInChat;
        this.compactDescription = builder.compactDescription;
        this.showToast = builder.showToast;

        this.maxProgress = builder.maxProgress;
    }

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
        NodeDisplay display = new NodeDisplay(icon, frameType, showToast, announceInChat, x, y, title, description, maxProgress);
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
                    advancements.put(key, new BaseAdvancement(key, displays.get(key), root, displays.get(key).maxProgression));

                }
                // Parent is an existing advancement
                else if (advancements.containsKey(displays.get(key).getParentKey())) {
                    if (displays.get(key).maxProgression > 0) {
                        advancements.put(key, new BaseAdvancement(key, displays.get(key), advancements.get(displays.get(key).getParentKey()), displays.get(key).maxProgression));
                    } else {
                        advancements.put(key, new BaseAdvancement(key, displays.get(key), advancements.get(displays.get(key).getParentKey())));
                    }
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
     * Builds new advancement nodes
     */
    public static class AdvancementNodeBuilder {
        private final String key;

        // AdvancementDisplay Required
        private Material material = Material.TURTLE_EGG;
        private AdvancementFrameType frameType = AdvancementFrameType.TASK;
        private String title = "Agent: P";
        private String description = "Curse you Perry the Platypus!";

        // Description
        private String[] requirements = null;
        private String reward = null;



        // Other Display Options
        private BaseComponent[] announceMessage = new BaseComponent[0];
        private boolean announceInChat = false;
        private String compactDescription = "Curse Perry";
        private boolean showToast = true;

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
         * Sets the announcement message
         * @param announceMessage Bungee BaseComponent message
         * @return The node builder with the announcement message set
         */
        public AdvancementNodeBuilder setAnnounceMessage(BaseComponent[] announceMessage) {
            this.announceMessage = announceMessage;
            return this;
        }

        /**
         * Sets if the advancement should announce it in chat
         * @param announceInChat true if it should be announced
         * @return The node builder with announce in chat set
         */
        public AdvancementNodeBuilder setAnnounceInChat(boolean announceInChat) {
            this.announceInChat = announceInChat;
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

        /**
         * If upon getting the advancement it should show the toast
         * @param showToast If the toast should be shown
         * @return The node builder with show toast set
         */
        public AdvancementNodeBuilder setShowToast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

        /**
         * Builds the advancement node
         * @return The built advancement node
         */
        public AdvancementNode build()
        {
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

            return new AdvancementNode(this);
        }
    }
}
