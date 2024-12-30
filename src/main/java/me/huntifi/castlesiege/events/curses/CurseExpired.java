package me.huntifi.castlesiege.events.curses;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Called when a curse expires
 */
public class CurseExpired extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String displayName;
    private final String expireMessage;
    private final long startTime;
    private final int duration;

    public final UUID player;

    private final Type castCurse;

    /**
     * @param curse The curse that expired
     */
    public CurseExpired(CurseCast curse) {
        this.displayName = curse.getDisplayName();
        this.expireMessage = curse.getExpireMessage();
        this.player = curse.getPlayer();

        this.duration = curse.getDuration();
        this.startTime = curse.getStartTime();
        this.castCurse = curse.getClass();
    }

    /**
     * @return The display name of the curse
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return The message to display when the curse expires
     */
    public String getExpireMessage() {
        return expireMessage;
    }

    /**
     * @return The time the curse was activated
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return How long the curse lasted
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
}
