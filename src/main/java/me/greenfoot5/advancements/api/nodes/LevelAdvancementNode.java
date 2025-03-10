package me.greenfoot5.advancements.api.nodes;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import me.greenfoot5.advancements.api.advancements.LevelAdvancement;
import me.greenfoot5.advancements.api.advancements.StandardAdvancement;
import me.greenfoot5.advancements.api.displays.NodeDisplay;

/**
 * An Advancement node with a minimum level to view
 */
public class LevelAdvancementNode extends AdvancementNode {
    private final int level;

    protected LevelAdvancementNode(AdvancementNodeBuilder builder, int level) {
        super(builder);
        this.level = level;
    }

    @Override
    protected StandardAdvancement getAdvancement(String advKey, NodeDisplay display, Advancement parent) {
        if (display.maxProgression > 0) {
            return new LevelAdvancement(advKey, display, parent, level, display.maxProgression);
        } else {
            return new LevelAdvancement(advKey, display, parent, level);
        }
    }

    /**
     * Builds new advancement nodes
     */
    public static class LevelAdvancementNodeBuilder extends AdvancementNodeBuilder {
        private int level;

        /**
         * Create a new advancement
         *
         * @param key The key of the new node
         * @param level Required level to view
         */
        public LevelAdvancementNodeBuilder(String key, int level) {
            super(key);
            this.level = level;
        }

        /**
         * Create a new advancement
         *
         * @param key    The key of the new node
         * @param parent The key of the parent node
         * @param level Required level to view
         */
        public LevelAdvancementNodeBuilder(String key, String parent, int level) {
            super(key, parent);
            this.level = level;
        }

        /**
         * Sets the required level to view the advancement
         * @param level The minimum level required
         * @return The node builder with required level set
         */
        public LevelAdvancementNodeBuilder setLevel(int level) {
            this.level = level;
            return this;
        }

        /**
         * Builds the advancement node
         * @return The built advancement node
         */
        public LevelAdvancementNode build()
        {
            constructDescription();
            verifyIcon();

            return new LevelAdvancementNode(this, this.level);
        }
    }
}
