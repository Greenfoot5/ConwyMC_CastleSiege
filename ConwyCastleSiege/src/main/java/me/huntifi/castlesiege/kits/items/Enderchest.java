package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.secrets.SecretItems;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Handles events relating to the enderchest
 */
public class Enderchest implements Listener {

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
			Player p = e.getPlayer();
			UUID uuid = p.getUniqueId();

			if (cooldown.contains(uuid)) {
				// Player is on cooldown
				Messenger.sendActionError("The enderchest is currently on cooldown", p);
			} else {
				Kit kit = Kit.equippedKits.get(uuid);
				kit.refillItems(uuid);
				SecretItems.giveSecretOnEnderchest(p);
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
						ChatColor.DARK_GREEN + "Equipment resupplied"));
				applyCooldown(uuid);
			}
		}
	}

	/**
	 * Set the player's cooldown and remove after some time
	 * @param uuid The UUID of the player
	 */
	private void applyCooldown(UUID uuid) {
		cooldown.add(uuid);
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldown.remove(uuid);
			}
		}.runTaskLater(Main.plugin, 100);
	}
}
