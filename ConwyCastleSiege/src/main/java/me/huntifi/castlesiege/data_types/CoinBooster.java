package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.text.DecimalFormat;

public class CoinBooster extends Booster {

    public Double multiplier;

    public CoinBooster(int duration, double multiplier) {
        super(duration, Material.SUNFLOWER);
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

    @Override
    public String toString() {
        return "§e" + new DecimalFormat("0.0").format(multiplier) + "x Coin Booster";
    }
}
