package me.greenfoot5.advancements.api.nodes;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import me.greenfoot5.advancements.api.advancements.AdvancementAdvancement;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.displays.NodeDisplay;

import java.util.HashSet;
import java.util.Set;

/**
 * An AdvancementNode that requires other advancements be earnt to be visible
 */
public class AdvacementAdvancementNode extends AdvancementNode {
    private final String[] requiredAdvancements;

    protected AdvacementAdvancementNode(AdvancementNodeBuilder builder, String[] requiredAdvancements) {
        super(builder);
        this.requiredAdvancements = requiredAdvancements;
    }

    @Override
    protected StandardAdvancement getAdvancement(String advKey, NodeDisplay display, Advancement parent) {
        if (display.maxProgression > 0) {
            return new AdvancementAdvancement(advKey, display, parent, requiredAdvancements, display.maxProgression);
        } else {
            return new AdvancementAdvancement(advKey, display, parent, requiredAdvancements);
        }
    }

    /**
     * Builds new advancement nodes
     */
    public static class AdvancementAdvancementNodeBuilder extends AdvancementNodeBuilder {
        private final Set<String> requiredKeys = new HashSet<>();

        /**
         * Create a new advancement
         *
         * @param key The key of the new node
         */
        public AdvancementAdvancementNodeBuilder(String key) {
            super(key);
        }

        /**
         * Create a new advancement
         *
         * @param key    The key of the new node
         * @param parent The key of the parent node
         */
        public AdvancementAdvancementNodeBuilder(String key, String parent) {
            super(key, parent);
        }

        /**
         * Adds a required advancement key to make it visible
         * @param key The key of the new advancement requirement
         * @return The builder with the required advancement key added
         */
        public AdvancementAdvancementNodeBuilder addAdvancement(String key) {
            this.requiredKeys.add(key);
            return this;
        }

        /**
         * Builds the advancement node
         * @return The built advancement node
         */
        public AdvacementAdvancementNode build()
        {
            constructDescription();
            verifyIcon();

            return new AdvacementAdvancementNode(this, this.requiredKeys.toArray(String[]::new));
        }
    }
}
