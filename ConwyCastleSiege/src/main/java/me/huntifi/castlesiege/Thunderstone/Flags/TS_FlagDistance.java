package me.huntifi.castlesiege.Thunderstone.Flags;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;

public class TS_FlagDistance {
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	public static double distancePlayerToFlag(Player p, String flag, Location playerLoc) {
		
		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {
				
				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 170, 66, 68);
				double distance = playerLoc.distance(flagloc);
				return distance;

			}

			if (flag.equalsIgnoreCase("twinbridge")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 291, 117, 79);
				double distance = playerLoc.distance(flagloc);
				return distance;
			}

			if (flag.equalsIgnoreCase("shiftedtower")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 232, 127, 80);
				double distance = playerLoc.distance(flagloc);
				return distance;
			}

			if (flag.equalsIgnoreCase("lonelytower")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 121, 111, 56);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
			}

			if (flag.equalsIgnoreCase("easttower")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 183, 106, 53);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
				//radius 20
				
			}

			if (flag.equalsIgnoreCase("westtower")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 111, 85);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
			}
			
			if (flag.equalsIgnoreCase("skyviewtower")) {

				Location flagloc = new Location(plugin.getServer().getWorld("Thunderstone"), 169, 155, 79);
				double distance = playerLoc.distance(flagloc);
				return distance;
				
			}


		}
		return 0;

	}

}
