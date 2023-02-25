package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Material;

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
    public String toString() {
        return "§a" + kitName + " kit booster";
    }

    private static Material getMaterial(String kitName) {
        if (kitName.equalsIgnoreCase("wild")) {
            return Material.GRASS;
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
