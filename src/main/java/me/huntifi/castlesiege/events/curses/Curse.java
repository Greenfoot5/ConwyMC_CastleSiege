package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Curse {
    public final String displayName;
    public final String activateMessage;
    public final String expireMessage;
    public final List<List<String>> options;
    protected long startTime;
    public final int duration;
    public final long endTime;

    protected static final ArrayList<Curse> globalActivatedCurses = new ArrayList<>();
    protected static final ArrayList<Tuple<Curse, UUID>> playerActivatedCurses = new ArrayList<>();

    public Curse(String name, String activateMessage, String expireMessage, List<List<String>> options, int duration) {
        this.displayName = name;
        this.activateMessage = activateMessage;
        this.expireMessage = expireMessage;
        this.options = options;

        this.startTime = System.currentTimeMillis() / 1000;
        this.duration = duration;
        this.endTime = startTime + duration;
    }

    public String getRemainingTime() {
        return Main.getTimeString(endTime - (System.currentTimeMillis() / 1000));
    }

    public boolean activateCurse() {
        startTime = System.currentTimeMillis() / 1000;
        Curse.globalActivatedCurses.add(this);
        Messenger.broadcastCurse(ChatColor.DARK_RED + displayName + "§r has been activated! " + activateMessage);
        Curse curse = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                Curse.globalActivatedCurses.remove(curse);
                Messenger.broadcastCurseEnd(ChatColor.DARK_GREEN + displayName + "§r has been expired! " + expireMessage);
            }
        }.runTaskLater(Main.plugin, duration * 20L);
        return true;
    }

    public boolean activateCurse(Player player) {
        startTime = System.currentTimeMillis() / 1000;
        Tuple<Curse, UUID> curse = new Tuple<>(this, player.getUniqueId());
        Curse.playerActivatedCurses.add(curse);
        Messenger.sendCurseEnd(ChatColor.DARK_RED + displayName + "§r has been activated! " + activateMessage, player);
        new BukkitRunnable() {
            @Override
            public void run() {
                Curse.playerActivatedCurses.remove(curse);
                Messenger.sendCurse(ChatColor.DARK_GREEN + displayName + "§r has been expired! " + expireMessage, player);
            }
        }.runTaskLater(Main.plugin, duration * 20L);
        return true;
    }

    public static Curse isCurseActive(Type t) {
        for (Curse curse : globalActivatedCurses) {
            if (curse.getClass() == t) {
                return curse;
            }
        }
        return null;
    }

    public static Curse isCurseActive(Type t, UUID uuid) {
        Curse curse = isCurseActive(t);
        if (curse != null) {
            return curse;
        }

        for (Tuple<Curse, UUID> tuple : playerActivatedCurses) {
            if (uuid == tuple.getSecond() && tuple.getFirst().getClass() == t) {
                return tuple.getFirst();
            }
        }
        return null;
    }
}
