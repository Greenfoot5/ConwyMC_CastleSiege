package me.huntifi.castlesiege.kits.kits.team_kits.conwy;

import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A kit that deals more damage the further away a target is
 */
public class ConwyLongbowman extends TeamKit implements Listener {

    /**
     * Creates a new Conwy longbowman
     */
    public ConwyLongbowman() {
        super("Conwy Longbowman", 220, 10, "Conwy",
                "The Rebellion", 2500, Material.BOW, "conwylongbowman");


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Dagger", NamedTextColor.GREEN), null, null, 29.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Dagger", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 31.5),
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
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
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

            Location shooterLoc = p.getLocation();
            Location hitLoc = hit.getLocation();

            double distance = shooterLoc.distance(hitLoc);

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

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
