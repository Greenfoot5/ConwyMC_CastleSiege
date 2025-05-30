package me.greenfoot5.castlesiege.kits.items;

import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * Serves as a tool to easily create items for kits
 */
public class CSItemCreator extends ItemCreator {

    /**
     * Create a specified weapon
     * @param item The item to apply all flags and parameters to
     * @param name The name for the weapon
     * @param lore The lore for the weapon
     * @param enchants The enchantments for the weapon
     * @param damage The weapon's damage
     * @return The weapon with all flags and parameters applied
     */
    public static ItemStack weapon(ItemStack item, Component name, List<Component> lore,
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
    public static ItemStack leatherArmor(ItemStack item, Component name, List<Component> lore,
                                         List<Tuple<Enchantment, Integer>> enchants, Color color) {
        ItemStack leatherItem = item(item, name, lore, enchants);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) leatherItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.setColor(color);
        leatherItem.setItemMeta(itemMeta);
        return leatherItem;
    }

    /**
     * Sets the damage the item deals
     * @param item The item to set the damage for
     * @param damage The damage value to set
     * @return The item with the damage set
     */
    private static ItemStack setDamage(ItemStack item, double damage) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                new AttributeModifier(new NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"), damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
        item.setItemMeta(meta);
        return item;
    }
}
