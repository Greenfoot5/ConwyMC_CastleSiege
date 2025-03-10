package me.greenfoot5.advancements.api.nodes;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.displays.NodeDisplay;

/**
 * A standard advancement node
 */
public class VisibleAdvancementNode extends AdvancementNode {
    protected VisibleAdvancementNode(AdvancementNodeBuilder builder) {
        super(builder);
    }

    /**
     * Builder for VisibleAdvancementnode
     */
    public static class VisibleAdvancementNodeBuilder extends AdvancementNodeBuilder {

        /**
         * Create a new advancement
         *
         * @param key The key of the new node
         */
        public VisibleAdvancementNodeBuilder(String key) {
            super(key);
        }

        /**
         * Create a new advancement
         *
         * @param key    The key of the new node
         * @param parent The key of the parent node
         */
        public VisibleAdvancementNodeBuilder(String key, String parent) {
            super(key, parent);
        }

        /**
         * Builds the advancement node
         *
         * @return The built advancement node
         */
        @Override
        public AdvancementNode build() {
            constructDescription();
            verifyIcon();

            return new VisibleAdvancementNode(this);
        }
    }
}
