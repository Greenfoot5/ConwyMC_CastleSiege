package me.huntifi.castlesiege.kits;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Helmsdeep.Secrets.Herugrim;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.Archer.Archer;
import me.huntifi.castlesiege.kits.Berserker.Berserker;
import me.huntifi.castlesiege.kits.Cavalry.Cavalry;
import me.huntifi.castlesiege.kits.Crossbowman.Crossbowman;
import me.huntifi.castlesiege.kits.Engineer.Engineer;
import me.huntifi.castlesiege.kits.Executioner.Executioner;
import me.huntifi.castlesiege.kits.FireArcher.Firearcher;
import me.huntifi.castlesiege.kits.Halberdier.Halberdier;
import me.huntifi.castlesiege.kits.Maceman.Maceman;
import me.huntifi.castlesiege.kits.Ranger.Ranger;
import me.huntifi.castlesiege.kits.Shieldman.Shieldman;
import me.huntifi.castlesiege.kits.Skirmisher.Skirmisher;
import me.huntifi.castlesiege.kits.Spearman.Spearman;
import me.huntifi.castlesiege.kits.Swordsman.Swordsman;
import me.huntifi.castlesiege.kits.Viking.Viking;
import me.huntifi.castlesiege.kits.VotedKits.Ladderman.Ladderman;
import me.huntifi.castlesiege.kits.VotedKits.Scout.Scout;
import me.huntifi.castlesiege.kits.Warhound.Warhound;
import me.huntifi.castlesiege.kits.medic.Medic;
import me.huntifi.castlesiege.maps.MapController;

public class EnderchestRefill {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static void refill(Player p) {

		ClearSlots.clearAllSlots(p);

		p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Swordsman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Swordsman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Archer")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Archer.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Spearman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Spearman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Shieldman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Shieldman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("FireArcher")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Firearcher.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Skirmisher")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Skirmisher.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Berserker")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Berserker.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Executioner")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Executioner.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Maceman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Maceman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Viking")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Viking.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Crossbowman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Crossbowman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Cavalry")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Cavalry.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Halberdier")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Halberdier.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Ranger")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Ranger.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Engineer")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Engineer.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Ladderman")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Ladderman.setItems(p); });
		}

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Scout")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Scout.setItems(p); });
		}
		
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Medic")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Medic.setItems(p); });
		}
		
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {

			Bukkit.getScheduler().runTask(plugin, () -> { Warhound.setItems(p); Warhound.wolfPlayer(p); });
			
		} 
		
		Warhound.removeWolfPlayer(p);

		Bukkit.getScheduler().runTask(plugin, () -> {
			
			if (Herugrim.containsHerugrim.contains(p)) { if(MapController.currentMapIs("HelmsDeep")) { p.getInventory().addItem(Herugrim.getHerugrim()); }  }
			Woolheads.setHead(p);

		});

	}

}
