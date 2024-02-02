package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Curse {
    public final String displayName;
    public final String activateMessage;
    public final String expireMessage;
    protected long startTime;
    public final int duration;
    public final long endTime;

    protected static final ArrayList<Curse> globalActivatedCurses = new ArrayList<>();
    protected static final ArrayList<Tuple<Curse, UUID>> playerActivatedCurses = new ArrayList<>();

    public Curse(String name, String activateMessage, String expireMessage, int duration) {
        this.displayName = name;
        this.activateMessage = activateMessage;
        this.expireMessage = expireMessage;

        this.startTime = System.currentTimeMillis() / 1000;
        this.duration = duration;
        this.endTime = startTime + duration;
    }

    public String getRemainingTime() {
        return Main.getTimeString(endTime - (System.currentTimeMillis() / 1000));
    }

    public abstract void activateCurse();

    public abstract void activateCurse(Player player);

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
