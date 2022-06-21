package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * A class to store data on a wool-map
 */
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
			for (WoolMapBlock block : woolMapBlocks) {
				if (Objects.equals(Objects.requireNonNull(block.blockLocation.getWorld()).getName(), MapController.getCurrentMap().worldName)) {
					if (target.getLocation().distance(block.signLocation) == 0
							&& (MapController.isOngoing() || MapController.timer.state == TimerState.EXPLORATION)) {
						// Remove mount
						if (player.isInsideVehicle()) {
							Objects.requireNonNull(player.getVehicle()).remove();
						}
						// Spawn player with kit
						block.SpawnPlayer(player.getUniqueId());
						Kit.equippedKits.get(player.getUniqueId()).setItems(player.getUniqueId());
					}
				}
			}
		}
	}
}