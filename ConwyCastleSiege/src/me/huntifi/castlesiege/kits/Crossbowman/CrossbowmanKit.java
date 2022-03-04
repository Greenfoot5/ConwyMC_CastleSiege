package me.huntifi.castlesiege.kits.Crossbowman;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CrossbowmanKit {

	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Arrow = null;
	private ItemMeta ArrowMeta = null;
	private ItemStack Bow = null;
	private ItemMeta BowMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public CrossbowmanKit() {

		//Chestplate

		ChestPlate = new ItemStack(Material.IRON_CHESTPLATE);
		
		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather Tunic");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//Leggings
		
		
		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();
		
		LeggingsMeta.setColor(Color.fromRGB(255, 255, 51));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//
		
		

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();
		
		BootsMeta.setColor(Color.fromRGB(255, 255, 51));

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		
		
		//ladder
		
		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);
		
		//arrows
		
		Arrow = new ItemStack(Material.ARROW, 32);

		ArrowMeta = Arrow.getItemMeta();

		ArrayList<String> lore6 = new ArrayList<String>();

		ArrowMeta.setLore(lore6);

		Arrow.setItemMeta(ArrowMeta);
		
		//Bow
		
		Bow = new ItemStack(Material.CROSSBOW);

		BowMeta = Bow.getItemMeta();
		
		BowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		BowMeta.setDisplayName(ChatColor.GREEN + "Crossbow");
		
		BowMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		BowMeta.setLore(lore7);

		Bow.setItemMeta(BowMeta);

	}
	
	
	public ItemStack getXbowmanBow() {
		return Bow;

	}  
	
	public ItemStack getXbowmanArrow() {
		return Arrow;

	}  
	
	public ItemStack getXbowmanChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getXbowmanLeggings() {
		return Leggings;

	}  
	
	public ItemStack getXbowmanLadders() {
		return Ladders;

	}  
	
	public ItemStack getXbowmanBoots() {
		return Boots;

	}  
}
