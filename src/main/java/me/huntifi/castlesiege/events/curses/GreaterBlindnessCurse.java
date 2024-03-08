package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GreaterBlindnessCurse extends CurseCast {
    public final static String name = "Curse of Greater Blindness";
    private final static String activateMessage = "Your team colour has been hidden";
    private final static String expireMessage = "Your team colour has been shown!";
    private final static List<List<String>> OPTIONS = List.of(List.of("<duration>"));

    public final static int MIN_DURATION = 30;

    private GreaterBlindnessCurse(CurseBuilder builder) {
        super(builder);
    }

    @Override
    protected void cast() {
        this.setStartTime();
        Messenger.broadcastCurse("<dark_red>" + getDisplayName() + "</dark_red> has been activated! " + getActivateMessage());
        playSound(MapController.getPlayers());

        CurseCast curse = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                CurseExpired expired = new CurseExpired(curse);
                Bukkit.getPluginManager().callEvent(expired);
                Messenger.broadcastCurseEnd("<dark_green>" + expired.getDisplayName() + "</dark_green> has been expired! " + expired.getExpireMessage());
            }
        }.runTaskLater(Main.plugin, getDuration());

    }

    //Builder Class
    public static class CurseBuilder extends CurseCast.CurseBuilder {

        public CurseBuilder(int duration) {
            super(name, activateMessage, expireMessage);
            if (duration < MIN_DURATION)
                throw new IllegalArgumentException("Duration is too small!");
            this.duration = duration;
            this.options = OPTIONS;
        }

        public GreaterBlindnessCurse cast() {
            GreaterBlindnessCurse curse = new GreaterBlindnessCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }
    }
}
