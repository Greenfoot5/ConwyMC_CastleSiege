package me.huntifi.castlesiege.data_types;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a battlepoint booster that modifies the battlepoint amount for a duration
 */
public class BattlepointBooster extends Booster {

    public Double multiplier = 0.0;

    /**
     * Defines a booster that allows a player to modify the multiplier
     * @param duration The duration of the booster
     */
    public BattlepointBooster(int duration) {
        super(duration, Material.LAPIS_LAZULI);
    }

    /**
     * Defines a booster with a preset multiplier
     * @param duration The duration of the booster
     * @param multiplier The multiplier of the booster
     */
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
            lore.add("§9Increase all battlepoints earnt by");
            lore.add("§9    " + getPercentage() + "% for the booster's duration!");
            lore.add("§9Modifier cannot drop below 0");
        }
        return lore;
    }

    /**
     * @return The multiplier as a percentage
     */
    public int getPercentage() {
        return (int)(multiplier * 100);
    }

    @Override
    public String getName() {
        if (multiplier == 0.0) {
            return "§9Battlepoint Booster - ID " + id;
        }
        return "§9" + getPercentage() + "% Battlepoint Booster - ID " + id;
    }
}
