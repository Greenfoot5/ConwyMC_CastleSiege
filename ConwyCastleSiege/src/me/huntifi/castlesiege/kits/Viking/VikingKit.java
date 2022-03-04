package me.huntifi.castlesiege.kits.Viking;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VikingKit {

	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Shield = null;
	private ItemMeta ShieldMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public VikingKit() {
		Sword = new ItemStack(Material.IRON_AXE);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Giant Battle Axe");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

		//Chestplate

		ChestPlate = new ItemStack(Material.IRON_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Iron Chestplate");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//Leggings
		
		
		Leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);

		LeggingsMeta = Leggings.getItemMeta();

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Chainmail Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//
		
		

		Boots = new ItemStack(Material.CHAINMAIL_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Chainmail boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		
		
		//ladder
		
		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();
		
		Ladders.setItemMeta(LaddersMeta);
		
		//Bow
		
		Shield = new ItemStack(Material.SHIELD);

		ShieldMeta = Shield.getItemMeta();
		
		ShieldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ShieldMeta.setDisplayName(ChatColor.GREEN + "Shield");

		ShieldMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
		
		ShieldMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		ShieldMeta.setLore(lore7);

		Shield.setItemMeta(ShieldMeta);

	}
	
	
	public ItemStack getVikingAxe() {
		return Sword;

	}  
	
	public ItemStack getVikingShield() {
		
		
		return Shield;
	}
	
	public ItemStack getVikingChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getVikingLeggings() {
		return Leggings;

	}  
	
	public ItemStack getVikingLadders() {
		return Ladders;

	}  
	
	public ItemStack getVikingBoots() {
		return Boots;

	}  
}
