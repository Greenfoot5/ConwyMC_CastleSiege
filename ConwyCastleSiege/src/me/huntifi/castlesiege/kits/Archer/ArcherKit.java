package me.huntifi.castlesiege.kits.Archer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArcherKit {

	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Arrow = null;
	private ItemMeta ArrowMeta = null;
	private ItemStack Bow = null;
	private ItemMeta BowMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public ArcherKit() {
		
		Sword = new ItemStack(Material.WOODEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Dagger");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 16, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather Tunic");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//Leggings
		
		
		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = Leggings.getItemMeta();

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//
		
		

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = Boots.getItemMeta();

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
		
		Arrow = new ItemStack(Material.ARROW, 35);

		ArrowMeta = Arrow.getItemMeta();

		ArrayList<String> lore6 = new ArrayList<String>();

		ArrowMeta.setLore(lore6);

		Arrow.setItemMeta(ArrowMeta);
		
		//Bow
		
		Bow = new ItemStack(Material.BOW);

		BowMeta = Bow.getItemMeta();
		
		BowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		BowMeta.setDisplayName(ChatColor.GREEN + "Bow");

		BowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 26, true);
		
		BowMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		BowMeta.setLore(lore7);

		Bow.setItemMeta(BowMeta);

	}
	
	
	public ItemStack getArcherSword() {
		return Sword;

	}  
	
	public ItemStack getArcherBow() {
		return Bow;

	}  
	
	public ItemStack getArcherArrow() {
		return Arrow;

	}  
	
	public ItemStack getArcherChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getArcherLeggings() {
		return Leggings;

	}  
	
	public ItemStack getArcherLadders() {
		return Ladders;

	}  
	
	public ItemStack getArcherBoots() {
		return Boots;

	}  
}
