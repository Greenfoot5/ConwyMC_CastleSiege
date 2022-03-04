package me.huntifi.castlesiege.kits.medic;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MedicKit {
	
	
	private ItemStack bandages = null;
	private ItemMeta bandagesMeta = null;
	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	
	private ItemStack Cake = null;
	private ItemMeta CakeMeta = null;


	public MedicKit() {
		
		bandages = new ItemStack(Material.PAPER);
		
		bandagesMeta = bandages.getItemMeta();
		
		bandagesMeta.setDisplayName(ChatColor.DARK_AQUA + "Bandages");
		
		ArrayList<String> lore1b = new ArrayList<String>();
		
		lore1b.add(ChatColor.AQUA + "Right click team mates to heal.");

		bandagesMeta.setLore(lore1b);

		bandages.setItemMeta(bandagesMeta);
		
		//sword
		
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

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather chestplate");

		ChestplateMeta.setColor(Color.fromRGB(255, 255, 255));

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();

		LeggingsMeta.setColor(Color.fromRGB(255, 255, 255));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.GOLDEN_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Golden Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//


		//ladder

		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);
		
		//Cakes

		Cake = new ItemStack(Material.CAKE, 16);

		CakeMeta = Cake.getItemMeta();

		CakeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		CakeMeta.setDisplayName(ChatColor.DARK_AQUA + "Healing Cake");

		CakeMeta.setUnbreakable(true);

		ArrayList<String> lore12 = new ArrayList<String>();
		
		lore12.add(ChatColor.AQUA + "Place the cake down, then");
		lore12.add(ChatColor.AQUA + "teammates can heal from it.");

		CakeMeta.setLore(lore12);
		
		CakeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		CakeMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		
		CakeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		Cake.setItemMeta(CakeMeta);
		
		

	}

	public ItemStack getMedicBandage() {
		return bandages;

	} 

	public ItemStack getMedicSword() {
		return Sword;

	} 
	
	public ItemStack getMedicCake() {
		return Cake;

	} 

	public ItemStack getMedicChestplate() {
		return ChestPlate;

	}  

	public ItemStack getMedicLeggings() {
		return Leggings;

	}  

	public ItemStack getMedicLadders() {
		return Ladders;

	}  

	public ItemStack getMedicBoots() {
		return Boots;

	} 
}
