package me.huntifi.castlesiege.combat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * A class to manage which players have and haven't been in combat
 */
public class InCombat implements Listener {

	private static ArrayList<UUID> beenInCombat = new ArrayList<>();
	private static HashMap<UUID, Integer> inCombat = new HashMap<>();

	/**
	 * When a player attacks another player, they have interacted
	 */
	@EventHandler
	public static void playerAttacksAnother(EntityDamageByEntityEvent ed) {
		// Both are players
		if (!(ed.getEntity() instanceof Player && ed.getDamager() instanceof Player)) { return; }

		UUID whoWasHit = ed.getEntity().getUniqueId();
		UUID whoHit = ed.getDamager().getUniqueId();

		// Check they aren't on the same team
		if (MapController.getCurrentMap().getTeam(whoHit) != MapController.getCurrentMap().getTeam(whoWasHit)) {
			playerInteracted(whoHit);
		}
	}

	/**
	 * When a player takes any damage, they are placed in combat
	 */
	@EventHandler
	public static void playerTakesDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) { return; }
		UUID uuid = event.getEntity().getUniqueId();

		addPlayerToCombat(uuid);
	}

	/**
	 * Adds a player to the inCombat
	 * @param uuid the UUID of the player to add
	 */
	public static void addPlayerToCombat(UUID uuid) {
		inCombat.merge(uuid, 1, Integer::sum);
		if (!beenInCombat.contains(uuid)) {beenInCombat.add(uuid);}

		// Players are in combat for 10 seconds only
		new BukkitRunnable() {
			@Override
			public void run() {
				inCombat.merge(uuid, -1, Integer::sum);
			}
		}.runTaskLater(Main.plugin, 200);
	}

	/**
	 * Removes a player's uuid from all combat lists
	 */
	public static void playerDied(UUID uuid) {
		inCombat.put(uuid, 0);
		beenInCombat.remove(uuid);
	}

	/**
	 * Adds a player to the list of those that interacted when alive.
	 * Means they die when changing kit or team
	 */
	public static void playerInteracted(UUID uuid) {
		if (!beenInCombat.contains(uuid)) {beenInCombat.add(uuid);}
	}

	/**
	 * Returns true if the player has taken damage in the last 8s
 	 */
	public static boolean isPlayerInCombat(UUID uuid) {
		return inCombat.get(uuid) != null && inCombat.get(uuid) <= 0;
	}

	/**
	 * Clears the combat lists
	 */
	public static void clearCombat() {
		beenInCombat = new ArrayList<>();
		inCombat = new HashMap<>();
	}
}
