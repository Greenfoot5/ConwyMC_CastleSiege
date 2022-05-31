package me.huntifi.castlesiege.kits.gui;

import java.util.HashMap;

/**
 * Manages the GUIs used to select and buy kits
 */
public class KitGuiController {

    private static final HashMap<String, KitGui> guis = new HashMap<>();

    /**
     * Save a GUI to the map of GUIs
     * @param key The key with which the GUI can be retrieved
     * @param gui The GUI to save
     */
    public static void add(String key, KitGui gui) {
        guis.put(trimColor(key), gui);
    }

    /**
     * Get a GUI from the map of GUIs
     * @param key The key with which the GUI is stored
     */
    public static KitGui get(String key) {
        return guis.get(trimColor(key));
    }

    /**
     * Remove the color prefix from a string
     * @param string The string to edit
     * @return The input string, with its first two characters removed if they specify a color
     */
    private static String trimColor(String string) {
        if (string.length() >= 2 && string.startsWith("§")) {
            return string.substring(2);
        }
        return string;
    }
}
