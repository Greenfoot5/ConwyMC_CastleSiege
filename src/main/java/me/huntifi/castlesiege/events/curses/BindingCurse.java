package me.huntifi.castlesiege.events.curses;

import org.bukkit.entity.Player;

public class BindingCurse extends Curse {
    public final static String name = "Curse of Binding";
    private final static String activateMessage = "You can no longer switch kits";
    private final static String expireMessage = "You can now switch kits again!";
    private final static String[][] OPTIONS = {{"<duration>"}, {"[player]"}};

    private final static int MIN_DURATION = 30;
    public BindingCurse(int duration) {
        super(name, activateMessage, expireMessage, OPTIONS, duration);
    }

    @Override
    public boolean activateCurse() {
        return duration >= MIN_DURATION && super.activateCurse();
    }

    @Override
    public boolean activateCurse(Player player) {
        return duration >= MIN_DURATION && super.activateCurse(player);
    }
}
