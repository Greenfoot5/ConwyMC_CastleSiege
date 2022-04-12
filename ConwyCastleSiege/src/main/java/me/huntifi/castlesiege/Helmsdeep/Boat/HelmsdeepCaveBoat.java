package me.huntifi.castlesiege.Helmsdeep.Boat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;

//import me.huntifi.castlesiege.teams.PlayerTeam;

public class HelmsdeepCaveBoat implements Listener, Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static Location boatSpawn = new Location(Bukkit.getServer().getWorld("Helmsdeep"), 922, 30, 956, 90, -3);


	public static ArrayList<Boat> Boats = new ArrayList<Boat>();

	@EventHandler
	public void enterBoat(VehicleEnterEvent e) {

		if (e.getVehicle() instanceof Boat) {

			if (e.getEntered() instanceof Player) {

				Boat b = (Boat) e.getVehicle();

				if (!Boats.contains(b)) {

					Boats.add(b);
				}

				if (Boats.contains(b)) {

					e.setCancelled(false);

				} 

			}

		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void destroyBoat(VehicleDamageEvent e) {

		if (e.getVehicle() instanceof Boat) {

			Boat boat = (Boat) e.getVehicle();

			if (Boats.contains(boat)) {

				if (boat.getPassenger() instanceof Player) {

					if (e.getAttacker() instanceof Player) {

						Player sitter = (Player) boat.getPassenger();

						Player attacker = (Player) e.getAttacker();

						//if (PlayerTeam.getPlayerTeam(attacker) != PlayerTeam.getPlayerTeam(sitter)) {

							e.getVehicle().remove();
							Boats.remove(boat);

						//} else if (PlayerTeam.getPlayerTeam(attacker) == PlayerTeam.getPlayerTeam(sitter)) {

							e.setCancelled(true);

						//}

					}

				}
			}

		}


	}

	//works together with NextMap class

	public static void spawnFirstBoat() {

		if (Boats.size() == 0) {

			Boat boat = (Boat) boatSpawn.getWorld().spawnEntity(boatSpawn, EntityType.BOAT);

			if (!Boats.contains(boat)) {

				Boats.add(boat);

			}

		}

	}

	public void spawnNextBoat() {

		int i = 0;

		if (Boats.size() > 0) {

			if (Boats.get(i) != null) {

				if (Boats.get(i).getLocation().distance(boatSpawn) >= 7) {

					Boat boat = (Boat) boatSpawn.getWorld().spawnEntity(boatSpawn, EntityType.BOAT);

					i++;

					if (!Boats.contains(boat)) {

						Boats.add(boat);

					}	

				}

			} 
		}

	}

	//works together with HelmsdeepReset and main class (at the bottom please change this later)


	public static void clearBoats() {

		List<Boat> boat = Boats;
		for(Boat b : boat){

			if (b != null) { b.remove();  }

		}
		Boats.clear();

	}

	@Override
	public void run() {

		spawnNextBoat();


	}


	@EventHandler
	public void ondestroy(PlayerDeathEvent e) {

		if (e.getEntity() instanceof Boat && e.getEntity().getKiller() instanceof Player) {

			Boat theBoat = (Boat) e.getEntity();

			if (Boats.contains(theBoat)) {

				theBoat.remove();
				Boats.remove(theBoat);

			}

		}
	}


	@EventHandler
	public void onLeave (PlayerQuitEvent e ) {

		Player p = (Player) e.getPlayer();

		if (p.getVehicle() != null) {

			Entity vehicle = p.getVehicle();

			if (vehicle instanceof Boat) {

				if (Boats.contains(vehicle)) {

					vehicle.remove();
					Boats.remove(vehicle);

				}

			}
		}
	}

}
