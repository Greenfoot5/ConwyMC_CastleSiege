package me.huntifi.castlesiege.kits.Skirmisher;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SkirmisherVotedKit {

	private ItemStack IronSword = null;
	private ItemMeta IronSwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public SkirmisherVotedKit() {
		
		IronSword = new ItemStack(Material.IRON_SWORD);
		
		IronSwordMeta = IronSword.getItemMeta();
		
		IronSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		IronSwordMeta.setUnbreakable(true);

		IronSwordMeta.setDisplayName(ChatColor.GREEN + "Iron Sword");

		IronSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 22, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		IronSwordMeta.setLore(lore1);

		IronSword.setItemMeta(IronSwordMeta);

	

		Boots = new ItemStack(Material.IRON_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Iron Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		lore4.add(null);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 10);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}

	public ItemStack getSkirmisherVotedSword() {
		return IronSword;

	}  
	
	public ItemStack getSkirmisherVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getSkirmisherVotedBoots() {
		return Boots;

	}  
}
