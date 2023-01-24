package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			// Prevent spawning by physical actions, e.g. stepping on a pressure plate
			if (e.getAction() == Action.PHYSICAL)
				return;

			Player player = e.getPlayer();
			Block target = player.getTargetBlockExact(50);

			if (target != null && target.getState() instanceof Sign) {
				for (WoolMapBlock block : woolMapBlocks) {
					if (Objects.equals(Objects.requireNonNull(block.blockLocation.getWorld()).getName(), MapController.getCurrentMap().worldName)) {
						if (target.getLocation().distance(block.signLocation) == 0
								&& (MapController.isOngoing() || MapController.timer.state == TimerState.EXPLORATION)
						&& !DeathEvent.onCooldown.contains(player)) {
							// Remove mount
							if (player.isInsideVehicle()) {
								Objects.requireNonNull(player.getVehicle()).remove();
							}

							// Check if player has enough battlepoints
							Kit kit = Kit.equippedKits.get(player.getUniqueId());
							// If the kit is a donator kit, take the cost
							if (kit instanceof DonatorKit)
							{
								DonatorKit donatorKit = (DonatorKit) kit;
								boolean hasBp = ActiveData.getData(player.getUniqueId()).takeBattlepoints(donatorKit.getBattlepointPrice());
								if (!hasBp) {
									Messenger.sendError("You do not have sufficient battlepoints (BP) to play this!", player);
									Messenger.sendError("Perform /battlepoints or /bp to see your battlepoints.", player);
									return;
								}
							}
							// Spawn player with kit
							block.spawnPlayer(player.getUniqueId());
							kit.setItems(player.getUniqueId());

						}
						else if (target.getLocation().distance(block.signLocation) == 0)
						{
							Messenger.sendError("You can't spawn in yet!", e.getPlayer());
							return;
						}
					}
				}
			}
		});
	}
}