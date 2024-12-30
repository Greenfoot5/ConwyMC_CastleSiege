package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.List;

/**
 * A booster that unlocks a kit for everyone for the duration
 */
public class KitBooster extends Booster {

    public String kitName;

    /**
     * @param duration How long the booster lasts for
     * @param kitName The kit to unlock
     */
    public KitBooster(int duration, String kitName) {
        super(duration, getMaterial(kitName));
        this.kitName = kitName;
    }

    @Override
    public String getBoostType() {
        return "KIT";
    }

    @Override
    public String getValue() {
        return kitName;
    }

    @Override
    public Component getName() {
        if (kitName.equalsIgnoreCase("wild")) {
            return Component.text("⁎Wild⁎", NamedTextColor.GREEN)
                    .append(Component.text("kit booster").decoration(TextDecoration.ITALIC, false));
        } else if (kitName.equalsIgnoreCase("random")) {
            return Messenger.mm.deserialize("<green><obf>!</obf>Random<obf>!</obf> kit booster");
        }
        return Component.text(kitName + " kit booster", NamedTextColor.GREEN);
    }

    @Override
    public List<Component> getLore() {
        List<Component> lore = super.getLore();
        if (kitName.equalsIgnoreCase("wild")) {
            lore.add(Component.text("Choose which coin kit gets boosted!", NamedTextColor.GREEN));
        } else if (kitName.equalsIgnoreCase("random")) {
            lore.add(Component.text("A random coin kit gets boosted!", NamedTextColor.YELLOW));
        } else {
            lore.add(Component.text(kitName + " gets boosted!", NamedTextColor.GREEN));
        }
        lore.add(Component.text("A boosted kit is unlocked for all players", NamedTextColor.DARK_AQUA));
        lore.add(Component.text("for the booster's duration!", NamedTextColor.DARK_AQUA));
        return lore;
    }

    private static Material getMaterial(String kitName) {
        if (kitName.equalsIgnoreCase("wild")) {
            return Material.SHORT_GRASS;
        } else if (kitName.equalsIgnoreCase("random")) {
            return Material.SUSPICIOUS_STEW;
        }
        Material material = Kit.getMaterial(kitName);
        if (material == null) {
            Main.instance.getLogger().severe("Invalid Booster - '" + kitName + "' is not a valid kit");
            return Material.BARRIER;
        }
        return material;
    }
}
