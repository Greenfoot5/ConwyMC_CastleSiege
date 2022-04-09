package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.kits.Kit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class WoolMap implements Listener {

	public WoolMapBlock[] woolMapBlocks;

	/**
	 * Used to measure when a player clicks on a WoolMap sign
	 * @param e The click
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player player = e.getPlayer();

		Block target = player.getTargetBlockExact(50);

		if (target != null && target.getState() instanceof Sign) {
			Sign s = (Sign) target.getState();
			for (WoolMapBlock block : woolMapBlocks) {
				if (s.getLine(1).equalsIgnoreCase(block.flagName)) {
					// Remove mount
					if (player.isInsideVehicle()) {
						player.getVehicle().remove();
					}
					// Spawn player with kit
					block.SpawnPlayer(player.getUniqueId());
					Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
				} else if (s.getLine(1).equalsIgnoreCase("Stair Hall") && Objects.equals(block.flagName, "The Stair Hall")) {
					// Remove mount
					if (player.isInsideVehicle()) {
						player.getVehicle().remove();
					}
					// Spawn player with kit
					block.SpawnPlayer(player.getUniqueId());
					Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
				}
			}
		}
	}
}