package me.greenfoot5.castlesiege.events.map;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event for when a gate is rammed either by a gate or a ram
 */
public class NextMapEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public final String previousMap;

    /**
     * Create a RamEvent of damage by player(s) on a ram
     * @param mapName The name of the map that ended
     * @param async If the event should be run async
     */
    public NextMapEvent(String mapName, boolean async) {
        super(async);
        this.previousMap = mapName;
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
}