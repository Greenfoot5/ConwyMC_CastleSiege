package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

public class BattlepointBooster extends Booster {

    public Double multiplier = 0.0;

    public BattlepointBooster(int duration) {
        super(duration, Material.LAPIS_LAZULI);
    }

    @Override
    public String getBoostType() {
        return "BATTLEPOINT";
    }

    @Override
    public String getValue() {
        return multiplier.toString();
    }

    @Override
    public String toString() {
        return "§9Battlepoint Booster";
    }
}
