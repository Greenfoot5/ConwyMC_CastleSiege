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

        public AdvancementNodeBuilder(String key) {
            this(key, null);
        }

        public AdvancementNodeBuilder(String key, String parent) {
            this.key = key;

            this.parent = parent;
        }

        public AdvancementNodeBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public AdvancementNodeBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public AdvancementNodeBuilder setRequirements(String[] requirements) {
            this.requirements = requirements;
            return this;
        }

        public AdvancementNodeBuilder setReward(String reward) {
            this.reward = reward;
            return this;
        }

        public AdvancementNodeBuilder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        public AdvancementNodeBuilder setFrameType(AdvancementFrameType frameType) {
            this.frameType = frameType;
            return this;
        }

        public AdvancementNodeBuilder setMaxProgress(int maxProgress) {
            this.maxProgress = maxProgress;
            return this;
        }

        public AdvancementNodeBuilder setAnnounceMessage(BaseComponent[] announceMessage) {
            this.announceMessage = announceMessage;
            return this;
        }

        public AdvancementNodeBuilder setAnnounceInChat(boolean announceInChat) {
            this.announceInChat = announceInChat;
            return this;
        }

        public AdvancementNodeBuilder setCompactDescription(String compactDescription) {
            this.compactDescription = compactDescription;
            return this;
        }

        public AdvancementNodeBuilder setShowToast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

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
