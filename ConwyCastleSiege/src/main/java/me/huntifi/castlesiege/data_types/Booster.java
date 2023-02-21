package me.huntifi.castlesiege.data_types;

import java.util.UUID;

public class Booster {
    public final int id;
    public UUID uuid;
    public BoosterType type;
    public int duration;
    public double amount;

    public Booster(int id, String type, int duration, double amount) {
        this.id = id;
        this.type = BoosterType.valueOf(type);
        this.duration = duration;
        this.amount = amount;
    }

    public enum BoosterType{
        COIN,
        BATTLEPOINT
    }
}
