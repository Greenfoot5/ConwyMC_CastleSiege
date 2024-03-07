package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The swordsman kit
 */
public class Swordsman extends FreeKit {
	private static final int health = 390;
	private static final double regen = 10.5;
	private static final double meleeDamage = 43;
	private static final int ladderCount = 4;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Swordsman() {
		super("Swordsman", health, regen, Material.IRON_SWORD);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
				Component.text("Iron Sword", NamedTextColor.GREEN), null, null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
						Component.text("Iron Sword", NamedTextColor.GREEN),
						Collections.singletonList(Component.text("- voted: +2 damage", NamedTextColor.AQUA)),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
				0);

		// Chestplate
		es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
				Component.text("Iron Chestplate", NamedTextColor.GREEN), null, null);

		// Leggings
		es.legs = ItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
				Component.text("Iron Leggings", NamedTextColor.GREEN), null, null);

		// Boots
		es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN), null, null);
		// Voted Boots
		es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				Component.text("Iron Boots", NamedTextColor.GREEN),
				Collections.singletonList(Component.text("- voted: Depth Strider II", NamedTextColor.AQUA)),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

		super.equipment = es;
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	@Override
    public ArrayList<Component> getGuiDescription() {
		ArrayList<Component> kitLore = new ArrayList<>();
		kitLore.add(Component.text("Standard melee kit", NamedTextColor.GRAY));
		kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
		return kitLore;
	}
}
