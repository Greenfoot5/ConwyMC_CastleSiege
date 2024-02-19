package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

    public String getDisplayName() {
        return displayName;
    }

    public String getActivateMessage() {
        return activateMessage;
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

    protected void setStartTime() {
        this.startTime = System.currentTimeMillis() / 1000;
    }

    public String getRemainingTime() {
        return Main.getTimeString(getEndTime() - (System.currentTimeMillis() / 1000));
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

    protected static void playSound(List<UUID> uuids) {
        for (UUID uuid : uuids) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
        }
    }

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

        public CurseBuilder(String name, String activateMessage, String expireMessage){
            this.displayName = name;
            this.activateMessage = activateMessage;
            this.expireMessage = expireMessage;
            this.player = null;

            this.duration = -1;
        }

        public List<String> getCommandOptions(int index) {
            return options.get(index);
        }
        public int getCommandOptionsLength() {
            return options.size();
        }

        public abstract CurseCast cast();
    }
}
