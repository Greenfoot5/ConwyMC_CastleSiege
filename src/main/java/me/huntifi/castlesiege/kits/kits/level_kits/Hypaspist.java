package me.huntifi.castlesiege.kits.kits.level_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.LevelKit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Objects;

public class Hypaspist extends LevelKit implements Listener {

    private static final int health = 460;
    private static final double regen = 10.5;
    private static final double meleeDamage = 34;
    private static final int ladderCount = 4;

    public Hypaspist() {
        super("Hypaspist", health, regen, Material.GOLDEN_CHESTPLATE, "Tank", 20);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Shortsword", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Shortsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Weapon
        es.offhand = ItemCreator.weapon(new ItemStack(Material.SHIELD, 1),
                ChatColor.GREEN + "Concave Shield",
                Collections.singletonList(ChatColor.AQUA + "Right-click to throw a spear."),
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 0)) , 10);

        // Weapon
        es.hotbar[1] = ItemCreator.weapon(new ItemStack(Material.TRIDENT),
                ChatColor.GREEN + "Sarissa", null, null, meleeDamage);

        // Chestplate + trim
        es.chest = ItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                ChatColor.GREEN + "Copper Chestplate", null, null);
        ItemMeta chest = es.chest.getItemMeta();
        ArmorMeta ameta = (ArmorMeta) chest;
        assert chest != null;
        ArmorTrim trim = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) chest).setTrim(trim);
        es.chest.setItemMeta(ameta);

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(183, 12, 12));

        // Boots + trim
        es.feet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "copper Boots", null, null);
        ItemMeta feet = es.feet.getItemMeta();
        ArmorMeta ametafeet = (ArmorMeta) feet;
        assert feet != null;
        ArmorTrim trimfeet = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) feet).setTrim(trimfeet);
        es.feet.setItemMeta(ametafeet);

        // Voted Boots + trim
        es.votedFeet = ItemCreator.item(new ItemStack(Material.GOLDEN_BOOTS),
                ChatColor.GREEN + "Copper Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));
        ItemMeta votedfeet = es.votedFeet.getItemMeta();
        ArmorMeta ametavotedfeet = (ArmorMeta) votedfeet;
        assert votedfeet != null;
        ArmorTrim trimvotedfeet = new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE);
        ((ArmorMeta) votedfeet).setTrim(trimvotedfeet);
        es.votedFeet.setItemMeta(ametavotedfeet);

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

        super.equipment = es;

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));

        // Death Messages
        super.projectileDeathMessage[0] = "You were impaled by ";
        super.projectileKillMessage[0] = " impaled ";
    }

    /**
     * Set the arrow-damage of a ranger's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onTridentHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Trident &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Trident) e.getEntity()).setDamage(39);
        }
    }
}
