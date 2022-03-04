package me.huntifi.castlesiege.kits.Berserker;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BerserkerVotedKit {
	
	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack DamSword = null;
	private ItemMeta DamSwordMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;

	public BerserkerVotedKit() {
		
		
		StoneSword = new ItemStack(Material.IRON_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 22, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		
		DamSword = new ItemStack(Material.IRON_SWORD);
		
		DamSwordMeta = DamSword.getItemMeta();
		
		DamSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		DamSwordMeta.setUnbreakable(true);

		DamSwordMeta.setDisplayName(ChatColor.GREEN + "Berserker Sword");

		DamSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 58, true);

		ArrayList<String> Damlore1 = new ArrayList<String>();
		
		Damlore1.add("");
		
		Damlore1.add(ChatColor.AQUA + "- voted: +2 damage");

		DamSwordMeta.setLore(Damlore1);

		DamSword.setItemMeta(DamSwordMeta);
		


		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();
		
		Ladders.setItemMeta(LaddersMeta);


	}
	

	
	public ItemStack getBerserkerVotedSword() {
		return StoneSword;

	} 

	
	public ItemStack getBerserkerVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getBerserkerVotedDamageSword() {
		return DamSword;

	}  

	

}
