package me.huntifi.castlesiege.kits.FireArcher;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class FirearcherKit {

	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Arrow = null;
	private ItemMeta ArrowMeta = null;
	private ItemStack specialArrow = null;
	private ItemMeta specialArrowMeta = null;
	private ItemStack Bow = null;
	private ItemMeta BowMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	
	private ItemStack Firepit = null;
	private ItemMeta FirepitMeta = null;


	public FirearcherKit() {



		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather chestplate");

		ChestplateMeta.setColor(Color.fromRGB(204, 0, 0));

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();

		LeggingsMeta.setColor(Color.fromRGB(255, 128, 0));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setColor(Color.fromRGB(204, 0, 0));

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//


		//ladder

		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);

		//arrows

		Arrow = new ItemStack(Material.ARROW, 48);

		ArrowMeta = Arrow.getItemMeta();

		ArrayList<String> lore6 = new ArrayList<String>();

		ArrowMeta.setLore(lore6);

		Arrow.setItemMeta(ArrowMeta);

		//special arrows

		specialArrow = new ItemStack(Material.TIPPED_ARROW);
		specialArrowMeta = specialArrow.getItemMeta();
		specialArrowMeta.setDisplayName(ChatColor.GOLD + "Fire arrow");
		PotionMeta meta = (PotionMeta) specialArrow.getItemMeta();
		meta.setColor(Color.ORANGE);
		specialArrowMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		specialArrow.setItemMeta(meta);


		//Bow

		Bow = new ItemStack(Material.BOW);

		BowMeta = Bow.getItemMeta();

		BowMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		BowMeta.setDisplayName(ChatColor.GREEN + "Bow");

		BowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 16, true);

		BowMeta.setUnbreakable(true);

		ArrayList<String> lore7 = new ArrayList<String>();

		BowMeta.setLore(lore7);

		Bow.setItemMeta(BowMeta);
		
		//Bow

		Firepit = new ItemStack(Material.CAULDRON);

		FirepitMeta = Firepit.getItemMeta();

		FirepitMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		FirepitMeta.setDisplayName(ChatColor.DARK_RED + "Firepit");
		
		FirepitMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		FirepitMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);

		FirepitMeta.setUnbreakable(true);

		ArrayList<String> lore12 = new ArrayList<String>();
		
		lore12.add("");
		
		lore12.add(ChatColor.AQUA + "Place the firepit down, then");
		lore12.add(ChatColor.AQUA + "right click it with an arrow.");
		lore12.add("");
		lore12.add(ChatColor.AQUA + "(tip): This firepit is very hard, so you");
		lore12.add(ChatColor.AQUA + "can beat your enemies to death with it.");

		FirepitMeta.setLore(lore12);
		
		FirepitMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		FirepitMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		
		FirepitMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		Firepit.setItemMeta(FirepitMeta);
		
		

	}



	
	public ItemStack getFirearcherFirepit() {
		return Firepit;

	} 

	public ItemStack getFirearcherBow() {
		return Bow;

	}  
	
	public ItemStack getFirearcherSpecialArrows() {
		return specialArrow;

	}  

	public ItemStack getFirearcherArrow() {
		return Arrow;

	}  

	public ItemStack getFirearcherChestplate() {
		return ChestPlate;

	}  

	public ItemStack getFirearcherLeggings() {
		return Leggings;

	}  

	public ItemStack getFirearcherLadders() {
		return Ladders;

	}  

	public ItemStack getFirearcherBoots() {
		return Boots;

	} 
}
