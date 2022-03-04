package me.huntifi.castlesiege.kits.Warhound;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarhoundVotedKit {

	private ItemStack fangs = null;
	private ItemMeta fangsMeta = null;
	
	public WarhoundVotedKit() {
		
		
		fangs = new ItemStack(Material.GHAST_TEAR);
		
		fangsMeta = fangs.getItemMeta();
		
		fangsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		fangsMeta.setUnbreakable(true);

		fangsMeta.setDisplayName(ChatColor.RED + "Fangs");

		fangsMeta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		fangsMeta.setLore(lore1);

		fangs.setItemMeta(fangsMeta);
	


	}
	
	public ItemStack getWarhoundVotedFangs() {
		return fangs;

	} 

}
