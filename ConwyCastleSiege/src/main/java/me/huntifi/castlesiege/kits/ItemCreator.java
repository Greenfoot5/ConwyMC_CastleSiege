package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class ItemCreator {
    public static ItemStack item(ItemStack item, String name, List<String> lore,
                                 List<Tuple<Enchantment, Integer>> enchants) {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        if (enchants != null) {
            for (Tuple<Enchantment, Integer> e : enchants) {
                itemMeta.addEnchant(e.getFirst(), e.getSecond(), true);
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack leatherArmor(ItemStack item, String name, List<String> lore,
                                         List<Tuple<Enchantment, Integer>> enchants, Color color) {
        ItemStack leatherItem = item(item, name, lore, enchants);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) leatherItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setColor(color);
        leatherItem.setItemMeta(itemMeta);
        return leatherItem;
    }
}
