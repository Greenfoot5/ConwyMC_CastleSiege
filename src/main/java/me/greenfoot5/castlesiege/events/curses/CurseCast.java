package me.greenfoot5.castlesiege.events.curses;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * An event for when a curse is cast
 */
public abstract class CurseCast extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private final String displayName;
    private final String activateMessage;
    private final String expireMessage;
    private long startTime;
    private final int duration;

    public final UUID player;

    protected CurseCast(CurseBuilder builder) {
        this.displayName = builder.displayName;
        this.activateMessage = builder.activateMessage;
        this.expireMessage = builder.expireMessage;
        this.player = builder.player;

        this.duration = builder.duration;
    }

    protected abstract void cast();

    /**
     * @return The display name for the curse
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return The message to display when the curse is activated
     */
    public String getActivateMessage() {
        return activateMessage;
    }

    /**
     * @return The message to display when the curse is deactivated
     */
    public String getExpireMessage() {
        return expireMessage;
    }

    /**
     * @return The time the curse began
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return How long the curse lasts
     */
    public int getDuration() {
        return duration;
    }

    /**
     * The player affected by the curse, {@code null} if all players are affected
     * @return The player affected
     */
    public UUID getPlayer() {
        return player;
    }

    protected void setStartTime() {
        this.startTime = System.currentTimeMillis() / 1000;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * @return The list of handlers
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    protected static void playSound(Collection<UUID> uuids) {
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
        }
    }

    /**
     * The builder to create a new curse
     */
    //Builder Class
    public abstract static class CurseBuilder {

        // required parameters
        private static final HandlerList HANDLERS = new HandlerList();
        private boolean isCancelled;
        private final String displayName;
        private final String activateMessage;
        private final String expireMessage;
        protected List<List<String>> options;
        protected int duration;

        public UUID player;

        /**
         * @param name The name of the curse
         * @param activateMessage The message to display when the curse is activated
         * @param expireMessage The message to display when the curse expires
         */
        public CurseBuilder(String name, String activateMessage, String expireMessage) {
            this.displayName = name;
            this.activateMessage = activateMessage;
            this.expireMessage = expireMessage;
            this.player = null;

            this.duration = -1;
        }

        /**
         * @param index The index of the options to display
         * @return The options a player could use
         */
        public List<String> getCommandOptions(int index) {
            return options.get(index);
        }

        /**
         * @return The maximum number of arguments
         */
        public int getCommandOptionsLength() {
            return options.size();
        }

        /**
         * Activates the curse
         */
        public abstract void cast();
    }
}
