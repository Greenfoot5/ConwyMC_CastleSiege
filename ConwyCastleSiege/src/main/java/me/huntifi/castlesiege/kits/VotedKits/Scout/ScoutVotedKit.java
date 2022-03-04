package me.huntifi.castlesiege.kits.VotedKits.Scout;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ScoutVotedKit {

	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;

	public ScoutVotedKit() {
		
		
		StoneSword = new ItemStack(Material.WOODEN_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Shortsword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 21, true);

		StoneSwordMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather boots");
		
		BootsMeta.setColor(Color.fromRGB(64, 87, 1));

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 8);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

	}
	
	public ItemStack getScoutVotedSword() {
		return StoneSword;

	}  
	
	public ItemStack getScoutVotedLadders() {
		return Ladders;

	}  
	
	public ItemStack getScoutVotedBoots() {
		return Boots;

	}  
}
