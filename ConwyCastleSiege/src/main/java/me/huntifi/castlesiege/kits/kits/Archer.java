package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class Archer extends Kit implements Listener {

	public Archer() {
		super("Archer");
		super.baseHeath = 105;


		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 1;

		// Weapon
		ItemStack item = new ItemStack(Material.WOODEN_SWORD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.setUnbreakable(true);
		itemMeta.setDisplayName(ChatColor.GREEN + "Dagger");
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 16, true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.hotbar[0] = item;
		// Voted Weapon
		item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 18, true);
		item.getItemMeta().setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
		es.votedWeapon = new Tuple<>(item, 0);

		//Chestplate
		item = new ItemStack(Material.LEATHER_CHESTPLATE);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Leather Tunic");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.chest = item;

		//Leggings
		item = new ItemStack(Material.LEATHER_LEGGINGS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.legs = item;

		// Boots
		item = new ItemStack(Material.LEATHER_BOOTS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.feet = item;
		item.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
		es.votedFeet = item;

		// Ladders
		es.hotbar[2] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

		// Arrows
		item = new ItemStack(Material.ARROW, 35);
		item.getItemMeta().setLore(new ArrayList<>());

		// Bow
		item = new ItemStack(Material.BOW);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.setDisplayName(ChatColor.GREEN + "Bow");
		itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 26, true);
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);

		super.equipment = es;


		// No changes to death messages
	}
}
