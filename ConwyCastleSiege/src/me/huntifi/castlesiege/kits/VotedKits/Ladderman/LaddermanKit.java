package me.huntifi.castlesiege.kits.VotedKits.Ladderman;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LaddermanKit {

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

	
	
	public LaddermanKit() {
		
		
		StoneSword = new ItemStack(Material.STONE_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Shortsword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather Tunic");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.IRON_LEGGINGS);

		LeggingsMeta = Leggings.getItemMeta();

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Iron Leggings");

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

		Ladders = new ItemStack(Material.LADDER, 25);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}
	
	public ItemStack getLaddermanSword() {
		return StoneSword;

	}  
	
	public ItemStack getLaddermanChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getLaddermanLeggings() {
		return Leggings;

	}  
	
	public ItemStack getLaddermanLadders() {
		return Ladders;

	}  
	
	public ItemStack getLaddermanBoots() {
		return Boots;

	}  
	

}