package me.huntifi.castlesiege.events.combat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.maps.MapController;
import net.citizensnpcs.api.npc.NPC;
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

	public static Collection<NPC> botInLobby = new ArrayList<>();

	public static HashMap<NPC, Integer> botInCombat = new HashMap<>();

	/**
	 * When a player attacks another player, they have interacted
	 */
	@EventHandler
	public void battles(EntityDamageByEntityEvent ed) {
		if (ed.isCancelled()) { return; }
		// Both are players
		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {

			UUID whoWasHit = ed.getEntity().getUniqueId();
			UUID whoHit = ed.getDamager().getUniqueId();

			// Check they aren't on the same team
			if (MapController.getCurrentMap().getTeam(whoHit) != MapController.getCurrentMap().getTeam(whoWasHit)) {
				addPlayerToCombat(whoHit);
			}

		} else if (ed.getEntity() instanceof NPC && ed.getDamager() instanceof Player) {

			NPC whoWasHit = (NPC) ed.getEntity();
			UUID whoHit = ed.getDamager().getUniqueId();

			// Check they aren't on the same team
			if (MapController.getCurrentMap().getTeam(whoHit) != MapController.getCurrentMap().getTeam(whoWasHit)) {
				addPlayerToCombat(whoHit);
			}

		} else if (ed.getEntity() instanceof Player && ed.getDamager() instanceof NPC) {

			UUID whoWasHit = ed.getEntity().getUniqueId();
			NPC whoHit = (NPC) ed.getDamager();

			// Check they aren't on the same team
			if (MapController.getCurrentMap().getTeam(whoHit) != MapController.getCurrentMap().getTeam(whoWasHit)) {
				addBotToCombat(whoHit);
			}

		} else if (ed.getEntity() instanceof NPC && ed.getDamager() instanceof NPC) {

			NPC whoWasHit = (NPC) ed.getEntity();
			NPC whoHit = (NPC) ed.getDamager();

			// Check they aren't on the same team
			if (MapController.getCurrentMap().getTeam(whoHit) != MapController.getCurrentMap().getTeam(whoWasHit)) {
				addBotToCombat(whoHit);
			}

		}
	}

	/**
	 * When a player takes any damage, they are placed in combat
	 */
	@EventHandler
	public void playerTakesDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player) || event.isCancelled()) { return; }
		UUID uuid = event.getEntity().getUniqueId();

		addPlayerToCombat(uuid);
	}

	/**
	 * When a bot takes any damage, they are placed in combat
	 */
	@EventHandler
	public void botTakesDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof NPC) || event.isCancelled()) { return; }
		NPC npc = (NPC) event.getEntity();

		addBotToCombat(npc);
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
	 * Adds a bot to the inCombat
	 * @param npc the bot to add
	 */
	public static void addBotToCombat(NPC npc) {
		botInCombat.merge(npc, 1, Integer::sum);

		// Players are in combat for 10 seconds only
		new BukkitRunnable() {
			@Override
			public void run() {
				botInCombat.merge(npc, -1, Integer::sum);
			}
		}.runTaskLater(Main.plugin, 200);
	}

	/**
	 * Removes a player's uuid from all combat lists
	 */
	public static void playerDied(UUID uuid) {
		inCombat.put(uuid, 0);
		if (!isPlayerInLobby(uuid)) {
			inLobby.add(uuid);
		}
	}

	/**
	 * Removes the specified bot from all combat lists
	 */
	public static void botDied(NPC npc) {
		botInCombat.put(npc, 0);
		if (!isBotInLobby(npc)) {
			botInLobby.add(npc);
		}
	}

	/**
	 * Adds a player to the list of those that interacted when alive.
	 * Means they die when changing kit or team
	 */
	public static void playerSpawned(UUID uuid) {
		inLobby.remove(uuid);
	}

	/**
	 * Adds a bot to the list of those that interacted when alive.
	 * Means they die when changing kit or team
	 */
	public static void botSpawned(NPC npc) {
		botInLobby.remove(npc);
	}

	/**
	 * Returns true if the player is still in the lobby
	 */
	public static boolean isPlayerInLobby(UUID uuid) {
		return inLobby.contains(uuid);
	}

	/**
	 * Returns true if the bot is still in the lobby
	 */
	public static boolean isBotInLobby(NPC npc) {
		return botInLobby.contains(npc);
	}

	/**
	 * Returns true if the player has taken damage in the last 8s
 	 */
	public static boolean isPlayerInCombat(UUID uuid) {
		return inCombat.get(uuid) != null && inCombat.get(uuid) > 0;
	}

	/**
	 * Returns true if the bot has taken damage in the last 8s
	 */
	public static boolean isBotInCombat(UUID uuid) {
		return botInCombat.get(uuid) != null && botInCombat.get(uuid) > 0;
	}

	/**
	 * Clears the combat lists
	 */
	public static void clearCombat() {
		inCombat = new HashMap<>();
		inLobby = new ArrayList<>();
		botInCombat = new HashMap<>();
		botInLobby = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			inLobby.add(player.getUniqueId());
		}
	}
}
