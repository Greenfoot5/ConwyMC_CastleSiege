package me.huntifi.castlesiege.events.curses;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BindingCurse extends CurseCast {
    public final static String name = "CurseCast of Binding";
    private final static String activateMessage = "You can no longer switch kits";
    private final static String expireMessage = "You can now switch kits again!";
    private final static List<List<String>> OPTIONS = Arrays.asList(List.of("<duration>"), List.of("[player]"));

    public final static int MIN_DURATION = 30;

    public BindingCurse(int duration) {
        super(name, activateMessage, expireMessage, OPTIONS, duration);
    }
}
