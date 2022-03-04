package me.huntifi.castlesiege.kits.Halberdier;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HalberdierKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack ChestPlate = null;
	private ItemMeta ChestplateMeta = null;
	private ItemStack Shield = null;
	private ItemMeta ShieldMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	
	
	public HalberdierKit() {
		
		
		StoneSword = new ItemStack(Material.IRON_AXE);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Halberd");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 28, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.DIAMOND_CHESTPLATE);

		ChestplateMeta = ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.AQUA + "Reinforced Iron Chestplate");
		
		ChestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ChestplateMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		ChestPlate.setItemMeta(ChestplateMeta);
		
		//
		
		Shield = new ItemStack(Material.SHIELD);

		ShieldMeta = Shield.getItemMeta();
		
		ShieldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ShieldMeta.setDisplayName(ChatColor.GREEN + "Shield");

		ShieldMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
		
		ShieldMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		ShieldMeta.setLore(lore7);

		Shield.setItemMeta(ShieldMeta);

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
		
		BootsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		//

	}
	
	public ItemStack getHalberdierShield() {
		return Shield;

	} 
	
	public ItemStack getHalberdierSword() {
		return StoneSword;

	} 
	
	public ItemStack getHalberdierChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getHalberdierLeggings() {
		return Leggings;

	}  
	
	public ItemStack getHalberdierBoots() {
		return Boots;

	}  
	

}
