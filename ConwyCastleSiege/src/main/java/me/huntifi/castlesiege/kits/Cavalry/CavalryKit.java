package me.huntifi.castlesiege.kits.Cavalry;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CavalryKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	private ItemStack horse = null;
	private ItemMeta horseMeta = null;
	private ItemStack horsearmor = null;
	private ItemMeta horsearmormeta = null;

	
	
	public CavalryKit() {
		
		
		StoneSword = new ItemStack(Material.IRON_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Sabre");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 16, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Chainmail chestplate");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);

		LeggingsMeta = Leggings.getItemMeta();

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Chainmail Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.IRON_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();
		
		Ladders.setItemMeta(LaddersMeta);

	
		
		////
		
		horse = new ItemStack(Material.WHEAT, 1);
		
		

		horseMeta = horse.getItemMeta();
		
		horseMeta.setDisplayName(ChatColor.GREEN + "Spawn Horse");

		ArrayList<String> lore6 = new ArrayList<String>();

		horseMeta.setLore(lore6);

		horse.setItemMeta(horseMeta);
		
		/////
		
		horsearmor = new ItemStack(Material.IRON_HORSE_ARMOR);

		horsearmormeta = horsearmor.getItemMeta();

		ArrayList<String> lore7 = new ArrayList<String>();

		horsearmormeta.setLore(lore7);

		horsearmor.setItemMeta(horsearmormeta);
		////

	}
	
	public ItemStack getCavalryHorse() {
		return horse;

	}  
	
	public ItemStack getCavalryArmor() {
		return horsearmor;

	} 
	
	public ItemStack getCavalrySword() {
		return StoneSword;

	} 
	
	public ItemStack getCavalryChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getCavalryLeggings() {
		return Leggings;

	}  
	
	public ItemStack getCavalryLadders() {
		return Ladders;

	}  
	
	public ItemStack getCavalryBoots() {
		return Boots;

	}  
	

}
