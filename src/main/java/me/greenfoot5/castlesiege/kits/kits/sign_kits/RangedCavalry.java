package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import me.greenfoot5.castlesiege.events.gameplay.HorseHandler;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ranged Cavalry Kit
 */
public class RangedCavalry extends SignKit implements Listener {

    private static final int health = 230;
    private static final double regen = 9;
    private static final double meleeDamage = 29.5;
    private static final int ladderCount = 4;
    private static final int horseHealth = 200;
    private static final int arrowDamage = 16;

    /**
     * Creates a new Ranged Cavalry instance
     */
    public RangedCavalry() {
        super("Ranged Cavalry", health, regen, Material.BOW, 2500);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                Component.text("Sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(meleeDamage + " Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
                        Component.text("Sword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text((meleeDamage + 2) + " Melee Damage", NamedTextColor.DARK_GREEN),
                                Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.GREEN)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), meleeDamage + 2),
                0);

        // Regular Bow
        es.offhand = CSItemCreator.item(new ItemStack(Material.BOW),
                Component.text("Bow", NamedTextColor.GREEN),
                List.of(Component.empty(),
                Component.text("Arrow Damage: " + arrowDamage + "+", NamedTextColor.DARK_GREEN)), null);

        // Chestplate
        es.chest = CSItemCreator.item(new ItemStack(Material.GOLDEN_CHESTPLATE),
                Component.text("Bronze Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Leggings
        es.legs = CSItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.IRON_BOOTS),
                Component.text("Iron Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.GREEN)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount), 2);

        // Horse
        es.hotbar[2] = CSItemCreator.item(new ItemStack(Material.WHEAT),
                Component.text("Spawn Horse", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("<< Right Click To Summon >>", NamedTextColor.DARK_GRAY),
                        Component.text("Summons your horse, with stats:", NamedTextColor.GRAY),
                        Component.text("- HP: " + horseHealth , NamedTextColor.GRAY),
                        Component.text("- Speed: 0.2225", NamedTextColor.GRAY),
                        Component.text("- Jump Strength: 0.7", NamedTextColor.GRAY)), null);
        HorseHandler.add(name, 600, horseHealth, 1, 0.2225, 0.7,
                Material.GOLDEN_HORSE_ARMOR, Arrays.asList(
                        new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 1),
                        new PotionEffect(PotionEffectType.REGENERATION, 999999, 0)
                ), "regular");

        // Arrows
        es.hotbar[4] = new ItemStack(Material.ARROW, 32);

        super.equipment = es;
    }

    /**
     * Set the arrow-damage of a ranged cavalry's arrows
     * @param e The event called when a player is hit by an arrow
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() != equippedPlayer)
            return;

        if (e.getEntity() instanceof AbstractArrow) {
            ((AbstractArrow) e.getEntity()).setDamage(arrowDamage);
        }
    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> description = new ArrayList<>();
        description.add(Component.text("//TODO - Add kit description", NamedTextColor.GRAY));
        return description;
    }
}
