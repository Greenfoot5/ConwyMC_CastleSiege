package me.greenfoot5.castlesiege.events.map;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An event for when a gate is rammed either by a gate or a ram
 */
public class GateBreachEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String gateName;
    private final int originalHealth;

    private final HashMap<UUID, Integer> damagers;

    /**
     * Create a GateBreachEvent of damage by player(s)
     *
     * @param gateName      The name of the gate being rammed
     * @param originalHealth The original health of the gate
     * @param damagers      The players who have attacked and their total damage
     */
    public GateBreachEvent(String gateName, int originalHealth, HashMap<UUID, Integer> damagers) {
        super(false);
        this.originalHealth = originalHealth;
        this.gateName = gateName;
        this.damagers = damagers;
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

    /**
     * @return The damage dealt by the event
     */
    public int getOriginalHealth() {
        return originalHealth;
    }

    /**
     * @return The name of the gate being rammed
     */
    public String getGateName() {
        return gateName;
    }

    /**
     * @return The damage (and who dealt it) so far
     */
    public Map<UUID, Integer> getDamagers() {
        return damagers;
    }

    /**
     * @param uuid The UUID of the player to query
     * @return The damage a player did
     */
    public int getDamage(UUID uuid) {
        return damagers.getOrDefault(uuid, 0);
    }

    /**
     * The source of the damage to the gate
     */
    public enum RamType {
        FIST,
        RAM
    }
}