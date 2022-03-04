package me.huntifi.castlesiege.kits.Swordsman;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SwordsmanKit {

	private ItemStack IronSword = null;
	private ItemMeta IronSwordMeta = null;
	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public SwordsmanKit() {
		
		IronSword = new ItemStack(Material.IRON_SWORD);
		IronSwordMeta = IronSword.getItemMeta();
		IronSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		IronSwordMeta.setUnbreakable(true);

		IronSwordMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");

		IronSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		IronSwordMeta.setLore(lore1);

		IronSword.setItemMeta(IronSwordMeta);

		ChestPlate = new ItemStack(Material.IRON_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Iron Chestplate");

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		lore2.add(null);

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.IRON_LEGGINGS);

		LeggingsMeta = Leggings.getItemMeta();

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Iron Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		lore3.add(null);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.IRON_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();
		
		lore4.add(null);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}

	public ItemStack getSwordsmanSword() {
		return IronSword;

	}  
	
	public ItemStack getSwordsmanChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getSwordsmanLeggings() {
		return Leggings;

	}  
	
	public ItemStack getSwordsmanLadders() {
		return Ladders;

	}  
	
	public ItemStack getSwordsmanBoots() {
		return Boots;

	}  
}
