package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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
import java.util.Collections;

public class Archer extends Kit implements CommandExecutor {

	public Archer() {
		super("Archer");
		super.baseHeath = 105;


		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 1;

		// Weapon
		es.hotbar[0] = createItem(new ItemStack(Material.WOODEN_SWORD),
				ChatColor.GREEN + "Dagger", null,
				Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 16)));
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				createItem(new ItemStack(Material.WOODEN_SWORD),
						ChatColor.GREEN + "Dagger",
						Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20))),
				0);

		// Chestplate
		es.chest = createItem(new ItemStack(Material.LEATHER_CHESTPLATE),
				ChatColor.GREEN + "Leather Tunic", null, null);

		// Leggings
		es.legs = createItem(new ItemStack(Material.LEATHER_LEGGINGS),
				ChatColor.GREEN + "Leather Leggings", null, null);

		// Boots
		es.feet = createItem(new ItemStack(Material.LEATHER_BOOTS),
				ChatColor.GREEN + "Leather Boots", null, null);
		// Voted Boots
		es.votedFeet = createItem(new ItemStack(Material.LEATHER_BOOTS),
				ChatColor.GREEN + "Leather Boots",
				Arrays.asList("", ChatColor.AQUA + "- voted: Depth Strider 2"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[2] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 2);

		// Arrows
		es.hotbar[7] = new ItemStack(Material.ARROW, 35);

		// Bow
		es.hotbar[1] = createItem(new ItemStack(Material.BOW),
				ChatColor.GREEN + "Bow", null,
				Collections.singletonList(new Tuple<>(Enchantment.ARROW_DAMAGE, 26)));

		super.equipment = es;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		super.addPlayer(((Player) commandSender).getUniqueId());
		return true;
	}
}
