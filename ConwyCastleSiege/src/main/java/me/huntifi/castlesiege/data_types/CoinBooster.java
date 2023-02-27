package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.text.DecimalFormat;
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
        int percentage = multiplier > 0 ? (int)((multiplier - 1) * 100) : (int)((multiplier + 1) * 100);
        lore.add("§6Increase all coins earnt by " + percentage + "%");
        lore.add("§6    for the booster's duration!");
        return lore;
    }

    @Override
    public String getName() {
        return "§e" + new DecimalFormat("0.0").format(multiplier) + "x Coin Booster - ID " + id;
    }
}
