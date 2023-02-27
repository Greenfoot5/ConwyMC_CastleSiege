package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import org.bukkit.Material;

import java.util.List;

public abstract class Booster implements Comparable<Booster> {
    public int id;
    public int duration;
    public final Material material;

    public static int newId;

    public Booster(int duration, Material material) {
        id = newId;
        newId++;
        this.duration = duration;
        this.material = material;
    }

    public abstract String getBoostType();

    public abstract String getValue();

    public abstract String getName();

    public abstract List<String> getLore();

    public String getDurationAsString() {
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

    public static void updateID(int oldId) {
        Main.instance.getLogger().info(oldId + " >= " + newId + " = " + (oldId >= newId));
        if (oldId >= newId) {
            newId = oldId + 1;
        }
    }
}
