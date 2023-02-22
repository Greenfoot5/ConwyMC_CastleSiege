package me.huntifi.castlesiege.data_types;

public class CoinBooster extends Booster {

    public Double multiplier;

    public CoinBooster(int duration, double multiplier) {
        super(duration);
        this.multiplier = multiplier;
    }

    @Override
    public String getBoostType() {
        return "COIN";
    }

    @Override
    public String getValue() {
        return multiplier.toString();
    }
}
