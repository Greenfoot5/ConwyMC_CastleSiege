package me.huntifi.castlesiege.kits.Ranger;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class RangerKit {

	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Arrow = null;
	private ItemMeta ArrowMeta = null;
	private ItemStack Bow = null;
	private ItemMeta BowMeta = null;
	private ItemStack VolleyBow = null;
	private ItemMeta VolleyBowMeta = null;
	private ItemStack BurstBow = null;
	private ItemMeta BurstBowMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public RangerKit() {
		
		//sword - dagger
		
		Sword = new ItemStack(Material.WOODEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Dagger");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 18, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);


		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather chestplate");

		ChestplateMeta.setColor(Color.fromRGB(28, 165, 33));

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();

		LeggingsMeta.setColor(Color.fromRGB(32, 183, 37));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setColor(Color.fromRGB(28, 165, 33));

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");

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

		//arrows

		Arrow = new ItemStack(Material.ARROW, 48);

		ArrowMeta = Arrow.getItemMeta();

		ArrayList<String> lore6 = new ArrayList<String>();

		ArrowMeta.setLore(lore6);

		Arrow.setItemMeta(ArrowMeta);

		//Bow

		Bow = new ItemStack(Material.BOW);

		BowMeta = Bow.getItemMeta();

		BowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		BowMeta.setDisplayName(ChatColor.GREEN + "Bow");

		BowMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		BowMeta.setLore(lore7);

		Bow.setItemMeta(BowMeta);
		
		//Bow

		VolleyBow = new ItemStack(Material.BOW);

		VolleyBowMeta = VolleyBow.getItemMeta();

		VolleyBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		VolleyBowMeta.setDisplayName(ChatColor.GREEN + "Volley Bow");

		VolleyBowMeta.setUnbreakable(true);

		ArrayList<String> lore8 = new ArrayList<String>();

		VolleyBowMeta.setLore(lore8);

		VolleyBow.setItemMeta(VolleyBowMeta);
		
		//Bow

		BurstBow = new ItemStack(Material.BOW);

		BurstBowMeta = BurstBow.getItemMeta();

		BurstBowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		BurstBowMeta.setDisplayName(ChatColor.GREEN + "Burst Bow");

		BurstBowMeta.setUnbreakable(true);

		ArrayList<String> lore9 = new ArrayList<String>();

		BurstBowMeta.setLore(lore9);

		BurstBow.setItemMeta(BurstBowMeta);
		
		

	}
	
	public ItemStack getRangerSword() {
		return Sword;

	} 

	public ItemStack getRangerBow() {
		return Bow;

	}
	
	public ItemStack getRangerVolleyBow() {
		return VolleyBow;

	}  
	
	public ItemStack getRangerBurstBow() {
		return BurstBow;

	}  

	public ItemStack getRangerArrow() {
		return Arrow;

	}  

	public ItemStack getRangerChestplate() {
		return ChestPlate;

	}  

	public ItemStack getRangerLeggings() {
		return Leggings;

	}  

	public ItemStack getRangerLadders() {
		return Ladders;

	}  

	public ItemStack getRangerBoots() {
		return Boots;

	} 
}
