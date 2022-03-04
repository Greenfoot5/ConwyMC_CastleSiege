package me.huntifi.castlesiege.Helmsdeep.rams;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepGreatHallDestroyEvent;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.structures.MakeStructure;

public class GreatHallGateReadyRam implements Runnable {

	boolean hasRamBeenReset = true;

	public static boolean isRamReady = false;

	@Override
	public void run() {

		if(currentMaps.currentMapIs("HelmsDeep")) {

			Location ram = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 978, 74, 1000);

			Location startRam = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 978, 72, 1000);

			if (GreatHallGateRam.rammingPlayers.size() >= 1) {

				if (!isRamReady) {

					try {
						MakeStructure.createSchematicStructure(ram, "Classic_Ram", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						MakeStructure.createSchematicStructure(startRam, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					isRamReady = true;

				}


			} else if (HelmsdeepGreatHallDestroyEvent.isBreached && HelmsdeepGreatHallDestroyEvent.GateHealth <= 0) {

				Location ramFix = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 977, 74, 1000); //did this to remove an extra block.

				try {
					MakeStructure.createSchematicStructure(startRam, "Classic_Ram", "HelmsDeep");
				} catch (WorldEditException e) {
					e.printStackTrace();
				}

				try {
					MakeStructure.createSchematicStructure(ram, "removeRam_Maingate", "HelmsDeep");
				} catch (WorldEditException e) {
					e.printStackTrace();
				}

				try {
					MakeStructure.createSchematicStructure(ramFix, "removeRam_Maingate", "HelmsDeep");
				} catch (WorldEditException e) {
					e.printStackTrace();
				}

			} else {

				if (isRamReady) {

					Location ramFix = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 977, 74, 1000); //did this to remove an extra block.

					try {
						MakeStructure.createSchematicStructure(startRam, "Classic_Ram", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						MakeStructure.createSchematicStructure(ram, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						MakeStructure.createSchematicStructure(ramFix, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					isRamReady = false;

				}

			}
		}
	}
}
