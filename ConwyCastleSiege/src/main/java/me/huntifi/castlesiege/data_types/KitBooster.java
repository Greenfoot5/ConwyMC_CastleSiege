package me.huntifi.castlesiege.data_types;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.kits.Kit;
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
    public String getName() {
        if (kitName.equalsIgnoreCase("wild")) {
            return "§a§oWild§a kit booster - ID " + id;
        } else if (kitName.equalsIgnoreCase("random")) {
            return "§a§k!§aRandom§k!§a kit booster - ID " + id;
        }
        return "§a" + kitName + " kit booster - ID " + id;
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("§3Duration: §b" + getDurationAsString());
        lore.add(" ");
        if (kitName.equalsIgnoreCase("wild")) {
            lore.add("§aChoose which elite kit gets boosted!");
        } else if (kitName.equalsIgnoreCase("random")) {
            lore.add("§eA random elite kit gets boosted!");
        } else {
            lore.add("§a" + kitName + " gets boosted!");
        }
        lore.add("§3A boosted kit is unlocked for all players");
        lore.add("§3    for the booster's duration!");
        return lore;
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
