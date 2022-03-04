package me.huntifi.castlesiege.kits.VotedKits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KitGUI_VotedKits {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	public Inventory GUI = plugin.getServer().createInventory(null, 54, ChatColor.DARK_GREEN + "Classic Kits" );
	
	public Inventory VotedKitGuiPage1() {
		
		ItemStack item = new ItemStack(Material.SHIELD);
		//meta
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Shieldman");
		//lore
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GOLD + "------------------------------");
		lore1.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A tankier melee unit with a shield.");
		lore1.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather chestplate, chainmail leggings and iron boots.");
		lore1.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Stone Sword");
		lore1.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness and damage resistance.");
		lore1.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore1.add(ChatColor.GOLD + "------------------------------");
		itemMeta.setLore(lore1);
		item.setItemMeta(itemMeta);
		
		ItemStack item2 = new ItemStack(Material.IRON_BOOTS);
		//meta
		ItemMeta itemMeta2 = item2.getItemMeta();
		itemMeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Skirmisher");
		//lore
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GOLD + "------------------------------");
		lore2.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A more mobile melee unit.");
		lore2.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Iron armor & leather leggings.");
		lore2.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Iron sword, 8 ladders.");
		lore2.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore2.add(ChatColor.GOLD + "------------------------------");
		itemMeta2.setLore(lore2);
		item2.setItemMeta(itemMeta2);
		
		ItemStack item3 = new ItemStack(Material.BLAZE_POWDER);
		//meta
		ItemMeta itemMeta3 = item3.getItemMeta();
		itemMeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta3.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Fire Archer");
		//lore
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GOLD + "------------------------------");
		lore3.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A slow archer that can put enemies on fire.");
		lore3.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor.");
		lore3.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Bow, cauldron (firepit), 48 arrows.");
		lore3.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness");
		lore3.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Can put down a firepit.");
		lore3.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " By clicking the firepit with arrows you create fire-arrows.");
		lore3.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore3.add(ChatColor.GOLD + "------------------------------");
		itemMeta3.setLore(lore3);
		item3.setItemMeta(itemMeta3);
		
		ItemStack item4 = new ItemStack(Material.LEATHER_HORSE_ARMOR);
		//meta
		ItemMeta itemMeta4 = item4.getItemMeta();
		itemMeta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta4.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Horserider");
		//lore
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GOLD + "------------------------------");
		lore4.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A light cavalry unit.");
		lore4.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor with iron boots.");
		lore4.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Iron sword, wheat.");
		lore4.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed I");
		lore4.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + " Can mount a horse when clicking with the wheat.");
		lore4.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore4.add(ChatColor.GOLD + "------------------------------");
		itemMeta4.setLore(lore4);
		item4.setItemMeta(itemMeta4);
		
		ItemStack item5 = new ItemStack(Material.LEATHER_BOOTS);
		//meta
		ItemMeta itemMeta5 = item5.getItemMeta();
		itemMeta5.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta5.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Scout");
		//lore
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GOLD + "------------------------------");
		lore5.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A very fast melee unit.");
		lore5.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather chestplate and boots.");
		lore5.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Wooden sword, 6 ladders.");
		lore5.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Speed 2.");
		lore5.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore5.add(ChatColor.GOLD + "------------------------------");
		itemMeta5.setLore(lore5);
		item5.setItemMeta(itemMeta5);
		
		ItemStack item6 = new ItemStack(Material.LADDER);
		//meta
		ItemMeta itemMeta6 = item6.getItemMeta();
		itemMeta6.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta6.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Ladderman");
		//lore
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(ChatColor.GOLD + "------------------------------");
		lore6.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A very useful kit to climb up walls fast without the need of restocking.");
		lore6.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Leather armor & chainmail leggings.");
		lore6.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + " Wooden sword & 20 ladders.");
		lore6.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + " Slowness 1");
		lore6.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Voter kit.");
		lore6.add(ChatColor.GOLD + "------------------------------");
		itemMeta6.setLore(lore6);
		item6.setItemMeta(itemMeta6);
		
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
		GUI.setItem(25, panel); GUI.setItem(26, panel); GUI.setItem(27, panel); GUI.setItem(28, item4); GUI.setItem(29, panel);
		GUI.setItem(30, panel); GUI.setItem(31, item5); GUI.setItem(32, panel); GUI.setItem(33, panel); GUI.setItem(34, item6);
		GUI.setItem(35, panel); GUI.setItem(36, panel); GUI.setItem(37, panel); GUI.setItem(38, panel); GUI.setItem(39, panel);
		GUI.setItem(40, panel); GUI.setItem(41, panel); GUI.setItem(42, panel); GUI.setItem(43, panel); GUI.setItem(44, panel);
		GUI.setItem(45, panel); GUI.setItem(46, panel); GUI.setItem(47, panel); GUI.setItem(48, panel); GUI.setItem(49, panel);
		GUI.setItem(50, panel); GUI.setItem(51, panel); GUI.setItem(52, panel); GUI.setItem(53, panel);
		
		
		return GUI;	
	}

}
