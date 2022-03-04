package me.huntifi.castlesiege.kits.Warhound;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarhoundKit {

	private ItemStack fangs = null;
	private ItemMeta fangsMeta = null;
	
	public WarhoundKit() {
		
		
		fangs = new ItemStack(Material.GHAST_TEAR);
		
		fangsMeta = fangs.getItemMeta();
		
		fangsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		fangsMeta.setUnbreakable(true);

		fangsMeta.setDisplayName(ChatColor.RED + "Fangs");

		fangsMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		fangsMeta.setLore(lore1);

		fangs.setItemMeta(fangsMeta);
	


	}
	
	public ItemStack getWarhoundFangs() {
		return fangs;

	} 

}
