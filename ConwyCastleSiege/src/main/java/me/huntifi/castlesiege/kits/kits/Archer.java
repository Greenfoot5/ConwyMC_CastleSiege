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
 * The archer kit
 */
public class Archer extends Kit {

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Archer() {
		super("Archer", 130, 6);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 1;

		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
				ChatColor.GREEN + "Dagger", null, null, 33);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
						ChatColor.GREEN + "Dagger",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 35),
				0);

		// Chestplate
		es.chest = ItemCreator.item(new ItemStack(Material.LEATHER_CHESTPLATE),
				ChatColor.GREEN + "Leather Tunic", null, null);

		// Leggings
		es.legs = ItemCreator.item(new ItemStack(Material.LEATHER_LEGGINGS),
				ChatColor.GREEN + "Leather Leggings", null, null);

		// Boots
		es.feet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
				ChatColor.GREEN + "Leather Boots", null, null);
		// Voted Boots
		es.votedFeet = ItemCreator.item(new ItemStack(Material.LEATHER_BOOTS),
				ChatColor.GREEN + "Leather Boots",
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[2] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

		// Arrows
		es.hotbar[7] = new ItemStack(Material.ARROW, 35);

		// Bow
		es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
				ChatColor.GREEN + "Bow", null,
				Collections.singletonList(new Tuple<>(Enchantment.ARROW_DAMAGE, 14)));

		super.equipment = es;
	}
}
