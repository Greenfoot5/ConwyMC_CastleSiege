package me.huntifi.castlesiege.maps;

import fr.skytasul.glowingentities.GlowingBlocks;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
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

							// Spawn player with kit
							block.spawnPlayer(player.getUniqueId());

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

	@EventHandler
	public void lookAtWoolmap(PlayerMoveEvent e) throws ReflectiveOperationException {
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
			Block target = e.getPlayer().getTargetBlockExact(50);
			for (WoolMapBlock block : woolMapBlocks) {
				assert target != null;
				if (target.getLocation().distance(block.blockLocation) == 0) {
					spawnGlower(e.getPlayer(), block);
				}
			}
		}
	}


	public static final HashMap<Player, Block> shulkers = new HashMap<>();
	public void spawnGlower(Player p, WoolMapBlock woolblock) throws ReflectiveOperationException {
      if ((!shulkers.containsKey(p)) || shulkers.get(p).getLocation() != woolblock.blockLocation ) {
		  Main.glowingblocks.setGlowing(woolblock.blockLocation.getBlock(), p , Objects.requireNonNull(getGlowColor(woolblock.blockLocation.getBlock())));
          shulkers.put(p, woolblock.blockLocation.getBlock());
		  new BukkitRunnable() {
			  @Override
			  public void run() {
				  try {
					  Main.glowingblocks.unsetGlowing(woolblock.blockLocation.getBlock(), p);
				  } catch (ReflectiveOperationException e) {
					  throw new RuntimeException(e);
				  }
				  shulkers.remove(p, woolblock.blockLocation.getBlock());
			  }
		  }.runTaskLater(Main.plugin, 60);
	  }
	}


	/**
	 * Convert the team's primary wool color to a chat color for the glowing block colour
	 * @param b the block to check for wool
	 * @return The chat colour corresponding to the team's primary wool color, null if primary wool is no wool
	 */
	private ChatColor getGlowColor(Block b) {
		switch (b.getType()) {
			case BLACK_WOOL:
				return ChatColor.BLACK;
			case BLUE_WOOL:
				return ChatColor.DARK_BLUE;
			case ORANGE_WOOL:
			case BROWN_WOOL:
				return ChatColor.GOLD;
			case CYAN_WOOL:
				return ChatColor.DARK_AQUA;
			case GRAY_WOOL:
				return ChatColor.DARK_GRAY;
			case GREEN_WOOL:
				return ChatColor.DARK_GREEN;
			case LIGHT_BLUE_WOOL:
				return ChatColor.AQUA;
			case LIGHT_GRAY_WOOL:
				return ChatColor.GRAY;
			case LIME_WOOL:
				return ChatColor.GREEN;
			case MAGENTA_WOOL:
				return ChatColor.LIGHT_PURPLE;
			case PURPLE_WOOL:
				return ChatColor.DARK_PURPLE;
			case PINK_WOOL:
				return ChatColor.RED;
			case RED_WOOL:
				return ChatColor.DARK_RED;
			case WHITE_WOOL:
				return ChatColor.WHITE;
			case YELLOW_WOOL:
				return ChatColor.YELLOW;
			default:
				return null;
		}
	}
}