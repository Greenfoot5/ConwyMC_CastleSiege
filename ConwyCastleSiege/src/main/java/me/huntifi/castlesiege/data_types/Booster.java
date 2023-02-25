package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

public abstract class Booster implements Comparable<Booster> {
    public int id;
    public int duration;
    public final Material material;

    public static int newId;

    public Booster(int duration, Material material) {
        this.id = newId;
        newId++;
        this.duration = duration;
        this.material = material;
    }

    public abstract String getBoostType();

    public abstract String getValue();

    public abstract String toString();

    public int compareTo(Booster booster) {
        return id - booster.id;
    }
}
