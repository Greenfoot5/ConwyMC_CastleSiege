package me.greenfoot5.castlesiege.kits.kits;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A kit that's automatically unlocked
 */
public abstract class FreeKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    /**
     * Create a Free Kit
     * @param name       This kit's name
     * @param baseHealth This kit's base health
     * @param regenAmount The amount to regen every regen tick
     * @param material The material to display in GUIS
     */
    public FreeKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.DARK_GREEN);
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
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Free to play!", color).decorate(TextDecoration.BOLD));
        return text;
    }

}
