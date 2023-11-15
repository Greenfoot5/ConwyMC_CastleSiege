package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
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
	public void lookAtWoolmap(PlayerMoveEvent e) {
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
			Block target = e.getPlayer().getTargetBlockExact(50);
			for (WoolMapBlock block : woolMapBlocks) {
				assert target != null;
				if (target.getLocation().distance(block.blockLocation) == 0) {
					spawnGlower(e.getPlayer(), block.blockLocation);
				}
			}
		}
	}


	public static final HashMap<Player, Shulker> shulkers = new HashMap<>();
	public void spawnGlower(Player p, Location loc) {
      if ((!shulkers.containsKey(p)) || shulkers.get(p).getLocation() != loc) {

		  ScoreboardManager manager = Bukkit.getScoreboardManager();
		  assert manager != null;
		  org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

		  Shulker shulk = (Shulker) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.SHULKER);
          shulkers.put(p, shulk);
		  shulk.setAI(false);
		  shulk.setInvulnerable(true);
		  shulk.setInvisible(true);
		  shulk.setCustomName(shulk.getUniqueId().toString());
		  p.showEntity(Main.plugin, shulk);

		  shulk.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 8000, 0, false, false));
          keepShulkerAlive(p, shulk, loc.getBlock());
	  }
	}

	public void keepShulkerAlive(Player p, Shulker shulk, Block b) {

		Block target = p.getTargetBlockExact(50);
		new BukkitRunnable() {
			@Override
			public void run() {
				assert target != null;
				if (target.equals(b)) {
					shulkers.remove(p, shulk);
					shulk.remove();
				} else {
					this.cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 20, 20);
	}


	/**
	 * Convert the  wool color to a chat color for the glowing block colour
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