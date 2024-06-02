package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.donator.duels.DuelCommand;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

/**
 * A class to store data on a wool-map
 */
public class WoolMap implements Listener {

	public WoolMapBlock[] woolMapBlocks;
	private static final HashMap<Player, Shulker> shulkers = new HashMap<>();

	/**
	 * Used to measure when a player clicks on a WoolMap sign
	 * @param e The click
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		// Prevent spawning by physical actions, e.g. stepping on a pressure plate
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		Player player = e.getPlayer();
		Block target = player.getTargetBlockExact(50);

		if (target == null || !(target.getState() instanceof Sign))
			return;

		if (!MapController.isOngoing() && MapController.timer.state != TimerState.EXPLORATION)
			return;

		if (!InCombat.isPlayerInLobby(e.getPlayer().getUniqueId()))
			return;

		Bukkit.getScheduler().runTask(Main.plugin, () -> {
			for (WoolMapBlock block : woolMapBlocks) {

				if (Objects.equals(Objects.requireNonNull(block.blockLocation.getWorld()).getName(), MapController.getCurrentMap().worldName)) {
					if (target.getLocation().distance(block.signLocation) == 0) {
                        if (DeathEvent.onCooldown.contains(player)) {
                            Messenger.sendError("You can't spawn in yet!", e.getPlayer());
                        } else {
                            // Remove mount
                            if (player.isInsideVehicle()) {
                                Objects.requireNonNull(player.getVehicle()).remove();
                            }

                            // Spawn player with kit
                            block.spawnPlayer(player.getUniqueId());
                        }

						return;
                    }
				}
			}
		});
	}

	/**
	 * Handles block glows when a player is looking at the woolmap
	 * @param e A player move event
	 */
	//@EventHandler
	public void lookAtWoolmap(PlayerMoveEvent e) {
		if (DuelCommand.challenging.containsValue(e.getPlayer()) || DuelCommand.challenging.containsKey(e.getPlayer())) {
			return;
		}
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
			Block target = e.getPlayer().getTargetBlockExact(50);
			if (target == null) {
				return;
			}

			for (WoolMapBlock block : woolMapBlocks) {
				if (target.getLocation().distance(block.blockLocation) == 0) {
					spawnGlower(e.getPlayer(), block.blockLocation);
				}
			}
		}
	}


	private void spawnGlower(Player p, Location loc) {
      if ((!shulkers.containsKey(p)) || shulkers.get(p).getLocation() != loc) {

		  Shulker shulker = (Shulker) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.SHULKER);
          shulkers.put(p, shulker);
		  shulker.setAI(false);
		  shulker.setInvulnerable(true);
		  shulker.setInvisible(true);
		  shulker.customName(Component.text(shulker.getUniqueId().toString()));
		  p.showEntity(Main.plugin, shulker);

		  shulker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 8000, 0, false, false));
          keepShulkerAlive(p, shulker, loc.getBlock());
	  }
	}

	private void keepShulkerAlive(Player p, Shulker shulker, Block b) {

		Block target = p.getTargetBlockExact(50);
		new BukkitRunnable() {
			@Override
			public void run() {
				assert target != null;
				if (target.equals(b)) {
					shulkers.remove(p, shulker);
					shulker.remove();
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 20, 20);
	}
}