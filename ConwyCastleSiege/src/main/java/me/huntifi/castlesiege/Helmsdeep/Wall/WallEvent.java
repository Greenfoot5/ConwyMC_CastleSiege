package me.huntifi.castlesiege.Helmsdeep.Wall;


import com.sk89q.worldedit.WorldEditException;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.structures.MakeStructure;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.UUID;

/**
 * A class to handle the wall explosion on HelmsDeep
 */
public class WallEvent implements Listener, Runnable {
	private int tnt_counter = 0;
	private UUID carrier;

	private final static Location PICKUP_LOCATION = new Location(Bukkit.getWorld("HelmsDeep"), 1168, 35, 1125);
	private final static Location PLACE_LOCATION = new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1124);
	private final static Location[] TNT_LOCATIONS = new Location[] {
			new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1124),
			new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1123),
			new Location(Bukkit.getWorld("HelmsDeep"), 1027, 34, 1123)};

	private final static int DEATH_RADIUS = 15;

	/**
	 * Called when the player picks up the TNT on Helms Deep
	 */
	@EventHandler
	public void onPickupTake(PlayerInteractEvent e) {

		Player player = e.getPlayer();

		// Check we're on HelmsDeep
		if(MapController.currentMapIs("HelmsDeep")) {
			// Check the player has right-clicked a block while standing within 5 blocks of the spawner
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && player.getLocation().distance(PICKUP_LOCATION) <= 5) {
				// If the player clicks on some TNT
				if(!Objects.equals(MapController.getCurrentMap().getTeam(player.getUniqueId()).name,
						MapController.getCurrentMap().teams[0].name)) {
					// Check the player isn't on the defending team
					if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.TNT && tnt_counter < TNT_LOCATIONS.length) {

						// Replace the block with air
						e.getClickedBlock().setType(Material.AIR);

						// Set the player's hat to be the TNT
						ItemStack tnt = new ItemStack(Material.TNT);
						ItemMeta tntMeta = tnt.getItemMeta();
						assert tntMeta != null;
						tntMeta.setDisplayName(ChatColor.GREEN + "Tnt-head");
						player.getInventory().setHelmet(tnt);

						// Notify the player
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA
								+ "You picked up the explosives!"));
						carrier = player.getUniqueId();

					// The player clicked on the torch
					} else if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.TORCH
							&& tnt_counter == TNT_LOCATIONS.length) {

						// Replace the block with air
						e.getClickedBlock().setType(Material.AIR);

						// Set the player's hat to be the TNT
						ItemStack tnt = new ItemStack(Material.GLOWSTONE);
						ItemMeta tntMeta = tnt.getItemMeta();
						assert tntMeta != null;
						tntMeta.setDisplayName(ChatColor.GREEN + "Glowstone-head");
						player.getInventory().setHelmet(tnt);

						// Notify the player(s)
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA
								+ "You picked up the torch!"));
						Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has picked up the torch!");
						carrier = player.getUniqueId();
					}
				}
			}
		}
	}

	/**
	 * Called when the player places a block
	 */
	@EventHandler
	public void onPickupPlace(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		// Check we're playing on Helms Deep
		if(MapController.currentMapIs("HelmsDeep")) {
			// Check the player is currently carrying the tnt
			if (p.getUniqueId() == carrier) {

				if (Objects.requireNonNull(e.getClickedBlock()).getLocation().distance(PLACE_LOCATION) <= 5) {

					// Reset stuff
					WoolHat.setHead(p);
					carrier = null;

					// The player is trying to place tnt
					if (tnt_counter < TNT_LOCATIONS.length) {
						// Place the tnt
						TNT_LOCATIONS[0].getBlock().setType(Material.TNT);
						tnt_counter++;

						// Inform the player and grant stats
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(ChatColor.DARK_AQUA
								+ "You placed the explosive down. (" + tnt_counter + 1 + "/" + TNT_LOCATIONS.length + ")"));
						Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () ->
								MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 12.0 ));

						// Spawn either the torch or tnt next
						if (tnt_counter == 3) {
							p.sendMessage(ChatColor.DARK_AQUA + "Now get the torch!");
							PICKUP_LOCATION.getBlock().setType(Material.TORCH);
						} else {
							PICKUP_LOCATION.getBlock().setType(Material.TNT);
						}

					// The player is placing the torch
					} else if (tnt_counter == 3) {

						// Paste the wall in the correct location
						final Location wallLoc = new Location(Bukkit.getWorld("HelmsDeep"), 1040, 34, 1140);
						try {
							MakeStructure.createSchematicStructure(wallLoc, "HelmsdeepWallBroken", "HelmsDeep");
						} catch (WorldEditException e1) {
							e1.printStackTrace();
						}

						// Kill any players within DEATH_RADIUS blocks
						for (Player close : Bukkit.getOnlinePlayers()) {
							double closeDistance = close.getLocation().distance(PLACE_LOCATION);
							if (closeDistance < DEATH_RADIUS) { close.setHealth(0); }
						}

						// Create our explosion
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).createExplosion(TNT_LOCATIONS[0], 15, false, false, p);

						// Give the player score
						Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () ->
								MVPstats.setSupports(p.getUniqueId(), MVPstats.getSupports(p.getUniqueId()) + 30.0 ));

						// Play various sound effects to make it sound like a massive explosion
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[0], Sound.ENTITY_GENERIC_EXPLODE , 10000, 2 );
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[1], Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR , 10000, 2 );
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[2], Sound.ENTITY_FIREWORK_ROCKET_BLAST , 10000, 2 );
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[0], Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST , 10000, 2 );
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[1], Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 10000, 2 );
						Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[2], Sound.ENTITY_DRAGON_FIREBALL_EXPLODE , 10000, 2 );
						Bukkit.broadcastMessage(ChatColor.RED + "The Deeping Wall has been blown up!");

						tnt_counter++;
					}
				}
			}
		}
	}

	@EventHandler
	public void onCarrierDeath(PlayerDeathEvent e) {

		if (nullCarrier(e.getEntity())) {
			Player player = e.getEntity();
			WoolHat.setHead(player);
		}
	}

	@EventHandler
	public void onCarrierQuit(PlayerQuitEvent e) {
		nullCarrier(e.getPlayer());
	}

	@EventHandler
	public void onCarrierLeave(PlayerChangedWorldEvent e) {
		nullCarrier(e.getPlayer());
	}

	/**
	 * Cleans up any issues when the carrier is no more
	 * i.e. the carrier leaves or dies
	 * @param player The carrier
	 * @return If the player was the carrier
	 */
	private boolean nullCarrier(Player player) {
		if (MapController.currentMapIs("HelmsDeep")) {

			assert player != null;
			if (player.getWorld() == (Bukkit.getWorld("HelmsDeep"))) {
				if (carrier == player.getUniqueId()) {
					carrier = null;

					if (tnt_counter == 3) {
						PICKUP_LOCATION.getBlock().setType(Material.TORCH);
					} else if (tnt_counter < 3) {
						PICKUP_LOCATION.getBlock().setType(Material.TNT);
					}

					return true;
				}
			}
		}
		return false;
	}


	@Override
	public void run() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			if(MapController.currentMapIs("HelmsDeep")) {
				if(online.getWorld() == (Bukkit.getWorld("HelmsDeep"))) {
					if (!(tnt_counter == 4)) {
						if (Objects.requireNonNull(online.getInventory().getHelmet()).getType() == Material.TNT) {
							online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "" + ChatColor.BOLD + "You are holding an explosive!"));
						} else if (online.getInventory().getHelmet().getType() == Material.GLOWSTONE) {
							online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "" + ChatColor.BOLD + "You are holding the torch!"));
						}
					}
				}
			}
		}
	}
}
