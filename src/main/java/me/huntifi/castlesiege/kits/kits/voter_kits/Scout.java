package me.huntifi.castlesiege.kits.kits.voter_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The scout kit
 */
public class Scout extends VoterKit {

    private static final int health = 230;
    private static final double regen = 9;
    private static final double meleeDamage = 45;
    private static final int ladderCount = 6;

    /**
     * Set the equipment and attributes of this kit
     */
    public Scout() {
        super("Scout", health, regen, Material.LEATHER_BOOTS);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Short-sword", null, null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Short-sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 1), new Tuple<>(Enchantment.PROTECTION_FALL, 4)),
                Color.fromRGB(64, 87, 1));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Arrays.asList(new Tuple<>(Enchantment.DEPTH_STRIDER, 3), new Tuple<>(Enchantment.PROTECTION_FALL, 4)),
                Color.fromRGB(64, 87, 1));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A light and agile kit", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed II", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste II", NamedTextColor.GRAY));
        return kitLore;
    }
}
