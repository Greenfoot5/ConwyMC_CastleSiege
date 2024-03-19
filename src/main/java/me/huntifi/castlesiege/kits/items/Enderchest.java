package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.EnderchestEvent;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles events relating to the enderchest
 */
public class Enderchest implements Listener {

	public static final int cooldownAmount = 400;

	/** Tracks the players that are on cooldown. */
	private static final ArrayList<UUID> cooldown = new ArrayList<>();

	/**
	 * Refills a player's inventory when they click an enderchest
	 * @param e The event called when a player clicks an enderchest
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) &&
				Objects.requireNonNull(e.getClickedBlock()).getType() == Material.ENDER_CHEST){
			Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
				Player player = e.getPlayer();

				if (checkAndApplyCooldown(player.getUniqueId()))
					Bukkit.getScheduler().runTask(Main.plugin, () ->
							Bukkit.getPluginManager().callEvent(new EnderchestEvent(player)));
				else
					Messenger.sendActionError("The enderchest is currently on cooldown", player);
			});
		}
	}

	/**
	 * Check whether a player is currently on cooldown.
	 * Apply cooldown if they are not.
	 * @param uuid The unique ID of the player
	 * @return Whether the player was not yet on cooldown
	 */
	private synchronized boolean checkAndApplyCooldown(UUID uuid) {
		if (cooldown.contains(uuid))
			return false;

		cooldown.add(uuid);
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> cooldown.remove(uuid), cooldownAmount);
		return true;
	}

	/**
	 * Resupply a players items when an enderchest is used.
	 * @param event The event called when an off-cooldown player interacts with an enderchest
	 */
	@EventHandler (priority = EventPriority.LOWEST)
	public void onClickEnderchest(EnderchestEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Kit kit = Kit.equippedKits.get(uuid);
		kit.refillItems(uuid);
		Messenger.sendActionSuccess("Equipment resupplied", player);
	}
}
