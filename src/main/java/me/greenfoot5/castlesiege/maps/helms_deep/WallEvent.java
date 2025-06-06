package me.greenfoot5.castlesiege.maps.helms_deep;

import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.kits.items.WoolHat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.castlesiege.structures.SchematicSpawner;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
public class WallEvent implements Listener {
	private int tnt_counter = 0;
	private UUID carrier;

	private static final Location PICKUP_LOCATION = new Location(Bukkit.getWorld("HelmsDeep"), 1168, 35, 1125);
	private static final Location PLACE_LOCATION = new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1124);
	private static final Location[] TNT_LOCATIONS = new Location[] {
			new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1124),
			new Location(Bukkit.getWorld("HelmsDeep"), 1026, 34, 1123),
			new Location(Bukkit.getWorld("HelmsDeep"), 1027, 34, 1123)};

	private static final int DEATH_RADIUS = 15;

	/**
	 * Called when the player picks up the TNT on Helms Deep
     * @param e When a player tries to grab the tnt
     */
	@EventHandler
	public void onPickupTake(PlayerInteractEvent e) {
		// Check we're on HelmsDeep
        if ((MapController.getCurrentMap() != null && !MapController.getCurrentMap().worldName.equals("HelmsDeep")) || !MapController.isOngoing()) {
            return;
        }

        Player player = e.getPlayer();
        // Check if the player is even in the same world (this was in addition to duels and coinshop)!
        if (player.getWorld() != PICKUP_LOCATION.getWorld()) {
            return;
        }
        // Check the player has right-clicked a block while standing within 5 blocks of the spawner
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK
                || !(player.getLocation().distanceSquared(PICKUP_LOCATION) <= 5 * 5)) {
            return;
        }
        // If the player is on Rohan do nothing.
        if (Objects.equals(TeamController.getTeam(player.getUniqueId()).getName(),
                MapController.getCurrentMap().teams[0].getName())) {
            return;
        }
        //rogue's should not be able to pick up the tnt
        if (Objects.equals(Kit.equippedKits.get(e.getPlayer().getUniqueId()).name, "rogue") &&
                (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.TNT
                        || Objects.requireNonNull(e.getClickedBlock()).getType() == Material.GLOWSTONE)) {
            Messenger.sendActionError("Rogue's can't pickup the tnt or torch!", player);
            return;
        }
        // Check the player isn't on the defending team
        if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.TNT && tnt_counter < TNT_LOCATIONS.length) {

            // Replace the block with air
            e.getClickedBlock().setType(Material.AIR);

            // Set the player's hat to be the TNT
            ItemStack tnt = new ItemStack(Material.TNT);
            ItemMeta tntMeta = tnt.getItemMeta();
            assert tntMeta != null;
            tntMeta.displayName(Component.text("TNT-Head", NamedTextColor.GREEN));
            player.getInventory().setHelmet(tnt);

            // Notify the player
            Messenger.sendActionInfo("You picked up the explosives!", player);
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
            tntMeta.displayName(Component.text("Glowstone Head", NamedTextColor.GREEN));
            player.getInventory().setHelmet(tnt);

            // Notify the player(s)
            Messenger.sendInfo("You picked up the torch!", player);
            Messenger.broadcastWarning(CSNameTag.mmUsername(player) + " has picked up the torch!");
            carrier = player.getUniqueId();
        }
    }

	/**
	 * Called when the player places a block
     * @param e When the player tries to place the tnt
     */
	@EventHandler
	public void onPickupPlace(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		// Check we're playing on Helms Deep
        if (!MapController.getCurrentMap().worldName.equals("HelmsDeep")) {
            return;
        }
        // Check the player is currently carrying the tnt
        if (p.getUniqueId() != carrier || e.getClickedBlock() == null) {
            return;
        }

        if (!(e.getClickedBlock().getLocation().distanceSquared(PLACE_LOCATION) <= 5 * 5)) {
            return;
        }

        // Reset stuff
        WoolHat.setHead(p);
        carrier = null;

        // The player is trying to place tnt
        if (tnt_counter < TNT_LOCATIONS.length) {
            // Place the tnt
            TNT_LOCATIONS[tnt_counter].getBlock().setType(Material.TNT);
            tnt_counter++;

            // Inform the player and grant stats
            Messenger.sendInfo("You placed the explosive down. (" + tnt_counter + "/" + TNT_LOCATIONS.length + ")", p);
            UpdateStats.addSupports(p.getUniqueId(), 30);

            // Spawn either the torch or tnt next
            if (tnt_counter == 3) {
                Messenger.sendInfo("Now get the torch!", p);
                PICKUP_LOCATION.getBlock().setType(Material.TORCH);
            } else {
                PICKUP_LOCATION.getBlock().setType(Material.TNT);
            }

        // The player is placing the torch
        } else if (tnt_counter == 3) {

            // Paste the wall in the correct location
            final Location wallLoc = new Location(Bukkit.getWorld("HelmsDeep"), 1040, 34, 1140);
            SchematicSpawner.spawnSchematic(wallLoc, "HelmsdeepWallBroken");

            // Kill any players within DEATH_RADIUS blocks
            for (Player close : Bukkit.getOnlinePlayers()) {
                double closeDistance = close.getLocation().distanceSquared(PLACE_LOCATION);
                if (closeDistance < DEATH_RADIUS * DEATH_RADIUS && TeamController.isPlaying(close)) { close.setHealth(0); }
            }

            // Create our explosion
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).createExplosion(TNT_LOCATIONS[0], 15, false, false, p);
            UpdateStats.addSupports(p.getUniqueId(), 60);

            // Play various sound effects to make it sound like a massive explosion
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[0], Sound.ENTITY_GENERIC_EXPLODE , 10000, 2 );
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[1], Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR , 10000, 2 );
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[2], Sound.ENTITY_FIREWORK_ROCKET_BLAST , 10000, 2 );
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[0], Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST , 10000, 2 );
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[1], Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 10000, 2 );
            Objects.requireNonNull(Bukkit.getWorld("HelmsDeep")).playSound(TNT_LOCATIONS[2], Sound.ENTITY_DRAGON_FIREBALL_EXPLODE , 10000, 2 );
            Messenger.broadcastWarning("The Deeping Wall has been blown up!");

            tnt_counter++;
        }
    }

	/**
	 * @param e The carrier of the tnt/torch dies
	 */
	@EventHandler
	public void onCarrierDeath(PlayerDeathEvent e) {

		if (nullCarrier(e.getEntity())) {
			Player player = e.getEntity();
			WoolHat.setHead(player);
		}
	}

	/**
	 * @param e The carrier of the tnt/torch leaves the game
	 */
	@EventHandler
	public void onCarrierQuit(PlayerQuitEvent e) {
		nullCarrier(e.getPlayer());
	}

	/**
	 * @param e The carrier of the tnt/torch changes world
	 */
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
		if (MapController.getCurrentMap().worldName.equals("HelmsDeep")) {

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
}
