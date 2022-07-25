package me.huntifi.castlesiege.deprecated.Helmsdeep.rams;

import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.maps.MapController;

public class MainGateReadyRam implements Runnable {

	boolean hasRamBeenReset = true;

	public static boolean isRamReady = false;

	@Override
	public void run() {

		if(MapController.currentMapIs("HelmsDeep")) {

			Location ram = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1045, 54, 1000);

			Location startRam = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1045, 52, 1000);

			if (MainGateRam.rammingPlayers.size() >= 1) {

				if (!isRamReady) {

					try {
						SchematicSpawner.spawnSchematic(ram, "Classic_Ram", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						SchematicSpawner.spawnSchematic(startRam, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					isRamReady = true;

				}


//			} else if (HelmsdeepMainGateDestroyEvent.isBreached && HelmsdeepMainGateDestroyEvent.GateHealth <= 0) {
//
//				Location ramFix = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1044, 54, 1000); //did this to remove an extra block.
//
//				try {
//					SchematicSpawner.spawnSchematic(startRam, "Classic_Ram", "HelmsDeep");
//				} catch (WorldEditException e) {
//					e.printStackTrace();
//				}
//
//				try {
//					SchematicSpawner.spawnSchematic(ram, "removeRam_Maingate", "HelmsDeep");
//				} catch (WorldEditException e) {
//					e.printStackTrace();
//				}
//
//				try {
//					SchematicSpawner.spawnSchematic(ramFix, "removeRam_Maingate", "HelmsDeep");
//				} catch (WorldEditException e) {
//					e.printStackTrace();
//				}

			} else {

				if (isRamReady) {

					Location ramFix = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1044, 54, 1000); //did this to remove an extra block.

					try {
						SchematicSpawner.spawnSchematic(startRam, "Classic_Ram", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						SchematicSpawner.spawnSchematic(ram, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						SchematicSpawner.spawnSchematic(ramFix, "removeRam_Maingate", "HelmsDeep");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					isRamReady = false;

				}

			}
		}
	}

}
