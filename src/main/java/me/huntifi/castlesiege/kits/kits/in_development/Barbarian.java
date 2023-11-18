package me.huntifi.castlesiege.kits.kits.in_development;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 */
public class Barbarian extends DonatorKit implements Listener {

    private static final int health = 260;
    private static final double regen = 10.5;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;

    public Barbarian() {
        super("Barbarian", health, regen, Material.NETHERITE_AXE);

        super.canSeeHealth = true;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                ChatColor.GREEN + "Battle Axe", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.NETHERITE_AXE),
                        ChatColor.GREEN + "Battle Axe",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(209, 112, 0));
        ItemMeta legs = es.legs.getItemMeta();
        ArmorMeta aleg = (ArmorMeta) legs;
        assert legs != null;
        ArmorTrim trimleg = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.TIDE);
        aleg.setTrim(trimleg);
        es.legs.setItemMeta(aleg);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta boots = es.feet.getItemMeta();
        ArmorMeta aboot = (ArmorMeta) boots;
        assert boots != null;
        ArmorTrim boottrim = new ArmorTrim(TrimMaterial.COPPER, TrimPattern.RIB);
        aboot.setTrim(boottrim);
        es.feet.setItemMeta(aboot);
        es.votedFeet.setItemMeta(aboot);

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0, true, false));

    }


    @EventHandler
    public void onIncreasedDamage(EntityDamageByEntityEvent e) {
        // Both are players
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoHit = (Player) e.getDamager();

            double formula = (health/whoHit.getHealth());
            // Barbarian increased damage
            if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name)) {
                if (formula < 3) {
                    e.setDamage(meleeDamage * formula);
                } else {
                    e.setDamage(meleeDamage * 3);
                }
            }
        } else if (e.getEntity() instanceof Attributable && e.getDamager() instanceof Player) {
            Player whoHit = (Player) e.getDamager();

            // Barbarian increased damage
            if (Objects.equals(Kit.equippedKits.get(whoHit.getUniqueId()).name, name)) {
                double formula = (health/whoHit.getHealth());
                if (formula < 3) {
                    e.setDamage(meleeDamage * formula);
                } else {
                    e.setDamage(meleeDamage * 3);
                }
            }
        }
    }

    public static ArrayList<String> loreStats() {
        ArrayList<String> kitLore = new ArrayList<>();
        kitLore.add("§7Fast axe wielding warrior that deals");
        kitLore.add("§7more damage when they are low on health.");
        kitLore.add(" ");
        kitLore.add("§a" + health + " §7HP");
        kitLore.add("§a" + meleeDamage + " §7Melee DMG");
        kitLore.add("§a" + regen + " §7Regen");
        kitLore.add("§a" + ladderCount + " §7Ladders");
        kitLore.add("§5Effects:");
        kitLore.add("§7- Speed I");
        kitLore.add("");
        kitLore.add("§6Passive: ");
        kitLore.add("§7- Melee damage is increased depending");
        kitLore.add("§7on how much health you have left.");
        kitLore.add("§7Maximum damage dealt is 108 DMG.");
        kitLore.add("");
        kitLore.add("§7Can be unlocked with §e§lcoins");
        return kitLore;
    }
}
