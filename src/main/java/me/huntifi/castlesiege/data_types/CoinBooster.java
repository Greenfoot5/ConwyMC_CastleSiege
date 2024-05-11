package me.huntifi.castlesiege.data_types;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

/**
 * A booster that increases the coin multiplier
 */
public class CoinBooster extends Booster {

    public final Double multiplier;

    /**
     * @param duration The duration of the booster
     * @param multiplier The multiplier to add to the coin multiplier
     */
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
    public List<Component> getLore() {
        List<Component> lore = super.getLore();
        lore.add(Component.text("Increase all coins earnt by " + multiplier * 100 + "%", NamedTextColor.GOLD));
        lore.add(Component.text("    for the booster's duration!", NamedTextColor.GOLD));
        return lore;
    }

    @Override
    public Component getName() {
        return Component.text(getPercentage() + "% Coin Booster", NamedTextColor.YELLOW);
    }

    /**
     * @return The multiplier as a percentage
     */
    public int getPercentage() {
        return (int)(multiplier * 100);
    }
}
