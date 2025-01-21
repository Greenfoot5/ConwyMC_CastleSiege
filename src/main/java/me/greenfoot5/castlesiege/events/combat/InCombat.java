package me.greenfoot5.castlesiege.events.combat;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.maps.objects.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * A class to manage which players have and haven't been in combat
 */
public class InCombat implements Listener {

	private static Collection<UUID> inLobby = new ArrayList<>();
	private static HashMap<UUID, Integer> inCombat = new HashMap<>();

	/**
	 * When a player attacks another player, they have interacted
     * @param ed The event
     */
	@EventHandler(ignoreCancelled = true)
	public void battles(EntityDamageByEntityEvent ed) {
		if (!CSActiveData.hasPlayer(ed.getEntity().getUniqueId()))
			return;

		// Both are players
		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player)
			addPlayerToCombat(ed.getDamager().getUniqueId());
	}

	/**
	 * When a player takes any damage, they are placed in combat
     * @param event The event
     */
	@EventHandler(ignoreCancelled = true)
	public void playerTakesDamage(EntityDamageEvent event) {
		if (!CSActiveData.hasPlayer(event.getEntity().getUniqueId()))
			return;

		if (!(event.getEntity() instanceof Player))
			return;

		UUID uuid = event.getEntity().getUniqueId();
		addPlayerToCombat(uuid);
	}

	/**
	 * Adds a player to the inCombat
	 * @param uuid the UUID of the player to add
	 */
	public static void addPlayerToCombat(UUID uuid) {
		inCombat.merge(uuid, 1, Integer::sum);

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
     * @param uuid The uuid that died
     */
	public static void playerDied(UUID uuid) {
		inCombat.put(uuid, 0);
		if (!isPlayerInLobby(uuid)) {
			inLobby.add(uuid);
		}
	}

	/**
	 * Adds a player to the list of those that interacted when alive.
	 * Means they die when changing kit or team
     * @param uuid The uuid that spawned in
     */
	public static void playerSpawned(UUID uuid) {
		inLobby.remove(uuid);
	}

	/**
	 * Returns true if the player is still in the lobby
     * @param uuid The uuid of the player to check
     * @return true if the player is in the lobby
     */
	public static boolean isPlayerInLobby(UUID uuid) {
		return inLobby.contains(uuid);
	}

	/**
	 * Returns true if the player has taken damage in the last 8s
     * @param uuid The uuid to check
     * @return true if the uuid has recently fought someone
     */
	public static boolean isPlayerInCombat(UUID uuid) {
		return inCombat.get(uuid) != null && inCombat.get(uuid) > 0;
	}

	/**
	 * Clears the combat lists
	 */
	public static void clearCombat() {
		inCombat = new HashMap<>();
		inLobby = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			inLobby.add(player.getUniqueId());
		}
	}

	/**
	 * Removes a player from combat
	 * @param uuid The UUID to clear
	 */
	public static void remove(UUID uuid) {
		inCombat.remove(uuid);
		inLobby.remove(uuid);

		Player player = Bukkit.getPlayer(uuid);
		if (player == null)
			return;

		for (Flag flag : MapController.getCurrentMap().flags) {
			flag.playerExit(player);
		}
	}
}
