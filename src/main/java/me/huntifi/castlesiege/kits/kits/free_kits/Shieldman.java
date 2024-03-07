package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.castlesiege.kits.kits.VoterKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The shieldman kit
 */
public class Shieldman extends FreeKit {

    private static final int health = 400;
    private static final double regen = 14;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;

    /**
     * Set the equipment and attributes of this kit
     */
    public Shieldman() {
        super("Shieldman", health, regen, Material.SHIELD);
        super.kbResistance = 0.5;

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Longsword", null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Longsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
                0);

        // Shield
        es.offhand = ItemCreator.item(new ItemStack(Material.SHIELD),
                ChatColor.GREEN + "Shield", null,
                Collections.singletonList(new Tuple<>(Enchantment.KNOCKBACK, 1)));

        // Chestplate
        es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
                ChatColor.GREEN + "Iron Chestplate Tunic", null, null);

        // Leggings
        es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.GREEN + "Chainmail Leggings", null, null);

        // Boots
        es.feet = ItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                ChatColor.GREEN + "Diamond Chestplate Boots", null, null);
        // Voted Boots
        es.votedFeet = ItemCreator.item(new ItemStack(Material.DIAMOND_BOOTS),
                ChatColor.GREEN + "Iron Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        super.equipment = es;

        // Perm Potion Effects
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0));
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Basic tank equipped with a", NamedTextColor.GRAY));
        kitLore.add(Component.text("sword and shield", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(" "));
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Mining Fatigue I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Slowness I", NamedTextColor.GRAY));
        return kitLore;
    }
}
