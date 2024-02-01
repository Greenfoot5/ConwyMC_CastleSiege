package me.huntifi.castlesiege.events.curses;

public class BindingCurse extends Curse {
    private final static String name = "Curse of Binding";
    private final static String activateMessage = "You can no longer switch kits";
    private final static String expireMessage = "You can now switch kits again";
    public BindingCurse() {
        super(name, activateMessage, expireMessage);
    }
}
