package me.huntifi.castlesiege.kits.Executioner;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExecutionerVotedKit {
	
	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;

	
	
	public ExecutionerVotedKit() {
		
		
		StoneSword = new ItemStack(Material.IRON_AXE);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Iron Axe");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 22, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);

		//

		Boots = new ItemStack(Material.IRON_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}
	
	public ItemStack getExecutionerVotedAxe() {
		return StoneSword;

	}  
	
	public ItemStack getExecutionerVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getExecutionerVotedBoots() {
		return Boots;

	}  

}
