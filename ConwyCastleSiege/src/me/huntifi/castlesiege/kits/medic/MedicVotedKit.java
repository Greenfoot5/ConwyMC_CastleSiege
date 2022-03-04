package me.huntifi.castlesiege.kits.medic;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MedicVotedKit {

	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack Boots = null;
	private ItemMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	


	public MedicVotedKit() {
		

		
		//sword
		
		Sword = new ItemStack(Material.WOODEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Dagger");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 18, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

		//

		Boots = new ItemStack(Material.GOLDEN_BOOTS);

		BootsMeta = Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Golden Boots");

		BootsMeta.setUnbreakable(true);

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
		
		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		//ladder

		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);


	}


	public ItemStack getMedicVotedSword() {
		return Sword;

	} 

	public ItemStack getMedicVotedLadders() {
		return Ladders;

	}  

	public ItemStack getMedicVotedBoots() {
		return Boots;

	} 
}
