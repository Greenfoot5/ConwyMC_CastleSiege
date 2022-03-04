package me.huntifi.castlesiege.kits.Shieldman;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ShieldmanKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private ItemMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	private ItemStack Shield = null;
	private ItemMeta ShieldMeta = null;
	
	
	public ShieldmanKit() {
		StoneSword = new ItemStack(Material.STONE_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Longsword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Shield
		
		
		Shield = new ItemStack(Material.SHIELD);
		
		ShieldMeta = Shield.getItemMeta();
		
		ShieldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		ShieldMeta.setUnbreakable(true);

		ShieldMeta.setDisplayName(ChatColor.GREEN + "Shield");

		ShieldMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		ArrayList<String> lore8 = new ArrayList<String>();

		ShieldMeta.setLore(lore8);

		Shield.setItemMeta(ShieldMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();
		
		ChestplateMeta.setColor(Color.fromRGB(204, 204, 255));

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather Tunic");

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

	}
	
	public ItemStack getShieldmanSword() {
		return StoneSword;

	}  
	
	public ItemStack getShieldmanChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getShieldmanLeggings() {
		return Leggings;

	}  
	
	public ItemStack getShieldmanLadders() {
		return Ladders;

	}  
	
	public ItemStack getShieldmanBoots() {
		return Boots;

	}  
	
	public ItemStack getShieldmanShield() {
		return Shield;

	}  

}
