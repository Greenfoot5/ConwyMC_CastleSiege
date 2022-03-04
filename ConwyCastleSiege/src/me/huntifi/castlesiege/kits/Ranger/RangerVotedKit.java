package me.huntifi.castlesiege.kits.Ranger;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class RangerVotedKit {
	
	private ItemStack Sword = null;
	private ItemMeta SwordMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;


	public RangerVotedKit() {
		
		//sword - dagger
		
		Sword = new ItemStack(Material.WOODEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.GREEN + "Dagger");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();
		
		lore1.add("");
		
		lore1.add(ChatColor.AQUA + "- voted: +2 damage");

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setColor(Color.fromRGB(28, 165, 33));

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//


		//ladder

		Ladders = new ItemStack(Material.LADDER, 6);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}
	
	public ItemStack getRangerVotedSword() {
		return Sword;

	} 

	public ItemStack getRangerVotedLadders() {
		return Ladders;

	}  

	public ItemStack getRangerVotedBoots() {
		return Boots;

	} 

}
