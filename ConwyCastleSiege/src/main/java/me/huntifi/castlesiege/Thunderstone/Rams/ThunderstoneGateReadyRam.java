package me.huntifi.castlesiege.Thunderstone.Rams;

import me.huntifi.castlesiege.structures.SchematicSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.Thunderstone.Gate.ThunderstoneGateDestroyEvent;
import me.huntifi.castlesiege.maps.MapController;

public class ThunderstoneGateReadyRam implements Runnable {

	boolean hasRamBeenReset = true;

	public static boolean isRamReady = false;

	@Override
	public void run() {

		if(MapController.currentMapIs("Thunderstone")) {

			Location ram = new Location(Bukkit.getServer().getWorld("Thunderstone"), 149, 67, 67);

			Location startRam = new Location(Bukkit.getServer().getWorld("Thunderstone"), 149, 65, 67);

			if (ThunderstoneRam.rammingPlayers.size() >= 1) {

				if (ThunderstoneGateDestroyEvent.isBreached == false) {

					if (!isRamReady) {

						try {
							SchematicSpawner.spawnSchematic(ram, "ram_east", "Thunderstone");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						try {
							SchematicSpawner.spawnSchematic(startRam, "clear_ram_east", "Thunderstone");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						isRamReady = true;

					}

				}


			} else {

				if (isRamReady) {

					Location ramFix = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 150, 67, 67); //did this to remove an extra block.

					try {
						SchematicSpawner.spawnSchematic(startRam, "ram_east", "Thunderstone");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						SchematicSpawner.spawnSchematic(ram, "clear_ram_east", "Thunderstone");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					try {
						SchematicSpawner.spawnSchematic(ramFix, "clear_ram_east", "Thunderstone");
					} catch (WorldEditException e) {
						e.printStackTrace();
					}

					isRamReady = false;

				}

			}
		}
	}
}
