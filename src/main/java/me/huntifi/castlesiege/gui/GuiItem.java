package me.huntifi.castlesiege.gui;

/**
 * Represents a GUI Item in a Gui menu.
 */
public class GuiItem {

    public final String command;
    public final boolean shouldClose;

    /**
     * @param command The command to run when the item is clicked
     * @param shouldClose If the menu should close or not
     */
    public GuiItem(String command, boolean shouldClose) {
        this.command = command;
        this.shouldClose = shouldClose;
    }
}
