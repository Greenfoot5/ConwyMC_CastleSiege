package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
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

public class Sorcerer extends DonatorKit implements Listener {

    private static final int health = 230;
    private static final double regen = 10.5;
    private static final double meleeDamage = 26;
    private static final int ladderCount = 4;
    private static final int arcaneboltCooldown = 320;
    private static final int frostnovaCooldown = 80;
    private static final int arcaneBarrageCooldown = 400;
    private static final int slowFallingCooldown = 300;
    public Sorcerer() {
        super("Sorcerer", health, regen, Material.BOOK);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STICK),
                ChatColor.DARK_PURPLE + "Sorcerer's Wand", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to shoot an arcanebolt which",
                        ChatColor.YELLOW + "does 60 DMG on hit.",
                        ChatColor.YELLOW + "Has a cooldown of " + arcaneboltCooldown/20 + " seconds."), null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STICK),
                        ChatColor.DARK_PURPLE + "Sorcerer's Wand",
                        Arrays.asList("",
                                ChatColor.YELLOW + "Right click to shoot an arcane bolt which",
                                ChatColor.YELLOW + "does 60 DMG on hit.",
                                ChatColor.YELLOW + "Has a cooldown of " + arcaneboltCooldown/20 + " seconds.",
                                ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)), meleeDamage + 2),
                0);

        // 1st ability
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.DIAMOND),
                ChatColor.LIGHT_PURPLE + "Frost nova", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to send a freezing shockwave out ",
                        ChatColor.YELLOW + "of yourself, slowing every enemy in a 4 block radius",
                        ChatColor.YELLOW + "around you and dealing 30 DMG to them.",
                        ChatColor.YELLOW + "Has a cooldown of " + frostnovaCooldown/20 + " seconds."), null);

        // 2nd ability
        es.hotbar[2] = ItemCreator.item(new ItemStack(Material.AMETHYST_SHARD),
                ChatColor.DARK_GREEN + "Arcane Barrage", Arrays.asList("",
                        ChatColor.YELLOW + "Fire 3 arcane bolts at a target.",
                        ChatColor.YELLOW + "Has a cooldown of " + arcaneBarrageCooldown/20 + " seconds."), null);

        // 3rd ability
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.FEATHER),
                ChatColor.DARK_RED + "Slow Falling", Arrays.asList("",
                        ChatColor.YELLOW + "Right click to give yourself slow falling",
                        ChatColor.YELLOW + "for 5 seconds.",
                        ChatColor.YELLOW + " ",
                        ChatColor.YELLOW + "Has a cooldown of " + slowFallingCooldown/20 + " seconds."), null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Sorcerer's robe", null, null,
                Color.fromRGB(75, 60, 63));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.DUNE);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Sorcerer's pants", null, null,
                Color.fromRGB(106, 62, 156));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Sorcerer's Boots", null, null,
                Color.fromRGB(106, 62, 156));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Sorcerer's Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(106, 62, 156));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        // Ladders
        es.hotbar[4] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 4);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0, true, false));
    }

}
