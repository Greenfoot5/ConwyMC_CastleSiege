package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.FreeKit;
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
	private static final double regenAmount = 10.5;
	private static final double meleeDamage = 43;
	private static final int ladderCount = 4;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Swordsman() {
		super("Swordsman", health, regenAmount, Material.IRON_SWORD);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
				ChatColor.GREEN + "Iron Sword", null, null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				ItemCreator.weapon(new ItemStack(Material.IRON_SWORD),
						ChatColor.GREEN + "Iron Sword",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

		super.equipment = es;
	}

	public static ArrayList<String> loreStats() {
		ArrayList<String> kitLore = new ArrayList<>();
		kitLore.add("§7Standard melee kit");
		kitLore.add("§7has a sword.");
		kitLore.add(" ");
		kitLore.add("§a" + health + " §7HP");
		kitLore.add("§a" + meleeDamage + " §7Melee DMG");
		kitLore.add("§a" + regenAmount + " §7Regen");
		kitLore.add("§a" + ladderCount + " §7Ladders");
		kitLore.add("§7Free To Play!");
		return kitLore;
	}
}
