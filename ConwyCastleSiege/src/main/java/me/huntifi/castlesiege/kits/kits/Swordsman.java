package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * The swordsman kit
 */
public class Swordsman extends Kit {

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Swordsman() {
		super("Swordsman", 200, 5.5);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = ItemCreator.item(new ItemStack(Material.IRON_SWORD),
				ChatColor.GREEN + "Iron Sword", null,
				Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20)));
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				ItemCreator.item(new ItemStack(Material.IRON_SWORD),
						ChatColor.GREEN + "Iron Sword",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 22))),
				0);

		// Chestplate
		es.chest = ItemCreator.item(new ItemStack(Material.IRON_CHESTPLATE),
				ChatColor.GREEN + "Iron Chestplate", null, null);

		// Leggings
		es.legs = ItemCreator.item(new ItemStack(Material.IRON_LEGGINGS),
				ChatColor.GREEN + "Iron Leggings", null, null);

		// Boots
		es.feet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots", null, null);
		// Voted Boots
		es.votedFeet = ItemCreator.item(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots",
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

		super.equipment = es;
	}
}
