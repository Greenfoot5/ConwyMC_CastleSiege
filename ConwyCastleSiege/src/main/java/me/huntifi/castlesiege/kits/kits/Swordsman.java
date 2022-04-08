package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Swordsman extends Kit implements CommandExecutor {

	public Swordsman() {
		super("Swordsman");
		super.baseHeath = 120;


		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		ItemStack item = new ItemStack(Material.IRON_SWORD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemMeta.setUnbreakable(true);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.hotbar[0] = item.clone();
		// Voted Weapon
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 24, true);
		itemMeta.setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
		item.setItemMeta(itemMeta);
		es.votedWeapon = new Tuple<>(item.clone(), 0);

		// Chestplate
		item = new ItemStack(Material.IRON_CHESTPLATE);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Chestplate");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.chest = item;

		// Leggings
		item = new ItemStack(Material.IRON_LEGGINGS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Leggings");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.legs = item;

		// Boots
		item = new ItemStack(Material.IRON_BOOTS);
		itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");
		itemMeta.setUnbreakable(true);
		itemMeta.setLore(new ArrayList<>());
		item.setItemMeta(itemMeta);
		es.feet = item.clone();
		// Voted Boots
		itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
		item.setItemMeta(itemMeta);
		es.votedFeet = item.clone();

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

		super.equipment = es;


		// Death Messages
		super.projectileDeathMessage = super.deathMessage;
		super.projectileKillMessage = super.killMessage;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		super.addPlayer(((Player) commandSender).getUniqueId());
		return true;
	}
}
