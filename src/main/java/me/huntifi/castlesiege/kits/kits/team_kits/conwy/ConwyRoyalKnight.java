package me.huntifi.castlesiege.kits.kits.team_kits.conwy;

import me.huntifi.castlesiege.events.gameplay.HorseHandler;
import me.huntifi.castlesiege.kits.items.CSItemCreator;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import me.huntifi.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A kit that has a royal horse
 */
public class ConwyRoyalKnight extends TeamKit {

    /**
     * Creates a new Royal Knight
     */
    public ConwyRoyalKnight() {
        super("Royal Knight", 600, 5, "Conwy", "The English",
                5000, Material.DIAMOND_HORSE_ARMOR, "conwyroyalknight");


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Sword", NamedTextColor.GREEN), null, null, 35.5);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 37.5),
                0);

        // Shield
        es.offhand = CSItemCreator.item(new ItemStack(Material.SHIELD),
                Component.text("Shield", NamedTextColor.GREEN), null, null);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.NETHERITE_CHESTPLATE),
                Component.text("Reinforced Iron Chestplate", NamedTextColor.GREEN), null, null);

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null,
                Collections.singletonList(new Tuple<>(Enchantment.PROTECTION_PROJECTILE, 2)),
                Color.fromRGB(174, 26, 26));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Horse
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Royal Steed", NamedTextColor.GREEN), null, null);
        HorseHandler.add(name, 600, 200, 1, 0.2425, 1.1,
                Material.DIAMOND_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP, 999999, 0),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 4)
                )
        );

        super.equipment = es;
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
