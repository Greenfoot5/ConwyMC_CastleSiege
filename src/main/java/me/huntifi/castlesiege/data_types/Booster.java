package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.util.List;

/**
 * A basic abstract booster that has a duration only
 */
public abstract class Booster implements Comparable<Booster> {
    public int id;
    public int duration;
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
    public abstract String getName();

    /**
     * @return The lore used when displaying the booster in a gui
     */
    public abstract List<String> getLore();

    /**
     * @return Get the duration in a nice day, hour, min, sec format
     */
    public String getDurationAsString() {
        return durationToString(duration);
    }

    public static String durationToString(int duration) {
        int days, hours, minutes, seconds, remainder;
        days = duration / 86400;
        remainder = duration % 86400;
        hours = remainder / 3600;
        remainder = remainder % 3600;
        minutes = remainder / 60;
        seconds = remainder % 60;

        if (days == 0) {
            if (hours == 0) {
                if (minutes == 0) {
                    return seconds + "s";
                }
                return minutes + " mins " + seconds + "s";
            }
            return hours + " hr " + minutes + " mins " + seconds + "s";
        }
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
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
