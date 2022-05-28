package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

/**
 * The scout kit
 */
public class Scout extends VoterKit {

    /**
     * Set the equipment and attributes of this kit
     */
    public Scout() {
        super("Scout", 100);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.WOODEN_SWORD),
                ChatColor.GREEN + "Shortsword", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 18)));
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.WOODEN_SWORD),
                        ChatColor.GREEN + "Shortsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20))),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(64, 87, 1));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(42, 57, 3));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null,
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1)),
                Color.fromRGB(64, 87, 1));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3)),
                Color.fromRGB(64, 87, 1));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 6);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 8), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
    }
}
