package me.huntifi.castlesiege.kits;

import me.huntifi.castlesiege.Helmsdeep.Secrets.Herugrim;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.Warhound.Warhound;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EnderchestRefill {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static void refill(Player p) {

		ClearSlots.clearAllSlots(p);

		p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);

		if (Kit.equippedKits.get(p.getUniqueId()) != null) {
			Bukkit.getScheduler().runTask(plugin, () -> { Kit.equippedKits.get(p.getUniqueId()).refillItems(p.getUniqueId()); });
		}
		
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Warhound.setItems(p); Warhound.wolfPlayer(p); });
			
		} 
		
		Warhound.removeWolfPlayer(p);

		Bukkit.getScheduler().runTask(plugin, () -> {
			
			if (Herugrim.containsHerugrim.contains(p)) { if(MapController.currentMapIs("HelmsDeep")) { p.getInventory().addItem(Herugrim.getHerugrim()); }  }
			WoolHat.setHead(p);

		});

	}

}
