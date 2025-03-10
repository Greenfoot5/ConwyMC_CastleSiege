package me.greenfoot5.advancements.api.nodes;

/**
 * An Advancement node with a
 */
public class LevelAdvancementNode extends AdvancementNode {
    private final int level;

    protected LevelAdvancementNode(AdvancementNodeBuilder builder, int level) {
        super(builder);
        this.level = level;
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
         */
        public LevelAdvancementNodeBuilder(String key) {
            super(key);
        }

        /**
         * Create a new advancement
         *
         * @param key    The key of the new node
         * @param parent The key of the parent node
         */
        public LevelAdvancementNodeBuilder(String key, String parent) {
            super(key, parent);
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
