package me.huntifi.castlesiege.kits.kits.team_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class HelmsDeepRangedCavalry extends TeamKit implements Listener {
    
    public HelmsDeepRangedCavalry() {
        super("Ranged Cavalry", 230, 9, "Helm's Deep",
                "Rohan", 2500, 0, Material.BOW);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Longsword", null, null, 29.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Longsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 31.5),
                0);

        // Regular Bow
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Bow", null, null);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                ChatColor.GREEN + "Bronze Chestplate", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[2] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

        // Horse
        es.hotbar[3] = ItemCreator.item(new ItemStack(Material.WHEAT),
                ChatColor.GREEN + "Spawn Horse", null, null);
        HorseHandler.add(name, 800, 100, 1, 0.2025, 0.8,
                Material.LEATHER_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)
                )
        );

        // Arrows
        es.hotbar[4] = new ItemStack(Material.ARROW, 20);

        super.equipment = es;
    }

    /**
     * Set the arrow-damage of a ranged cavalry's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler (priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {
            ((Arrow) e.getEntity()).setDamage(12);
        }
    }
}
