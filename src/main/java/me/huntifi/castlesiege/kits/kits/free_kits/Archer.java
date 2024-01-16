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
 * The archer kit
 */
public class Archer extends FreeKit {
	private static final int health = 210;
	private static final double regen = 10.5;
	private static final double meleeDamage = 34;
	private static final int ladderCount = 4;
	private static final int arrowCount = 35;
	private static final int bowPowerLevel = 36;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Archer() {
		super("Archer", health, regen, Material.BOW);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 1;

		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.WOODEN_SWORD),
				ChatColor.GREEN + "Dagger", null, null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
						ChatColor.GREEN + "Dagger",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
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
		es.hotbar[2] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 2);

		// Arrows
		es.hotbar[7] = new ItemStack(Material.ARROW, arrowCount);

		// Bow
		es.hotbar[1] = ItemCreator.item(new ItemStack(Material.BOW),
				ChatColor.GREEN + "Bow", null,
				Collections.singletonList(new Tuple<>(Enchantment.ARROW_DAMAGE, bowPowerLevel)));

		super.equipment = es;
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	public static ArrayList<String> getGuiDescription() {
		ArrayList<String> kitLore = new ArrayList<>();
		kitLore.add("§7Standard ranged kit");
		kitLore.add("§7has a sword and bow.");
		kitLore.add(" ");
		kitLore.add("§a" + health + " §7HP");
		kitLore.add("§a" + meleeDamage + " §7Melee DMG");
		kitLore.add("§a" + "45+ §7Ranged DMG");
		kitLore.add("§a" + regen + " §7Regen");
		kitLore.add("§a" + ladderCount + " §7Ladders");
		kitLore.add("§a" + arrowCount + " §7Arrows");
		kitLore.add("§7Free To Play!");
		return kitLore;
	}
}
