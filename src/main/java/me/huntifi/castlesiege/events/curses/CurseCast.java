package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class CurseCast extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private final String displayName;
    private final String activateMessage;
    private final String expireMessage;
    private final List<List<String>> options;
    private long startTime;
    private final int duration;

    public final UUID player;

    public CurseCast(String name, String activateMessage, String expireMessage, List<List<String>> options, int duration) {
        this.displayName = name;
        this.activateMessage = activateMessage;
        this.expireMessage = expireMessage;
        this.options = options;
        this.player = null;

        this.duration = duration;
    }

    public CurseCast(String name, String activateMessage, String expireMessage, List<List<String>> options, int duration, UUID player) {
        this.displayName = name;
        this.activateMessage = activateMessage;
        this.expireMessage = expireMessage;
        this.options = options;
        this.player = player;

        this.duration = duration;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getActivateMessage() {
        return activateMessage;
    }

    public String getExpireMessage() {
        return expireMessage;
    }

    public List<String> getCommandOptions(int index) {
        return options.get(index);
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

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

    public String getRemainingTime() {
        return Main.getTimeString(getEndTime() - (System.currentTimeMillis() / 1000));
    }
}
