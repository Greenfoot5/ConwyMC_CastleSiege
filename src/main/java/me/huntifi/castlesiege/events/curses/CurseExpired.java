package me.huntifi.castlesiege.events.curses;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

public class CurseExpired extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private final String displayName;
    private final String expireMessage;
    private final long startTime;
    private final int duration;

    public final UUID player;

    private final Type castCurse;

    public CurseExpired(CurseCast curse) {
        this.displayName = curse.getDisplayName();
        this.expireMessage = curse.getExpireMessage();
        this.player = curse.getPlayer();

        this.duration = curse.getDuration();
        this.startTime = curse.getStartTime();
        this.castCurse = curse.getClass();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getExpireMessage() {
        return expireMessage;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public long getEndTime() {
        return startTime + duration;
    }

    public UUID getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
