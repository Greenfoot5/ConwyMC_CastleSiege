package me.huntifi.castlesiege.data_types;

public abstract class Booster {
    public int id;
    public int duration;

    public static int newId;

    public Booster(int duration) {
        this.id = newId;
        newId++;
        this.duration = duration;
    }

    public abstract String getBoostType();

    public abstract String getValue();
}
