package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;

public class FlagDistance {
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	public static double distancePlayerToFlag(Player p, String flag, Location playerLoc) {
		
		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {
				
				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 988, 37, 1141);
				double distance = playerLoc.distance(flagloc);
				return distance;

			}

			else if (flag.equalsIgnoreCase("Horn")) {

				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 982, 145, 1040);
				double distance = playerLoc.distance(flagloc);
				return distance;
			}

			else if (flag.equalsIgnoreCase("MainGate")) {

				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1032, 52, 1000);
				double distance = playerLoc.distance(flagloc);
				return distance;
			}

			else if (flag.equalsIgnoreCase("Courtyard")) {

				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1025, 74, 1000);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
			}

			else if (flag.equalsIgnoreCase("GreatHalls")) {

				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 948, 72, 1000);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
				//radius 20
				
			}

			else if (flag.equalsIgnoreCase("Caves")) {

				Location flagloc = new Location(plugin.getServer().getWorld("HelmsDeep"), 870, 52, 940);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
			}


		}
		return 0;

	}

}
