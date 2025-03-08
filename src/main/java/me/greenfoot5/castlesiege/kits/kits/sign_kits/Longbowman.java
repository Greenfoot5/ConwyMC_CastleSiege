package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A kit that deals more damage the further away a target is
 */
public class Longbowman extends SignKit implements Listener {

    /**
     * Creates a new Conwy longbowman
     */
    public Longbowman() {
        super("Longbowman", 220, 10, Material.BOW, 1250);


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN), null, null, 29.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Dagger", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), 31.5),
                0);

        // Regular Bow
        es.hotbar[1] = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN), null, null,
                Color.fromRGB(255, 255, 255));

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
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
        if (e.getEntity().getShooter() != equippedPlayer)
            return;
        if (e.getHitEntity() == null)
            return;

        if (e.getEntity() instanceof AbstractArrow arrow) {

            Entity hit = e.getHitEntity();

            Location shooterLoc = equippedPlayer.getLocation();
            Location hitLoc = hit.getLocation();

            double distance = shooterLoc.distanceSquared(hitLoc);

            arrow.setDamage(getArrowDamage(distance));
        }
    }

    private static int getArrowDamage(double distance) {
        int damage;
        if (distance < 10 * 10) {
            damage = 14;
        } else if (distance <= 15 * 15) {
            damage = 15;
        } else if (distance <= 20 * 20) {
            damage = 16;
        } else if (distance <= 25 * 25) {
            damage = 18;
        } else if (distance <= 30 * 30) {
            damage = 20;
        } else if (distance <= 35 * 35) {
            damage = 22;
        } else if (distance <= 40 * 40) {
            damage = 26;
        } else if (distance <= 50 * 50) {
            damage = 30;
        } else if (distance <= 60 * 60) {
            damage = 34;
        } else {
            damage = 40;
        }
        return damage;
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
