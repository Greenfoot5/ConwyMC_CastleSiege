package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class KitBooster extends Booster {

    public String kitName;

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
            return Component.text("Wild", NamedTextColor.GREEN).decorate(TextDecoration.ITALIC)
                    .append(Component.text("kit booster - ID " + id).decoration(TextDecoration.ITALIC, false));
        } else if (kitName.equalsIgnoreCase("random")) {
            return MiniMessage.miniMessage().deserialize("<green><obf>!</obf>Random<obf>!</obf> kit booster - ID " + id);
        }
        return Component.text(kitName + " kit booster - ID " + id, NamedTextColor.GREEN);
    }

    @Override
    public List<Component> getLore() {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Duration: ", NamedTextColor.DARK_AQUA)
                .append(Component.text(getDurationAsString()).decorate(TextDecoration.BOLD)));
        lore.add(Component.empty());
        if (kitName.equalsIgnoreCase("wild")) {
            lore.add(Component.text("Choose which elite kit gets boosted!", NamedTextColor.GREEN));
        } else if (kitName.equalsIgnoreCase("random")) {
            lore.add(Component.text("A random elite kit gets boosted!", NamedTextColor.YELLOW));
        } else {
            lore.add(Component.text(kitName + " gets boosted!", NamedTextColor.GREEN));
        }
        lore.add(Component.text("A boosted kit is unlocked for all players", NamedTextColor.DARK_AQUA));
        lore.add(Component.text("    for the booster's duration!", NamedTextColor.DARK_AQUA));
        return lore;
    }

    private static Material getMaterial(String kitName) {
        if (kitName.equalsIgnoreCase("wild")) {
            return Material.SHORT_GRASS;
        } else if (kitName.equalsIgnoreCase("random")) {
            return Material.COOKIE;
        }
        Material material = Kit.getMaterial(kitName);
        if (material == null) {
            Main.instance.getLogger().severe("Invalid Booster - '" + kitName + "' is not a valid kit");
            return Material.BARRIER;
        }
        return material;
    }
}
