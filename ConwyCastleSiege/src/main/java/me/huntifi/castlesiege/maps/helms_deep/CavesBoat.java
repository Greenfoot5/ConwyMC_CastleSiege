package me.huntifi.castlesiege.maps.helms_deep;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles spawning boats in the caves of HelmsDeep
 */
public class CavesBoat implements Listener, Runnable {

	public static final Location SPAWN_LOCATION = new Location(Bukkit.getServer().getWorld("Helmsdeep"), 922, 30, 956, 90, -3);
	public static ArrayList<Integer> boats = new ArrayList<>();

	/**
	 * Adds a boat to the boat list if it isn't there already
	 * @param event Called when an entity enters a vehicle
	 */
	@EventHandler
	public void enterBoat(VehicleEnterEvent event) {
		if (event.getVehicle() instanceof Boat) {
			if (event.getEntered() instanceof Player) {

				Boat boat = (Boat) event.getVehicle();
				if (!boats.contains(boat.getEntityId())) {
					boats.add(boat.getEntityId());
				}
			}
		}
	}

	/**
	 * Removes a boat from the list if it's destroyed
	 * @param event Called when a vehicle is hit
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onBoatDestroyBoat(VehicleDamageEvent event) {

		if (event.getVehicle() instanceof Boat) {


		}
	}

	/**
	 * Spawns a boat.
	 * Does not check if there are any boats already
	 */
	public void spawnBoat() {
		if (boats.size() == 0) {
			Boat boat = (Boat) Objects.requireNonNull(SPAWN_LOCATION.getWorld()).spawnEntity(SPAWN_LOCATION, EntityType.BOAT);
			boats.add(boat.getEntityId());
		}
	}

	/**
	 * Spawns the next boat if there isn't one already
	 */
	public void spawnNextBoat() {

		int i = 0;

		// Make sure we've recorded every boat in the radius
		for (Entity entity : Objects.requireNonNull(SPAWN_LOCATION.getWorld()).getNearbyEntities(SPAWN_LOCATION, 7.0, 7.0, 7.0)) {
			i++;
			// If for some reason, it isn't a registered boat, register it
			if (entity instanceof Boat && !boats.contains(entity.getEntityId())) {
				boats.add(entity.getEntityId());
			}
		}

		// We don't have any boats in the radius!
		if (i <= 0) {
			Boat boat = (Boat) Objects.requireNonNull(SPAWN_LOCATION.getWorld()).spawnEntity(SPAWN_LOCATION, EntityType.BOAT);
			boats.add(boat.getEntityId());
		}
	}

	/**
	 * Removes any boats that don't exist anymore.
	 * Probably not needed, but here in case we don't need something to clear it
	 */
	public void cleanBoats() {
		boats.clear();
		for(Entity entity : Objects.requireNonNull(SPAWN_LOCATION.getWorld()).getEntities()){
			if (entity instanceof Boat) {
				boats.add(entity.getEntityId());
			}
		}

		if (boats.size() <= 0)
			spawnNextBoat();
	}

	/**
	 * Destroys the boat if the player riding it dies
	 * @param event Called when a player dies
	 */
	@EventHandler
	public void onPassengerKill(PlayerDeathEvent event) {
		if (!(event.getEntity().getVehicle() instanceof Boat)) {
			return;
		}

		Boat boat = (Boat) event.getEntity().getVehicle();
		if (boats.contains(boat.getEntityId())) {
			boats.remove(boat.getEntityId());
			boat.remove();
			spawnNextBoat();
		}
	}

	/**
	 * Removes a boat if the player was sitting in it
	 * @param event Called when a player disconnects
	 */
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (!(event.getPlayer().getVehicle() instanceof Boat)) {
			return;
		}

		Boat boat = (Boat) event.getPlayer().getVehicle();
		if (boats.contains(boat.getEntityId())) {
			boats.remove(boat.getEntityId());
			boat.remove();
			spawnNextBoat();
		}
	}

	/**
	 * Our gameplay loop for the boats that makes sure one is spawned,
	 * and we don't have too many
	 */
	@Override
	public void run() {
		// Attempts to spawn a new boat
		if (Objects.equals(MapController.getCurrentMap().name, "Helms Deep")) {
			spawnNextBoat();
		}

		// Let's try to clean the boats list if we've got a lot
		if (boats.size() > 5) {
			cleanBoats();
		}
	}
}
