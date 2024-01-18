package me.huntifi.castlesiege.kits.kits;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;

public abstract class FreeKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a kit with basic settings
     *
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     */
    public FreeKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, ChatColor.DARK_GREEN);
        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
    }

    /**
     * Get all free kit names
     * @return All free kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public static ArrayList<String> getGuiCostText() {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add("§2§lFree to play!");
        return text;
    }

}
