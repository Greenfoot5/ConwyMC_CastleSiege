package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

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
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("§3Duration: §b" + getDurationAsString());
        lore.add(" ");
        lore.add("§6Increase all coins earnt by " + multiplier * 100 + "%");
        lore.add("§6    for the booster's duration!");
        return lore;
    }

    @Override
    public String getName() {
        return "§e" + getPercentage() + "% Coin Booster - ID " + id;
    }

    public int getPercentage() {
        return (int)(multiplier * 100);
    }
}
