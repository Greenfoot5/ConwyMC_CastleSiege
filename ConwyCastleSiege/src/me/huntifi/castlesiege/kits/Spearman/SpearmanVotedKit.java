package me.huntifi.castlesiege.kits.Spearman;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SpearmanVotedKit {

	private ItemStack IronSword = null;
	private ItemMeta IronSwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public SpearmanVotedKit() {
		
		IronSword = new ItemStack(Material.STICK, 5);
		IronSwordMeta = IronSword.getItemMeta();
		IronSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		IronSwordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		IronSwordMeta.setUnbreakable(true);

		IronSwordMeta.setDisplayName(ChatColor.GREEN + "Spear");

		IronSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 50, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");
		
		lore1.add(ChatColor.AQUA + "Right click to throw a spear.");

		IronSwordMeta.setLore(lore1);

		IronSword.setItemMeta(IronSwordMeta);

		//

		Boots = new ItemStack(Material.CHAINMAIL_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Chainmail Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		lore4.add(null);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}

	public ItemStack getSpearmanVotedSpears() {
		return IronSword;

	}  
	
	public ItemStack getSpearmanVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getSpearmanVotedBoots() {
		return Boots;

	}  
}
