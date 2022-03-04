package me.huntifi.castlesiege.Helmsdeep.Secrets;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.huntifi.castlesiege.maps.currentMaps;

public class Herugrim implements Listener, Runnable {
	
	public static ArrayList<Player> containsHerugrim = new ArrayList<Player>();

	static ItemStack Sword;
	static ItemMeta SwordMeta;

	public Herugrim() {

		Sword = new ItemStack(Material.GOLDEN_SWORD);

		SwordMeta = Sword.getItemMeta();

		SwordMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		SwordMeta.setUnbreakable(true);

		SwordMeta.setDisplayName(ChatColor.DARK_PURPLE + "Herugrim");

		SwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 30, true);

		SwordMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);

		ArrayList<String> lore1 = new ArrayList<String>();

		SwordMeta.setLore(lore1);

		Sword.setItemMeta(SwordMeta);

	}
	
	//Herugrim

	public static ItemStack getHerugrim() {
		return Sword;

	}  
	
	//Literally spawns herugrim at its spawn location.


	public static void spawnHerugrim() {

		Location block = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 983.903, 58, 986.954);

		Bukkit.getServer().getWorld("HelmsDeep").dropItem(block.add(+0.5, +1, +0.5), getHerugrim()).setVelocity(new Vector(0, 0, 0));

	}

	//When a player dies and has herugrim, they drop Herugrim on the ground.

	@EventHandler
	public static void dropHerugrim(PlayerDeathEvent e) {

		if(currentMaps.currentMapIs("HelmsDeep")) {

			Player p = (Player) e.getEntity();

			if (p.getInventory().contains(getHerugrim())) {
				
				p.getInventory().remove(getHerugrim());

				p.getWorld().dropItem(p.getLocation().add(+0.5, +1, +0.5), getHerugrim()).setVelocity(new Vector(0, 0, 0));

				if (containsHerugrim.contains(p)) { containsHerugrim.remove(p);}
			}

		}

	}
	
	//Spawns herugrim back at its original spawn point, when a player who has herugrim logs out.
	
	@EventHandler
	public static void dropHerugrim2(PlayerQuitEvent e) {

		if(currentMaps.currentMapIs("HelmsDeep")) {

			Player p = e.getPlayer();

			if (p.getInventory().contains(getHerugrim())) {
				
				if (containsHerugrim.contains(p)) {
				
				Location block = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 983.903, 58, 986.954);
				
				Bukkit.getServer().getWorld("HelmsDeep").dropItem(block.add(+0.5, +1, +0.5), getHerugrim()).setVelocity(new Vector(0, 0, 0));

		        containsHerugrim.remove(p);
		        
		        }
			}

		}

	}
	
	//Checks whether a player has herugrim in their inventory

	@Override
	public void run() {
		
		if(currentMaps.currentMapIs("HelmsDeep")) {
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				if (p.getInventory().contains(getHerugrim())) {
					
					if (!containsHerugrim.contains(p)) { containsHerugrim.add(p);}
					
				}

			}
		}
	}
}
