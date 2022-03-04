package me.huntifi.castlesiege.kits.Viking;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VikingVotedKit {

	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public VikingVotedKit() {
		
		Sword = new ItemStack(Material.IRON_AXE);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Giant Battle Axe");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 22, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

		//
		
		

		Boots = new ItemStack(Material.CHAINMAIL_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Chainmail boots");

		BootsMeta.setUnbreakable(true);
		
		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		
		
		//ladder
		
		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();
		
		Ladders.setItemMeta(LaddersMeta);
		
	}
	
	
	public ItemStack getVikingVotedAxe() {
		return Sword;

	}  
	
	public ItemStack getVikingVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getVikingVotedBoots() {
		return Boots;

	}  
}
