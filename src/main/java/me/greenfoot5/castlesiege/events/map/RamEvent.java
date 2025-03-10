package me.greenfoot5.castlesiege.events.map;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An event for when a gate is rammed either by a gate or a ram
 */
public class RamEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final String gateName;
    private final RamType ramType;
    private final int currentHealth;

    private int damageDealt;

    private final ArrayList<UUID> uuids;

    /**
     * Create a RamEvent of damage by player(s) on a ram
     *
     * @param gateName      The name of the gate being rammed
     * @param damageDealt   The damage dealt by the ram
     * @param currentHealth The current health of the gate (pre-hit)
     * @param players       The player(s) dealing the damage
     */
    public RamEvent(String gateName, int damageDealt, int currentHealth, ArrayList<UUID> players) {
        super(true);
        this.isCancelled = false;
        this.gateName = gateName;
        this.damageDealt = damageDealt;
        this.currentHealth = currentHealth;
        this.uuids = players;

        this.ramType = RamType.RAM;
    }

    /**
     * Create a RamEvent of damage by fist
     * @param gateName The name of the gate being rammed
     * @param damageDealt The damage dealt by the ram
     * @param currentHealth The current health of the gate (pre-hit)
     * @param player The player dealing the damage
     */
    public RamEvent(String gateName, int damageDealt, int currentHealth, UUID player) {
        this.isCancelled = false;
        this.gateName = gateName;
        this.damageDealt = damageDealt;
        this.currentHealth = currentHealth;
        this.uuids = new ArrayList<>();
        this.uuids.add(player);

        this.ramType = RamType.FIST;
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
     * @return The damage dealt by the event
     */
    public int getDamageDealt() {
        return damageDealt;
    }

    /**
     * @param damageDealt The new damage to deal with the event
     */
    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    /**
     * @return The name of the gate being rammed
     */
    public String getGateName() {
        return gateName;
    }

    /**
     * @return How the damage is being done to the gate
     */
    public RamType getRamType() {
        return ramType;
    }

    /**
     * @return The health of the gate before being hit
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * @return The player(s) ramming the gate
     */
    public ArrayList<UUID> getPlayerUUIDs() {
        return uuids;
    }

    /**
     * @return If the gate was breached with this ram
     */
    public boolean wasGateBreached() {
        return getCurrentHealth() <= getDamageDealt();
    }

    /**
     * The source of the damage to the gate
     */
    public enum RamType {
        FIST,
        RAM
    }
}