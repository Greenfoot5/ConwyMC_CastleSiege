package me.huntifi.castlesiege.data_types;

import java.util.UUID;

public class Booster {
    public final int id;
    public UUID uuid;
    public BoosterType type;
    public int duration;

    public Booster(int id, String type, int duration) {
        this.id = id;
        this.type = BoosterType.valueOf(type);
        this.duration = duration;
    }

    public enum BoosterType{
        COIN,
        BATTLEPOINT
    }
}
