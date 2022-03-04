package me.huntifi.castlesiege.Thunderstone.KitsGUI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KitsGUI_Cloudcrawlers {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	Inventory GUI = plugin.getServer().createInventory(null, 54, ChatColor.DARK_GREEN + "Kit GUI" );
	
	public Inventory KitGui(Player p) {
		
		ItemStack item = new ItemStack(Material.IRON_BLOCK);
		//meta
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta.setDisplayName(ChatColor.YELLOW + "Classic Kits");
		//lore
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.DARK_PURPLE + "Play as one of the original Dark Age kits!");
		lore1.add(ChatColor.DARK_PURPLE + "However certain kits will have to be unlocked");
		lore1.add(ChatColor.DARK_PURPLE + "with coins first.");
		itemMeta.setLore(lore1);
		item.setItemMeta(itemMeta);
		
		ItemStack item2 = new ItemStack(Material.LAPIS_BLOCK);
		//meta
		ItemMeta itemMeta2 = item2.getItemMeta();
		itemMeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta2.setDisplayName(ChatColor.YELLOW + "Voter Kits");
		//lore
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.DARK_PURPLE + "Play as one of the voter kits,");
		lore2.add(ChatColor.DARK_PURPLE + "requires you to vote first.");
		itemMeta2.setLore(lore2);
		item2.setItemMeta(itemMeta2);
		
		ItemStack item3 = new ItemStack(Material.EMERALD_BLOCK);
		//meta
		ItemMeta itemMeta3 = item3.getItemMeta();
		itemMeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta3.setDisplayName(ChatColor.YELLOW + "Secret Kits");
		//lore
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.DARK_PURPLE + "Play as one of the secret kits,");
		lore3.add(ChatColor.DARK_PURPLE + "requires you to find their respective secrets first.");
		itemMeta3.setLore(lore3);
		item3.setItemMeta(itemMeta3);
		
		ItemStack panel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta panelMeta = panel.getItemMeta();
		panelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		panelMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "");
		ArrayList<String> panelLore = new ArrayList<String>();
		panelMeta.setLore(panelLore);
		panel.setItemMeta(panelMeta);
		
		GUI.setItem(0, panel); GUI.setItem(1, panel); GUI.setItem(2, panel); GUI.setItem(3, panel); GUI.setItem(4, panel);
		GUI.setItem(5, panel); GUI.setItem(6, panel); GUI.setItem(7, panel); GUI.setItem(8, panel); GUI.setItem(9, panel);
		GUI.setItem(10, item); GUI.setItem(11, panel); GUI.setItem(12, panel); GUI.setItem(13, item2); GUI.setItem(14, panel);
		GUI.setItem(15, panel); GUI.setItem(16, item3); GUI.setItem(17, panel); GUI.setItem(18, panel); GUI.setItem(19, panel); 
		GUI.setItem(20, panel); GUI.setItem(21, panel); GUI.setItem(22, panel); GUI.setItem(23, panel); GUI.setItem(24, panel);
		GUI.setItem(25, panel); GUI.setItem(26, panel); GUI.setItem(27, panel); GUI.setItem(28, panel); GUI.setItem(29, panel);
		GUI.setItem(30, panel); GUI.setItem(31, panel); GUI.setItem(32, panel); GUI.setItem(33, panel); GUI.setItem(34, panel);
		GUI.setItem(35, panel); GUI.setItem(36, panel); GUI.setItem(37, panel); GUI.setItem(38, panel); GUI.setItem(39, panel);
		GUI.setItem(40, panel); GUI.setItem(41, panel); GUI.setItem(42, panel); GUI.setItem(43, panel); GUI.setItem(44, panel);
		GUI.setItem(45, panel); GUI.setItem(46, panel); GUI.setItem(47, panel); GUI.setItem(48, panel); GUI.setItem(49, panel);
		GUI.setItem(50, panel); GUI.setItem(51, panel); GUI.setItem(52, panel); GUI.setItem(53, panel);
		
		
		return GUI;	
	}

}
