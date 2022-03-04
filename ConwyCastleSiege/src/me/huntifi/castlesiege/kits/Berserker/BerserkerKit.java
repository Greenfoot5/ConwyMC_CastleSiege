package me.huntifi.castlesiege.kits.Berserker;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BerserkerKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack DamSword = null;
	private ItemMeta DamSwordMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	private ItemStack Potion = null;
	private ItemMeta PotionMeta = null;

	
	
	public BerserkerKit() {
		
		
		StoneSword = new ItemStack(Material.IRON_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		
		DamSword = new ItemStack(Material.IRON_SWORD);
		
		DamSwordMeta = DamSword.getItemMeta();
		
		DamSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		DamSwordMeta.setUnbreakable(true);

		DamSwordMeta.setDisplayName(ChatColor.GREEN + "Berserker Sword");

		DamSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 56, true);

		DamSwordMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		ArrayList<String> Damlore1 = new ArrayList<String>();

		DamSwordMeta.setLore(Damlore1);

		DamSword.setItemMeta(DamSwordMeta);
		


		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();
		
		Ladders.setItemMeta(LaddersMeta);

	
		

		Potion = new ItemStack(Material.POTION, 1);
		
		PotionMeta = Potion.getItemMeta();
		
		PotionMeta.setDisplayName(ChatColor.GOLD + "Berserker Potion");

		PotionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		PotionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		

		ArrayList<String> lore10 = new ArrayList<String>();

		PotionMeta.setLore(lore10);
		
		Potion.setItemMeta(PotionMeta);


	}
	

	
	public ItemStack getBerserkerSword() {
		return StoneSword;

	} 

	
	public ItemStack getBerserkerLadders() {
		return Ladders;

	}  
	
	public ItemStack getBerserkerPotion() {
		return Potion;

	}  
	
	public ItemStack getBerserkerSword2() {
		return DamSword;

	}  

	

}
