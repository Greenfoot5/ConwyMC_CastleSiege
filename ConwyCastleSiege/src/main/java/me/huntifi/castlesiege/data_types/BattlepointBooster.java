package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BattlepointBooster extends Booster {

    public Double multiplier = 0.0;

    public BattlepointBooster(int duration) {
        super(duration, Material.LAPIS_LAZULI);
    }

    public BattlepointBooster(int duration, double multiplier) {
        super(duration, Material.LAPIS_LAZULI);
        this.multiplier = multiplier;
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
    public List<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§3Duration: §b" + getDurationAsString());
        lore.add(" ");
        if (multiplier == 0.0) {
            lore.add("§9Modify all battlepoints earnt for the booster's duration!");
            lore.add("§9You choose what to modify them by");
            lore.add("§9    (additively applied to the current multiplier)!");
            lore.add("§9Battlepoint multiplier cannot drop below 0");
            lore.add("§9    (but can be set below 0)");
        } else {
            int percentage = multiplier > 0 ? (int)((multiplier - 1) * 100) : (int)((multiplier + 1) * 100);
            lore.add("§9Increase all battlepoints earnt by");
            lore.add("§9    " + percentage + "% for the booster's duration!");
            lore.add("§9Modifier cannot drop below 0");
        }
        return lore;
    }

    @Override
    public String getName() {
        if (multiplier == 0.0) {
            return "§9Battlepoint Booster - ID " + id;
        }
        return "§9" + new DecimalFormat("0.0").format(multiplier) + "x Battlepoint Booster - ID " + id;
    }
}
