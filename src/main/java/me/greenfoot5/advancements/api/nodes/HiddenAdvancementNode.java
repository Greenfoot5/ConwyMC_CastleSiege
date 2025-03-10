package me.greenfoot5.advancements.api.nodes;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import me.greenfoot5.advancements.api.advancements.HiddenAdvancement;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.displays.NodeDisplay;

/**
 * An AdvancementNode that's hidden until obtained
 */
public class HiddenAdvancementNode extends AdvancementNode {
    protected HiddenAdvancementNode(AdvancementNodeBuilder builder) {
        super(builder);
    }

    @Override
    protected StandardAdvancement getAdvancement(String advKey, NodeDisplay display, Advancement parent) {
        if (display.maxProgression > 0) {
            return new HiddenAdvancement(advKey, display, parent, display.maxProgression);
        } else {
            return new HiddenAdvancement(advKey, display, parent);
        }
    }

    /**
     * Builds new hidden advancement nodes
     */
    public static class HiddenAdvancementNodeBuilder extends AdvancementNodeBuilder {
        /**
         * Create a new advancement
         *
         * @param key The key of the new node
         */
        public HiddenAdvancementNodeBuilder(String key) {
            super(key);
        }

        /**
         * Create a new advancement
         *
         * @param key    The key of the new node
         * @param parent The key of the parent node
         */
        public HiddenAdvancementNodeBuilder(String key, String parent) {
            super(key, parent);
        }

        /**
         * Builds the advancement node
         * @return The built advancement node
         */
        public HiddenAdvancementNode build()
        {
            constructDescription();
            verifyIcon();

            return new HiddenAdvancementNode(this);
        }
    }
}
