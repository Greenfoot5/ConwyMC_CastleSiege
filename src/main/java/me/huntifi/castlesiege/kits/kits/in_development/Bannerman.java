package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bannerman extends FreeKit implements Listener {

    private static final int health = 200;
    private static final double regen = 10.5;
    private static final double meleeDamage = 30;
    private static final int ladderCount = 4;

    public Bannerman() {
        super("Bannerman", health, regen, Material.ENDER_CHEST);
        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.DARK_PURPLE + "Short-sword", List.of(""), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.DARK_PURPLE + "Short-sword",
                        List.of(""),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Armorer's robe", null, null,
                Color.fromRGB(58, 179, 218));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.QUARTZ, TrimPattern.SENTRY);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Bannerman's pants", null, null,
                Color.fromRGB(58, 179, 218));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.EYE);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Bannerman's Boots", null, null,
                Color.fromRGB(58, 179, 218));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Bannerman's Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(58, 179, 218));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.EYE);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0, true, false));
    }
}
