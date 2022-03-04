package me.huntifi.castlesiege.kits.VotedKits.Scout;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ScoutKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;

	public ScoutKit() {
		
		
		StoneSword = new ItemStack(Material.WOODEN_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Shortsword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 19, true);

		StoneSwordMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather chestplate");
		
		ChestplateMeta.setColor(Color.fromRGB(64, 87, 1));

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();
		
		LeggingsMeta.setColor(Color.fromRGB(42, 57, 3));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather boots");
		
		BootsMeta.setColor(Color.fromRGB(64, 87, 1));

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}
	
	public ItemStack getScoutSword() {
		return StoneSword;

	}  
	
	public ItemStack getScoutChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getScoutLeggings() {
		return Leggings;

	}  
	
	public ItemStack getScoutLadders() {
		return Ladders;

	}  
	
	public ItemStack getScoutBoots() {
		return Boots;

	}  
	

}
