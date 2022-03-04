package me.huntifi.castlesiege.kits.Halberdier;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HalberdierVotedKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	
	
	public HalberdierVotedKit() {
		
		
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
		

		Boots = new ItemStack(Material.IRON_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");
		
		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		BootsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		//



		////

	}
	public ItemStack getHalberdierVotedAxe() {
		return StoneSword;

	} 
	
	public ItemStack getHalberdierVotedBoots() {
		return Boots;

	}  
}
