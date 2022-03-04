package me.huntifi.castlesiege.kits.FireArcher;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import net.md_5.bungee.api.ChatColor;

public class FirearcherVotedKit {

	
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;

	private ItemStack Firepit = null;
	private ItemMeta FirepitMeta = null;


	public FirearcherVotedKit() {

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setColor(Color.fromRGB(204, 0, 0));

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

		//

		Firepit = new ItemStack(Material.CAULDRON);

		FirepitMeta = Firepit.getItemMeta();

		FirepitMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		FirepitMeta.setDisplayName(ChatColor.DARK_RED + "Firepit");

		FirepitMeta.addEnchant(Enchantment.DAMAGE_ALL, 22, true);

		FirepitMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		FirepitMeta.setUnbreakable(true);

		ArrayList<String> lore12 = new ArrayList<String>();

		lore12.add("");

		lore12.add(ChatColor.AQUA + "Place the firepit down, then");
		lore12.add(ChatColor.AQUA + "right click it with an arrow.");
		lore12.add("");
		lore12.add(ChatColor.AQUA + "- Voted: + 2 damage.");
		lore12.add(ChatColor.AQUA + "(tip): This firepit is very hard, so you");
		lore12.add(ChatColor.AQUA + "can beat your enemies to death with it.");

		FirepitMeta.setLore(lore12);

		FirepitMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		FirepitMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		FirepitMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		Firepit.setItemMeta(FirepitMeta);



	}




	public ItemStack getFirearcherVotedFirepit() {
		return Firepit;

	} 

	public ItemStack getFirearcherVotedLadders() {
		return Ladders;

	}  

	public ItemStack getFirearcherVotedBoots() {
		return Boots;

	}  
	
}
