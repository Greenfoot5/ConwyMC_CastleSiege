package me.huntifi.castlesiege.kits.kits.team_kits.moria;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MoriaGuardian extends TeamKit implements Listener {

    public MoriaGuardian() {
        super("Guardian", 620, 15, "Moria",
                "The Dwarves", 5000, Material.SHIELD,
                "moriaguardian");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Sword", null, null, 41);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Sword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 43),
                0);

        // Shield
        es.offhand = ItemCreator.item(new ItemStack(Material.SHIELD),
                ChatColor.GREEN + "Shield", null,
                null);

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate", null, Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)));

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.NETHERITE_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)));

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0));

        // Death Messages
        super.deathMessage[0] = "You were beaten to death by ";
        super.killMessage[0] = " beat ";
        super.killMessage[1] = " to death";

        super.equipment = es;
    }

    /**
     * Whilst a guardian is blocking they get speed when they are hit by an arrow.
     * If they are not blocking and hit by an arrow they get damage resistance.
     * @param e The event called when a guardian is hit by an arrow whilst blocking
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getHitEntity() instanceof Player && e.getEntity().getShooter() instanceof Player) {
            Player hit = (Player) e.getHitEntity();
            if (Objects.equals(Kit.equippedKits.get(hit.getUniqueId()).name, name)) {
                hit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 0));
                hit.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30, 1));
            }
        }
    }

    @Override
    public ArrayList<String> getGuiDescription() {
        ArrayList<String> description = new ArrayList<>();
        description.add("§7//TODO - Add kit description");
        return description;
    }
}
