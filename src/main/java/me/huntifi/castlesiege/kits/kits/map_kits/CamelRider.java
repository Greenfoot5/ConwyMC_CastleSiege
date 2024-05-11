package me.huntifi.castlesiege.kits.kits.map_kits;

import me.huntifi.castlesiege.events.gameplay.CamelHandler;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.MapKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A kit that can summon and ride a camel
 */
public class CamelRider extends MapKit implements Listener {

    private static final int health = 360;
    private static final double regen = 10.5;
    private static final double meleeDamage = 43;
    private static final int ladderCount = 4;
    private static final int camelHealth = 440;

    /**
     * Creates a new camel rider
     */
    public CamelRider() {
        super("CamelRider", health, regen, Material.SAND, "Abrakhan", "Camel Rider");

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Scimitar", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Scimitar", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.SWEEPING_EDGE, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                Component.text("Chainmail Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Horse
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Camel", NamedTextColor.GREEN), null, null);
        CamelHandler.add(name, 600, camelHealth, 2, 0.23, 0.01,
                Collections.singletonList(new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)));

        super.equipment = es;

    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Can summon a camel to ride on", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(camelHealth, color)
                        .append(Component.text(" Camel HP", color)));
        return kitLore;
    }
}
