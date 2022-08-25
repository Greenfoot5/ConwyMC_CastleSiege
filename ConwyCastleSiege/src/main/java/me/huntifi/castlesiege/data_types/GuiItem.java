package me.huntifi.castlesiege.data_types;

public class GuiItem {

    public final String command;
    public final boolean shouldClose;

    public GuiItem(String command, boolean shouldClose) {
        this.command = command;
        this.shouldClose = shouldClose;
    }
}
