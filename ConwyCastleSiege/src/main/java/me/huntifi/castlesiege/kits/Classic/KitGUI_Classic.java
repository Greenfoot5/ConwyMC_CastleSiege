package me.huntifi.castlesiege.kits.Classic;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KitGUI_Classic {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	public Inventory GUI = plugin.getServer().createInventory(null, 54, ChatColor.DARK_GREEN + "Classic Kits" );
	
	public Inventory GUI2 = plugin.getServer().createInventory(null, 54, ChatColor.DARK_GREEN + "Classic Kits" );
	
	public Inventory ClassicKitGuiPage1() {
		
		ItemStack item = new ItemStack(Material.IRON_SWORD);
		//meta
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Swordsman");
		//lore
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GOLD + "------------------------------");
		lore1.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "The standard melee kit.");
		lore1.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Iron armor.");
		lore1.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron sword");
		lore1.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Free to play!");
		lore1.add(ChatColor.GOLD + "------------------------------");
		itemMeta.setLore(lore1);
		item.setItemMeta(itemMeta);
		
		ItemStack item2 = new ItemStack(Material.BOW);
		//meta
		ItemMeta itemMeta2 = item2.getItemMeta();
		itemMeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Archer");
		//lore
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GOLD + "------------------------------");
		lore2.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "The standard ranged kit.");
		lore2.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Leather armor.");
		lore2.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Wooden sword, bow and 32 arrows.");
		lore2.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Free to play!");
		lore2.add(ChatColor.GOLD + "------------------------------");
		itemMeta2.setLore(lore2);
		item2.setItemMeta(itemMeta2);
		
		ItemStack item3 = new ItemStack(Material.STICK);
		//meta
		ItemMeta itemMeta3 = item3.getItemMeta();
		itemMeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta3.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Spearman");
		//lore
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GOLD + "------------------------------");
		lore3.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A kit that can throw spears.");
		lore3.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Chainmail armor.");
		lore3.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "5x spears.");
		lore3.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Free to play!");
		lore3.add(ChatColor.GOLD + "------------------------------");
		itemMeta3.setLore(lore3);
		item3.setItemMeta(itemMeta3);
		
		ItemStack item4 = new ItemStack(Material.POTION);
		//meta
		ItemMeta itemMeta4 = item4.getItemMeta();
		itemMeta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta4.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Berserker");
		//lore
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GOLD + "------------------------------");
		lore4.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A armorless warrior with high dps and speed.");
		lore4.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "No armor.");
		lore4.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron sword, berserker potion.");
		lore4.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore4.add(ChatColor.GOLD + "------------------------------");
		itemMeta4.setLore(lore4);
		item4.setItemMeta(itemMeta4);
		
		ItemStack item5 = new ItemStack(Material.LIME_DYE);
		//meta
		ItemMeta itemMeta5 = item5.getItemMeta();
		itemMeta5.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta5.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Ranger");
		//lore
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GOLD + "------------------------------");
		lore5.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A ranged unit with more speed and bows.");
		lore5.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Leather armor.");
		lore5.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Wooden sword, 3 special bows and 48 arrows.");
		lore5.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + "Speed 1.");
		lore5.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore5.add(ChatColor.GOLD + "------------------------------");
		itemMeta5.setLore(lore5);
		item5.setItemMeta(itemMeta5);
		
		ItemStack item6 = new ItemStack(Material.DIAMOND_CHESTPLATE);
		//meta
		ItemMeta itemMeta6 = item6.getItemMeta();
		itemMeta6.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta6.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Halberdier");
		//lore
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(ChatColor.GOLD + "------------------------------");
		lore6.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A extremely tanky kit that deals extra damage to mounted units.");
		lore6.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "diamond chestplate, chainmail leggings and iron boots.");
		lore6.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron axe (sharp 3).");
		lore6.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + "Slowness 4, mining fatigue and hunger.");
		lore6.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Does 50% more damage to mounted units.");
		lore6.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
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
		
		ItemStack arrowRight = new ItemStack(Material.ARROW);
		ItemMeta arrowRightMeta = arrowRight.getItemMeta();
		arrowRightMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		arrowRightMeta.setDisplayName(ChatColor.GREEN + "Next Page");
		ArrayList<String> arrowRightLore = new ArrayList<String>();
		arrowRightMeta.setLore(arrowRightLore);
		arrowRight.setItemMeta(arrowRightMeta);
		
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
		GUI.setItem(50, panel); GUI.setItem(51, panel); GUI.setItem(52, panel); GUI.setItem(53, arrowRight);
		
		
		return GUI;	
	}
	
	
	
	public Inventory ClassicKitGuiPage2() {
		
		ItemStack item = new ItemStack(Material.IRON_AXE);
		//meta
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Executioner");
		//lore
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GOLD + "------------------------------");
		lore1.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A light unit with the capability to execute enemies.");
		lore1.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Leather armor.");
		lore1.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron Axe");
		lore1.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Insta-kills enemies that are below 35% hp.");
		lore1.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore1.add(ChatColor.GOLD + "------------------------------");
		itemMeta.setLore(lore1);
		item.setItemMeta(itemMeta);
		
		ItemStack item2 = new ItemStack(Material.CAKE);
		//meta
		ItemMeta itemMeta2 = item2.getItemMeta();
		itemMeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta2.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Medic");
		//lore
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GOLD + "------------------------------");
		lore2.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A support kit that heals allies and places down cakes.");
		lore2.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Leather armor, iron boots.");
		lore2.add(ChatColor.DARK_GREEN + "[Weapon-Items]:" + ChatColor.GREEN + "Wooden sword, bandages, 16x cakes.");
		lore2.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Heals allies on right click with bandages.");
		lore2.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Cakes placed on the ground heal allies when they right click them.");
		lore2.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore2.add(ChatColor.GOLD + "------------------------------");
		itemMeta2.setLore(lore2);
		item2.setItemMeta(itemMeta2);
		
		ItemStack item3 = new ItemStack(Material.IRON_SHOVEL);
		//meta
		ItemMeta itemMeta3 = item3.getItemMeta();
		itemMeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta3.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Maceman");
		//lore
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add(ChatColor.GOLD + "------------------------------");
		lore3.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A kit with the ability to stun enemies.");
		lore3.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Chainmail armor, leather leggings & iron boots.");
		lore3.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron shovel.");
		lore3.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Stun ability with 10 seconds cooldown.");
		lore3.add(ChatColor.DARK_GREEN + "[On Stun]:" + ChatColor.GREEN + "First hit deals +25% damage, gives slowness and blindness.");
		lore3.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore3.add(ChatColor.GOLD + "------------------------------");
		itemMeta3.setLore(lore3);
		item3.setItemMeta(itemMeta3);
		
		ItemStack item4 = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		//meta
		ItemMeta itemMeta4 = item4.getItemMeta();
		itemMeta4.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta4.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Viking");
		//lore
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add(ChatColor.GOLD + "------------------------------");
		lore4.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "An axeman with the ability to ignore armor.");
		lore4.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "No armor.");
		lore4.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Iron axe.");
		lore4.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Ignore 50% of a target's armor.");
		lore4.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore4.add(ChatColor.GOLD + "------------------------------");
		itemMeta4.setLore(lore4);
		item4.setItemMeta(itemMeta4);
		
		ItemStack item5 = new ItemStack(Material.CROSSBOW);
		//meta
		ItemMeta itemMeta5 = item5.getItemMeta();
		itemMeta5.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta5.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Crossbowman");
		//lore
		ArrayList<String> lore5 = new ArrayList<String>();
		lore5.add(ChatColor.GOLD + "------------------------------");
		lore5.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A ranged unit that functions like a sniper.");
		lore5.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Leather armor, iron chestplate.");
		lore5.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "Crossbow and 32 arrows.");
		lore5.add(ChatColor.DARK_GREEN + "[Effects]:" + ChatColor.GREEN + "Slowness 2");
		lore5.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Shoots in a straight line, 5 second cooldown.");
		lore5.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore5.add(ChatColor.GOLD + "------------------------------");
		itemMeta5.setLore(lore5);
		item5.setItemMeta(itemMeta5);
		
		ItemStack item6 = new ItemStack(Material.IRON_HORSE_ARMOR);
		//meta
		ItemMeta itemMeta6 = item6.getItemMeta();
		itemMeta6.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta6.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Cavalry");
		//lore
		ArrayList<String> lore6 = new ArrayList<String>();
		lore6.add(ChatColor.GOLD + "------------------------------");
		lore6.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A basic mounted unit with a sword.");
		lore6.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Chainmail armor and leather boots.");
		lore6.add(ChatColor.DARK_GREEN + "[Weapon-Item]:" + ChatColor.GREEN + "Iron sword, 1x wheat");
		lore6.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Right clicking the wheat to mount your horse.");
		lore6.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore6.add(ChatColor.GOLD + "------------------------------");
		itemMeta6.setLore(lore6);
		item6.setItemMeta(itemMeta6);
		
		ItemStack item7 = new ItemStack(Material.COBWEB);
		//meta
		ItemMeta itemMeta7 = item7.getItemMeta();
		itemMeta7.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta7.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Engineer");
		//lore
		ArrayList<String> lore7 = new ArrayList<String>();
		lore7.add(ChatColor.GOLD + "------------------------------");
		lore7.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A basic mounted unit with a sword.");
		lore7.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "Chainmail armor and leather boots.");
		lore7.add(ChatColor.DARK_GREEN + "[Weapon-Item]:" + ChatColor.GREEN + "Stone sword, 16x cobwebs, 16x stone, 16x wood, 8x traps.");
		lore7.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "The ability to place traps and cobwebs, repair walls and wooden");
		lore7.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "structures. You can also refill catapults and operate cannons.");
		lore7.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore7.add(ChatColor.GOLD + "------------------------------");
		itemMeta7.setLore(lore7);
		item7.setItemMeta(itemMeta7);
		
		ItemStack item8 = new ItemStack(Material.GHAST_TEAR);
		//meta
		ItemMeta itemMeta8 = item8.getItemMeta();
		itemMeta8.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		//name
		itemMeta8.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CLASS:" + ChatColor.RESET +  ChatColor.GREEN + " Warhound");
		//lore
		ArrayList<String> lore8 = new ArrayList<String>();
		lore8.add(ChatColor.GOLD + "------------------------------");
		lore8.add(ChatColor.DARK_GREEN + "[Desc]:" + ChatColor.GREEN + "A special class where you play as a dog.");
		lore8.add(ChatColor.DARK_GREEN + "[Armor]:" + ChatColor.GREEN + "No armor, instead resistance II");
		lore8.add(ChatColor.DARK_GREEN + "[Weapon]:" + ChatColor.GREEN + "no sword, you have fangs.");
		lore8.add(ChatColor.DARK_GREEN + "[Ability]:" + ChatColor.GREEN + "Slow down enemies and make them bleed.");
		lore8.add(ChatColor.DARK_GREEN + "[Status]:" + ChatColor.GREEN + "Unlocked with coins.");
		lore8.add(ChatColor.GOLD + "------------------------------");
		itemMeta8.setLore(lore8);
		item8.setItemMeta(itemMeta8);
		
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
		
		ItemStack arrowLeft = new ItemStack(Material.ARROW);
		ItemMeta arrowLeftMeta = arrowRight.getItemMeta();
		arrowLeftMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		arrowLeftMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
		ArrayList<String> arrowLeftLore = new ArrayList<String>();
		arrowLeftMeta.setLore(arrowLeftLore);
		arrowLeft.setItemMeta(arrowLeftMeta);
		
		GUI.setItem(0, panel); GUI.setItem(1, panel); GUI.setItem(2, panel); GUI.setItem(3, panel); GUI.setItem(4, panel);
		GUI.setItem(5, panel); GUI.setItem(6, panel); GUI.setItem(7, panel); GUI.setItem(8, panel); GUI.setItem(9, panel);
		GUI.setItem(10, item); GUI.setItem(11, panel); GUI.setItem(12, item2); GUI.setItem(13, panel); GUI.setItem(14, item3);
		GUI.setItem(15, panel); GUI.setItem(16, item4); GUI.setItem(17, panel); GUI.setItem(18, panel); GUI.setItem(19, panel); 
		GUI.setItem(20, panel); GUI.setItem(21, panel); GUI.setItem(22, panel); GUI.setItem(23, panel); GUI.setItem(24, panel);
		GUI.setItem(25, panel); GUI.setItem(26, panel); GUI.setItem(27, panel); GUI.setItem(28, item5); GUI.setItem(29, panel);
		GUI.setItem(30, item6); GUI.setItem(31, panel); GUI.setItem(32, item7); GUI.setItem(33, panel); GUI.setItem(34, item8);
		GUI.setItem(35, panel); GUI.setItem(36, panel); GUI.setItem(37, panel); GUI.setItem(38, panel); GUI.setItem(39, panel);
		GUI.setItem(40, panel); GUI.setItem(41, panel); GUI.setItem(42, panel); GUI.setItem(43, panel); GUI.setItem(44, panel);
		GUI.setItem(45, arrowLeft); GUI.setItem(46, panel); GUI.setItem(47, panel); GUI.setItem(48, panel); GUI.setItem(49, panel);
		GUI.setItem(50, panel); GUI.setItem(51, panel); GUI.setItem(52, panel); GUI.setItem(53, panel);
		
		
		return GUI;	
	}

}
