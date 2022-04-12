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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Swordsman extends Kit implements CommandExecutor {

	public Swordsman() {
		super("Swordsman");
		super.baseHealth = 120;


		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = createItem(new ItemStack(Material.IRON_SWORD),
				ChatColor.GREEN + "Iron Sword", null,
				Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 20)));
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				createItem(new ItemStack(Material.IRON_SWORD),
						ChatColor.GREEN + "Iron Sword",
						Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 22))),
				0);

		// Chestplate
		es.chest = createItem(new ItemStack(Material.IRON_CHESTPLATE),
				ChatColor.GREEN + "Iron Chestplate", null, null);

		// Leggings
		es.legs = createItem(new ItemStack(Material.IRON_LEGGINGS),
				ChatColor.GREEN + "Iron Leggings", null, null);

		// Boots
		es.feet = createItem(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots", null, null);
		// Voted Boots
		es.votedFeet = createItem(new ItemStack(Material.IRON_BOOTS),
				ChatColor.GREEN + "Iron Boots",
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

		super.equipment = es;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		super.addPlayer(((Player) commandSender).getUniqueId());
		return true;
	}
}
