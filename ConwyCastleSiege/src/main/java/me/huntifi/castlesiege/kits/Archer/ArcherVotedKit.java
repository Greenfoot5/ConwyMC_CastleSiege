package me.huntifi.castlesiege.kits.Archer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArcherVotedKit {

	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;

	public ArcherVotedKit() {
		
		Sword = new ItemStack(Material.WOODEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		SwordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Dagger");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 18, true);

		ArrayList<String> lore1 = new ArrayList<>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);


		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		BootsMeta.setLore(lore4);
		
		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		BootsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		Boots.setItemMeta(BootsMeta);

		
		//ladder
		
		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);
		

	}
	
	
	public ItemStack getArcherVotedSword() {
		return Sword;

	}  
	
	public ItemStack getArcherVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getArcherVotedBoots() {
		return Boots;

	} 
}
