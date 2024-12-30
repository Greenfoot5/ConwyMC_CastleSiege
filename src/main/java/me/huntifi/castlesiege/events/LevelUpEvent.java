package me.huntifi.castlesiege.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event for a player levelling up
 */
public class LevelUpEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int newLevel;
    private final Player player;

    /**
     * Create a Level Event
     * @param player the uuid of the player levelling up
     * @param newLevel the new level
     */
    public LevelUpEvent(Player player, int newLevel) {
        super(false);
        this.player = player;
        this.newLevel = newLevel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * @return Gets the list of handlers
     */
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public Player getPlayer() {
        return player;
    }
}