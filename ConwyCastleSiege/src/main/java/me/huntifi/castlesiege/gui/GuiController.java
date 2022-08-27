package me.huntifi.castlesiege.gui;

import java.util.HashMap;

/**
 * Manages the GUIs used to select and buy kits
 */
public class GuiController {

    private static final HashMap<String, Gui> keyToGui = new HashMap<>();

    /**
     * Save a GUI to the map of GUIs
     * @param key The key with which the GUI can be retrieved
     * @param gui The GUI to save
     */
    public static void add(String key, Gui gui) {
        keyToGui.put(key, gui);
    }

    /**
     * Get a GUI from the map of GUIs
     * @param key The key with which the GUI is stored
     */
    public static Gui get(String key) {
        return keyToGui.get(key);
    }
}
