package me.huntifi.castlesiege.events.map;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event for when a gate is rammed either by a gate or a ram
 */
public class CannonEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final Player shooter;

    /**
     * A player fired a cannon
     * @param player The player that fired the cannon
     */
    public CannonEvent(Player player) {
        super(false);
        this.isCancelled = false;
        this.shooter = player;
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

    /**
     * @return The shooter
     */
    public Player getShooter() {
        return shooter;
    }
}