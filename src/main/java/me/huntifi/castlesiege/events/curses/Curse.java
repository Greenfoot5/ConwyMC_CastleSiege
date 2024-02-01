package me.huntifi.castlesiege.events.curses;

import me.huntifi.castlesiege.data_types.Tuple;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Curse {
    public final String displayName;
    public final String activateMessage;
    public final String expireMessage;

    private static final ArrayList<Curse> globalActivatedCurses = new ArrayList<>();
    private static final ArrayList<Tuple<Curse, UUID>> playerActivatedCurses = new ArrayList<>();

    public Curse(String name, String activateMessage, String expireMessage) {
        this.displayName = name;
        this.activateMessage = name;
        this.expireMessage = name;
    }
}
