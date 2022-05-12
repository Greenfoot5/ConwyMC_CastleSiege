package me.huntifi.castlesiege.events.security;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import java.util.Objects;

/**
 * Prevents players from altering the map
 */
public class MapProtection implements Listener {

	/**
	 * Cancels event when player places block
	 * @param e The event called when a player places a block
	 */
	@EventHandler (priority = EventPriority.LOW)
	 public void onBlockPlace(BlockPlaceEvent e) {
		// Allow building in creative mode
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		// Allow placing ladders and cobwebs outside the lobby
		if ((e.getBlock().getType() != Material.LADDER && e.getBlock().getType() != Material.COBWEB)
				|| InCombat.isPlayerInLobby(p.getUniqueId())) {
			e.setCancelled(true);
		}
	 }

	/**
	 * Cancels event when player destroys a block
	 * @param e The event called when a player breaks a block
	 */
	@EventHandler (priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent e) {
		// Allow breaking in creative mode
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		// Allow breaking ladders and cobwebs outside the lobby
		if ((e.getBlock().getType() != Material.LADDER && e.getBlock().getType() != Material.COBWEB)
				|| InCombat.isPlayerInLobby(p.getUniqueId())) {
			e.setCancelled(true);
		} else {
			e.getBlock().setType(Material.AIR);
		}
	}

	/**
	 * Cancels event when player breaks item frame or painting
	 * @param e The event called when breaking an item frame or painting
	 */
	@EventHandler
	public void onHangingBreak(HangingBreakEvent e) {
		if (e.getCause() == HangingBreakEvent.RemoveCause.ENTITY) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player interacts with or item frame
	 * Doesn't work for armor stand for some reason
	 * @param e The event called when a player interacts with an entity
	 */
	@EventHandler
	public void onInteractItemFrame(PlayerInteractEntityEvent e) {
		// Allow interacting in creative mode
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if (e.getRightClicked() instanceof ItemFrame) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when entity damages armor stand or item frame
	 * @param e The event called when an entity is damaged
	 */
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e) {
		// Allow breaking in creative mode
		if (e.getDamager() instanceof Player && ((Player) e.getDamager()).getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player interacts with armor stand
	 * Doesn't work for item frame for some reason
	 * @param e The event called when a player interacts with an entity
	 */
	@EventHandler
	public void onInteractArmorStand(PlayerInteractAtEntityEvent e) {
		// Allow interacting in creative mode
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if (e.getRightClicked() instanceof ArmorStand) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player destroys a minecart
	 * @param e The event called when a minecart is destroyed
	 */
	@EventHandler
	public void onDestroyMinecart(VehicleDestroyEvent e) {
		// Allow breaking in creative mode
		if (e.getAttacker() instanceof Player && ((Player) e.getAttacker()).getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if (e.getVehicle() instanceof Minecart) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player destroys and end crystal
	 * @param e The event called when and end crystal explodes
	 */
	@EventHandler
	public void onDestroyEndCrystal(EntityExplodeEvent e) {
		if (e.getEntity() instanceof EnderCrystal) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player puts out fire
	 * @param e The event called when a player left clicks fire
	 */
	@EventHandler
	public void onInteractFire(PlayerInteractEvent e) {
		// Allow putting out in creative mode
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if (e.getAction() == Action.LEFT_CLICK_BLOCK &&
				Objects.requireNonNull(e.getClickedBlock()).getType() == Material.FIRE) {
			e.setCancelled(true);
		}
	}

	/**
	 * Cancels event when player tramples farmland
	 * @param e The event called when a player tramples farmland
	 */
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		// Allow trampling in creative mode
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}

		if(e.getAction() == Action.PHYSICAL &&
				Objects.requireNonNull(e.getClickedBlock()).getType() == Material.FARMLAND) {
			e.setCancelled(true);
		}
	}

	/**
	 * Stops tnt from being primed. Primed TNT will still explode
	 * @param e Called when tnt is primed
	 */
	@EventHandler
	public void onTNTPrimed(TNTPrimeEvent e) {
		e.setCancelled(true);
	}
}
