package me.huntifi.castlesiege.kits;

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

public class KitGUI {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	public Inventory Kitgui(Player p) {
		
	Inventory GUI = plugin.getServer().createInventory(null, 54, ChatColor.DARK_GREEN + "Kit GUI" );
	
	ItemStack sign = new ItemStack(Material.BOW);
	//meta
	ItemMeta signMeta = sign.getItemMeta();
	signMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	signMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Archer");
	//lore
	ArrayList<String> lore1 = new ArrayList<String>();
	lore1.add(ChatColor.GOLD + "------------------------------");
	lore1.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " The standard ranged class.");
	lore1.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Full leather armor.");
	lore1.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " wooden sword, bow and 48 arrows.");
	lore1.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!");
	lore1.add(ChatColor.GOLD + "------------------------------");
	signMeta.setLore(lore1);
	sign.setItemMeta(signMeta);
	
	ItemStack FireArcher = new ItemStack(Material.BLAZE_POWDER);
	//meta
	ItemMeta FireArcherMeta = FireArcher.getItemMeta();
	FireArcherMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	FireArcherMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.BLUE + " Fire Archer");
	//lore
	ArrayList<String> lore3FireArcher = new ArrayList<String>();
	lore3FireArcher.add(ChatColor.GOLD + "------------------------------");
	lore3FireArcher.add(ChatColor.DARK_AQUA + "[Desc]:" + ChatColor.AQUA + " A archer specialised in putting players on fire.");
	lore3FireArcher.add(ChatColor.AQUA + " Has slowness I.");
	lore3FireArcher.add(ChatColor.DARK_AQUA + "[Usage]:" + ChatColor.AQUA + " Place cauldron -> right click it with arrow.");
	lore3FireArcher.add(ChatColor.DARK_AQUA + "[Armor]:" + ChatColor.AQUA + " Full leather armor.");
	lore3FireArcher.add(ChatColor.DARK_AQUA + "[Weapon]:" + ChatColor.AQUA + " Wooden sword, bow and 32 arrows.");
	lore3FireArcher.add(ChatColor.DARK_AQUA + "[Status]:" + ChatColor.AQUA + " Voter class!");
	lore3FireArcher.add(ChatColor.GOLD + "------------------------------");
	FireArcherMeta.setLore(lore3FireArcher);
	FireArcher.setItemMeta(FireArcherMeta);
	
	ItemStack signSpearman = new ItemStack(Material.STICK);
	//meta
	ItemMeta signMetaSpearman = signSpearman.getItemMeta();
	signMetaSpearman.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	signMetaSpearman.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Spearman");
	//lore
	ArrayList<String> loreSpearman = new ArrayList<String>();
	loreSpearman.add(ChatColor.GOLD + "------------------------------");
	loreSpearman.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " A soldier that can throw spears.");
	loreSpearman.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Full chainmail armor.");
	loreSpearman.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " 5x Spear, 4x ladders.");
	loreSpearman.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!");
	loreSpearman.add(ChatColor.GOLD + "------------------------------");
	signMetaSpearman.setLore(loreSpearman);
	signSpearman.setItemMeta(signMetaSpearman);
	
	ItemStack starShield = new ItemStack(Material.SHIELD);
	//meta
	ItemMeta starMetaShield = starShield.getItemMeta();
	starMetaShield.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	starMetaShield.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.BLUE + " Shieldman");
	//lore
	ArrayList<String> lore3Shield = new ArrayList<String>();
	lore3Shield.add(ChatColor.GOLD + "------------------------------");
	lore3Shield.add(ChatColor.DARK_AQUA + "[Desc]:" + ChatColor.AQUA + " A melee warrior with a shield.");
	lore3Shield.add(ChatColor.AQUA + " Has slowness I.");
	lore3Shield.add(ChatColor.DARK_AQUA + "[Armor]:" + ChatColor.AQUA + " Leather chestplate, chainmail leggings ,iron boots.");
	lore3Shield.add(ChatColor.DARK_AQUA + "[Weapon]:" + ChatColor.AQUA + " stone sword, shield, 4x ladders.");
	lore3Shield.add(ChatColor.DARK_AQUA + "[Status]:" + ChatColor.AQUA + " Voter class!");
	lore3Shield.add(ChatColor.GOLD + "------------------------------");
	starMetaShield.setLore(lore3Shield);
	starShield.setItemMeta(starMetaShield);
	
	ItemStack signSwordsman = new ItemStack(Material.IRON_SWORD);
	//meta
	ItemMeta signMetaSwordsman = signSwordsman.getItemMeta();
	signMetaSwordsman.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	signMetaSwordsman.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Swordsman");
	//lore
	ArrayList<String> lore1Swordsman = new ArrayList<String>();
	lore1Swordsman.add(ChatColor.GOLD + "------------------------------");
	lore1Swordsman.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + " The standard melee class.");
	lore1Swordsman.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + " Full iron armor.");
	lore1Swordsman.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + " Iron sword");
	lore1Swordsman.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + " Free to play!");
	lore1Swordsman.add(ChatColor.GOLD + "------------------------------");
	signMetaSwordsman.setLore(lore1Swordsman);
	signSwordsman.setItemMeta(signMetaSwordsman);
	
	ItemStack starSkirmisher = new ItemStack(Material.IRON_BOOTS);
	//meta
	ItemMeta starMetaSkirmisher = starSkirmisher.getItemMeta();
	starMetaSkirmisher.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	//name
	starMetaSkirmisher.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.BLUE + " Skirmisher");
	//lore
	ArrayList<String> lore3Skirmisher = new ArrayList<String>();
	lore3Skirmisher.add(ChatColor.GOLD + "------------------------------");
	lore3Skirmisher.add(ChatColor.DARK_AQUA + "[Desc]:" + ChatColor.AQUA + " A fast melee warrior, has speed I.");
	lore3Skirmisher.add(ChatColor.DARK_AQUA + "[Armor]:" + ChatColor.AQUA + " Iron chestplate, Iron boots, leather leggings.");
	lore3Skirmisher.add(ChatColor.DARK_AQUA + "[Weapon]:" + ChatColor.AQUA + " Iron sword");
	lore3Skirmisher.add(ChatColor.DARK_AQUA + "[Status]:" + ChatColor.AQUA + " Voter class!");
	lore3Skirmisher.add(ChatColor.GOLD + "------------------------------");
	starMetaSkirmisher.setLore(lore3Skirmisher);
	starSkirmisher.setItemMeta(starMetaSkirmisher);
	
	ItemStack panel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
	ItemMeta panelMeta = panel.getItemMeta();
	panelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	panelMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "");
	ArrayList<String> panelLore = new ArrayList<String>();
	panelMeta.setLore(panelLore);
	panel.setItemMeta(panelMeta);
	
	ItemStack arrowRight = new ItemStack(Material.ARROW);
	ItemMeta arrowRightMeta = arrowRight.getItemMeta();
	arrowRightMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	arrowRightMeta.setDisplayName(ChatColor.GREEN + "Next Page");
	ArrayList<String> arrowRightLore = new ArrayList<String>();
	arrowRightMeta.setLore(arrowRightLore);
	arrowRight.setItemMeta(arrowRightMeta);
	
		
	GUI.setItem(0, panel); GUI.setItem(1, panel); GUI.setItem(2, panel); GUI.setItem(3, panel); GUI.setItem(4, panel);
	GUI.setItem(5, panel); GUI.setItem(6, panel); GUI.setItem(7, panel); GUI.setItem(8, panel); GUI.setItem(9, panel);
	GUI.setItem(10, signSwordsman); GUI.setItem(11, panel); GUI.setItem(12, panel); GUI.setItem(13, sign); GUI.setItem(14, panel);
	GUI.setItem(15, panel); GUI.setItem(16, signSpearman); GUI.setItem(17, panel); GUI.setItem(18, panel); GUI.setItem(19, panel); 
	GUI.setItem(20, panel); GUI.setItem(21, panel); GUI.setItem(22, panel); GUI.setItem(23, panel); GUI.setItem(24, panel);
	GUI.setItem(25, panel); GUI.setItem(26, panel); GUI.setItem(27, panel); GUI.setItem(28, starSkirmisher); GUI.setItem(29, panel);
	GUI.setItem(30, panel); GUI.setItem(31, FireArcher); GUI.setItem(32, panel); GUI.setItem(33, panel); GUI.setItem(34, starShield);
	GUI.setItem(35, panel); GUI.setItem(36, panel); GUI.setItem(37, panel); GUI.setItem(38, panel); GUI.setItem(39, panel);
	GUI.setItem(40, panel); GUI.setItem(41, panel); GUI.setItem(42, panel); GUI.setItem(43, panel); GUI.setItem(44, panel);
	GUI.setItem(45, panel); GUI.setItem(46, panel); GUI.setItem(47, panel); GUI.setItem(48, panel); GUI.setItem(49, panel);
	GUI.setItem(50, panel); GUI.setItem(51, panel); GUI.setItem(52, arrowRight); GUI.setItem(53, panel);
	 
	return GUI;
	
	}

}
