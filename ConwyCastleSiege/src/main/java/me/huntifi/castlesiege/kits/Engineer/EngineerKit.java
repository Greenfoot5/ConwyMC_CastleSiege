package me.huntifi.castlesiege.kits.Engineer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class EngineerKit {
	
	private ItemStack StoneSword = null;
	private ItemMeta StoneSwordMeta = null;
	private ItemStack ChestPlate = null;
	private LeatherArmorMeta ChestplateMeta = null;
	private ItemStack Leggings = null;
	private LeatherArmorMeta LeggingsMeta = null;
	private ItemStack Boots = null;
	private LeatherArmorMeta BootsMeta = null;
	private ItemStack Ladders = null;
	private ItemMeta LaddersMeta = null;
	private ItemStack cobblestone = null;
	private ItemMeta cobblestoneMeta = null;
	private ItemStack planks = null;
	private ItemMeta planksMeta = null;
	private ItemStack cobwebs = null;
	private ItemMeta cobwebsMeta = null;
	private ItemStack traps = null;
	private ItemMeta trapsMeta = null;

	
	
	public EngineerKit() {
		
		
		StoneSword = new ItemStack(Material.STONE_SWORD);
		
		StoneSwordMeta = StoneSword.getItemMeta();
		
		StoneSwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		StoneSwordMeta.setUnbreakable(true);

		StoneSwordMeta.setDisplayName(ChatColor.GREEN + "Shortsword");

		StoneSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		StoneSwordMeta.setLore(lore1);

		StoneSword.setItemMeta(StoneSwordMeta);
		
		//Chestplate

		ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ChestplateMeta = (LeatherArmorMeta)ChestPlate.getItemMeta();

		ChestplateMeta.setDisplayName(ChatColor.GREEN + "Leather chestplate");
		
		ChestplateMeta.setColor(Color.fromRGB(57, 75, 57));

		ChestplateMeta.setUnbreakable(true);

		ArrayList<String> lore2 = new ArrayList<String>();

		ChestplateMeta.setLore(lore2);

		ChestPlate.setItemMeta(ChestplateMeta);

		ChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Leggings = new ItemStack(Material.LEATHER_LEGGINGS);

		LeggingsMeta = (LeatherArmorMeta)Leggings.getItemMeta();
		
		LeggingsMeta.setColor(Color.fromRGB(57, 75, 57));

		LeggingsMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");

		ArrayList<String> lore3 = new ArrayList<String>();

		LeggingsMeta.setUnbreakable(true);

		LeggingsMeta.setLore(lore3);

		Leggings.setItemMeta(LeggingsMeta);

		LeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Boots = new ItemStack(Material.LEATHER_BOOTS);

		BootsMeta = (LeatherArmorMeta)Boots.getItemMeta();

		BootsMeta.setDisplayName(ChatColor.GREEN + "Leather boots");
		
		BootsMeta.setColor(Color.fromRGB(57, 75, 57));

		BootsMeta.setUnbreakable(true);

		ArrayList<String> lore4 = new ArrayList<String>();

		BootsMeta.setLore(lore4);

		Boots.setItemMeta(BootsMeta);

		BootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		//

		Ladders = new ItemStack(Material.LADDER, 4);

		LaddersMeta = Ladders.getItemMeta();

		Ladders.setItemMeta(LaddersMeta);
		
		//traps
		
		traps = new ItemStack(Material.STONE_PRESSURE_PLATE, 8);

		trapsMeta = traps.getItemMeta();

		traps.setItemMeta(trapsMeta);
		
		//stone
		
		cobblestone = new ItemStack(Material.COBBLESTONE, 16);

		cobblestoneMeta = cobblestone.getItemMeta();

		cobblestone.setItemMeta(cobblestoneMeta);
		
		//planks
		
		planks = new ItemStack(Material.OAK_PLANKS, 16);

		planksMeta = planks.getItemMeta();

		planks.setItemMeta(planksMeta);
		
		//cobwebs
		
		cobwebs = new ItemStack(Material.COBWEB, 16);

		cobwebsMeta = cobwebs.getItemMeta();

		cobwebs.setItemMeta(cobwebsMeta);
		
		

	}
	
	public ItemStack getEngineerStone() {
		return cobblestone;

	}  
	
	public ItemStack getEngineerPlanks() {
		return planks;

	}  
	
	public ItemStack getEngineerCobwebs() {
		return cobwebs;

	}  
	
	public ItemStack getEngineerTraps() {
		return traps;

	}  
	
	public ItemStack getEngineerSword() {
		return StoneSword;

	}  
	
	public ItemStack getEngineerChestplate() {
		return ChestPlate;

	}  
	
	public ItemStack getEngineerLeggings() {
		return Leggings;

	}  
	
	public ItemStack getEngineerLadders() {
		return Ladders;

	}  
	
	public ItemStack getEngineerBoots() {
		return Boots;

	}  
	

}
