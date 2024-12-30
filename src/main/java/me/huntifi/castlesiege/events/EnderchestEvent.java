package me.huntifi.castlesiege.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Handles players opening an enderchest
 */
public class EnderchestEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    private final Player player;

    /**
     * @param player The player opening the enderchest
     */
    public EnderchestEvent(Player player){
        this.player = player;
        this.isCancelled = false;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    /**
     * Gets the event handlers of this event.
     *
     * @return The handlers of this event
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    /**
     * Gets the event handlers of this event.
     *
     * @return The handlers of this event
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Gets the player that triggered this event.
     *
     * @return The player that triggered this event
     */
    public Player getPlayer() {
        return player;
    }
}
