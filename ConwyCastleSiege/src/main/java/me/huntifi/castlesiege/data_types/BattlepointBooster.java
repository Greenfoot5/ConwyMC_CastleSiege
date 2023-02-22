package me.huntifi.castlesiege.data_types;

public class BattlepointBooster extends Booster {

    public Double multiplier;

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
