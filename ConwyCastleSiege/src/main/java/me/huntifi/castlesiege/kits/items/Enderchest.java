package me.huntifi.castlesiege.kits.items;

import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * Handles events relating to the enderchest
 */
public class Enderchest implements Listener {

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

			Kit kit = Kit.equippedKits.get(uuid);
			kit.refillItems(uuid);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
					ChatColor.DARK_GREEN + "Equipment resupplied"));

		}
	}
}
