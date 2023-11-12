package me.huntifi.castlesiege.kits.kits.team_kits.conwy;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Objects;

public class ConwyLongbowman extends TeamKit implements Listener {

    public ConwyLongbowman() {
        super("Conwy Longbowman", 220, 10, "Conwy",
                "The Rebellion", 2500, Material.BOW, "ranged");


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                ChatColor.GREEN + "Dagger", null, null, 29.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        ChatColor.GREEN + "Dagger",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 31.5),
                0);

        // Regular Bow
        es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
                ChatColor.GREEN + "Bow", null, null);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(255, 255, 255));

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

        // Arrows
        es.hotbar[4] = new ItemStack(Material.ARROW, 20);

        super.equipment = es;
    }

    /**
     * Set the arrow-damage of a Moria Orc's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow &&
                e.getEntity().getShooter() instanceof Player &&
                Objects.equals(Kit.equippedKits.get(((Player) e.getEntity().getShooter()).getUniqueId()).name, name)) {

            Player p = (Player) e.getEntity().getShooter();
            if (e.getHitEntity() == null) { return; }

            Entity hit = e.getHitEntity();

            Location shooterloc = p.getLocation();
            Location hitloc = hit.getLocation();

            double distance = shooterloc.distance(hitloc);

            //default value
            ((Arrow) e.getEntity()).setDamage(14);

            if (distance <= 15 && distance >= 10) {
                ((Arrow) e.getEntity()).setDamage(15);
            }

            if (distance <= 20 && distance >= 15) {
                ((Arrow) e.getEntity()).setDamage(16);
            }

            if (distance <= 25 && distance >= 20) {
                ((Arrow) e.getEntity()).setDamage(18);
            }

            if (distance <= 30 && distance >= 25) {
                ((Arrow) e.getEntity()).setDamage(20);
            }

            if (distance <= 35 && distance >= 30) {
                ((Arrow) e.getEntity()).setDamage(22);
            }

            if (distance <= 40 && distance >= 35) {
                ((Arrow) e.getEntity()).setDamage(26);
            }

            if (distance <= 50 && distance >= 40) {
                ((Arrow) e.getEntity()).setDamage(30);
            }

            if (distance <= 60 && distance >= 50) {
                ((Arrow) e.getEntity()).setDamage(34);
            }

            if (distance >= 70) {
                ((Arrow) e.getEntity()).setDamage(40);
            }
        }
    }
}
