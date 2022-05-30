package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * Serves as a tool to easily create items for kits
 */
public class ItemCreator {

    /**
     * Create a specified item
     * @param item The item to apply all flags and parameters to
     * @param name The name for the item
     * @param lore The lore for the item
     * @param enchants The enchantments for the item
     * @return The item with all flags and parameters applied
     */
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

    public static ItemStack weapon(ItemStack item, String name, List<String> lore,
                                   List<Tuple<Enchantment, Integer>> enchants, double damage) {
        return setDamage(item(item, name, lore, enchants), damage);

    }

    /**
     * Create a specified piece of leather armor
     * @param item The item to apply all flags and parameters to
     * @param name The name for the item
     * @param lore The lore for the item
     * @param enchants The enchantments for the item
     * @param color The color for the item
     * @return The item with all flags and parameters applied
     */
    public static ItemStack leatherArmor(ItemStack item, String name, List<String> lore,
                                         List<Tuple<Enchantment, Integer>> enchants, Color color) {
        ItemStack leatherItem = item(item, name, lore, enchants);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) leatherItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setColor(color);
        leatherItem.setItemMeta(itemMeta);
        return leatherItem;
    }

    private static ItemStack setDamage(ItemStack item, double damage) {
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("SetDamage", damage, AttributeModifier.Operation.ADD_NUMBER));
        item.setItemMeta(meta);
        return item;
    }
}
