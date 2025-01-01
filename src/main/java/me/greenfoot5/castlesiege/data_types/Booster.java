package me.greenfoot5.castlesiege.data_types;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic abstract booster that has a duration only
 */
public abstract class Booster implements Comparable<Booster> {
    public int id;
    public final int duration;
    public final Material material;

    public static int newId;

    /**
     * Defines a basic booster
     * @param duration The duration of the booster
     * @param material The material used when displaying the booster
     */
    public Booster(int duration, Material material) {
        id = newId;
        newId++;
        this.duration = duration;
        this.material = material;
    }

    /**
     * @return The type of the booster as an all caps string
     */
    public abstract String getBoostType();

    /**
     * Gets any optional values of the booster required by children
     * @return A string of all optional parameters
     */
    public abstract String getValue();

    /**
     * @return The display name of the booster
     */
    public abstract Component getName();

    /**
     * @return The lore used when displaying the booster in a gui
     */
    public List<Component> getLore() {
        List<Component> lore = new ArrayList<>();
        lore.add(Messenger.mm.deserialize("<dark_aqua>ID: <aqua>" + id + "</aqua></dark_aqua>"));
        lore.add(Component.text("Duration: ", NamedTextColor.DARK_AQUA)
                .append(Component.text(getDurationAsString(), NamedTextColor.AQUA).decorate(TextDecoration.BOLD)));
        lore.add(Component.empty());
        return lore;
    }

    /**
     * @return Get the duration in a nice day, hour, min, sec format
     */
    public String getDurationAsString() {
        return Main.getTimeString(duration);
    }

    public int compareTo(Booster booster) {
        return id - booster.id;
    }

    /**
     * Used each time a booster is loaded from the database,
     * Makes sure new boosters are created with the highest booster + 1
     * @param oldId The id of the booster to check against
     */
    public static void updateID(int oldId) {
        if (oldId >= newId) {
            newId = oldId + 1;
        }
    }
}
