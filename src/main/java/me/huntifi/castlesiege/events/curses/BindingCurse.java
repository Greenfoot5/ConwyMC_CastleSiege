package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BindingCurse extends CurseCast {
    public final static String name = "Curse of Binding";
    private final static String activateMessage = "You can no longer switch kits";
    private final static String expireMessage = "You can now switch kits again!";
    private final static List<List<String>> OPTIONS = Arrays.asList(List.of("<duration>"), List.of("[player]"));

    public final static int MIN_DURATION = 30;

    private BindingCurse(CurseBuilder builder) {
        super(builder);
    }

    @Override
    protected void cast() {
        this.setStartTime();
        Player player = getPlayer() == null ? null : Bukkit.getPlayer(getPlayer());
        if (player == null) {
            Messenger.broadcastCurse("<dark_red>" + getDisplayName() + "</dark_red> has been activated! " + getActivateMessage());
            playSound(MapController.getPlayers());
        } else {
            Messenger.sendCurse("<dark_red>" + getDisplayName() + "</dark_red> has been activated! " + getActivateMessage(), player);
            playSound(List.of(player.getUniqueId()));
        }

        CurseCast curse = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                CurseExpired expired = new CurseExpired(curse);
                Bukkit.getPluginManager().callEvent(expired);
                if (player == null) {
                    Messenger.broadcastCurseEnd("<dark_green>" + expired.getDisplayName() + "</dark_green> has been expired! " + expired.getExpireMessage());
                } else
                    Messenger.sendCurseEnd("<dark_green>" + expired.getDisplayName() + "</dark_green> has been expired! " + expired.getExpireMessage(), player);
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

        public BindingCurse cast() {
            BindingCurse curse = new BindingCurse(this);
            Bukkit.getPluginManager().callEvent(curse);
            if (!curse.isCancelled())
                curse.cast();
            return curse;
        }

        public CurseCast.CurseBuilder setPlayer(UUID player) {
            this.player = player;
            return this;
        }
    }
}
