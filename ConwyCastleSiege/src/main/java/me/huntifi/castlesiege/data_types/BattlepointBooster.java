package me.huntifi.castlesiege.data_types;

public class BattlepointBooster extends Booster {

    public Double multiplier = 0.0;

    public BattlepointBooster(int duration) {
        super(duration);
    }

    @Override
    public String getBoostType() {
        return "BATTLEPOINT";
    }

    @Override
    public String getValue() {
        return multiplier.toString();
    }
}
