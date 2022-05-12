package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Door implements Listener {
    private boolean open;
    private final String flagName;
    private final Location centre;
    private final Tuple<Vector, Material>[] doorBlocks;

    /**
     * Crates a new door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param blocks The blocks that make up the door
     */
    public Door(String flagName, Location centre, Tuple<Vector, Material>[] blocks) {
        open = false;
        this.flagName = flagName;
        this.centre = centre;
        doorBlocks = blocks;
    }

    /**
     * Handles the opening and then closing of the door when a player steps on the pressure plates
     * @param event Called when a player moves
     */
    @EventHandler
    public void onPressure(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null
                || event.getClickedBlock().getType() != Material.STONE_PRESSURE_PLATE) {
            return;
        }

        // Make sure the player is playing, and the flag is on the correct map
        if (Objects.equals(Objects.requireNonNull(centre.getWorld()).getName(), MapController.getCurrentMap().worldName)) {

            Player player = event.getPlayer();
            double distance = player.getLocation().distance(centre);
            if (distance <= 3) {

                Flag flag = MapController.getCurrentMap().getFlag(flagName);
				if (Objects.equals(flagName, MapController.getCurrentMap().name) ||
                        Objects.equals(Objects.requireNonNull(flag).getCurrentOwners(), MapController.getCurrentMap().getTeam(player.getUniqueId()).name)) {
					if (!open) {
                        open = true;

                        for (Tuple<Vector, Material> tuple : doorBlocks) {
                            tuple.getFirst().toLocation(Objects.requireNonNull(centre.getWorld())).getBlock().setType(Material.AIR);
                        }
                        Objects.requireNonNull(centre.getWorld()).playSound(centre, Sound.BLOCK_WOODEN_DOOR_OPEN, 3, 1);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Tuple<Vector, Material> tuple : doorBlocks) {
                                    tuple.getFirst().toLocation(centre.getWorld()).getBlock().setType(tuple.getSecond());
                                }
                                Objects.requireNonNull(centre.getWorld()).playSound(centre, Sound.BLOCK_WOODEN_DOOR_OPEN, 3, 1);
                                open = false;
                            }
                        }.runTaskLater(Main.plugin, 40);
                    }
				} else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this door. You need to capture " + flagName + " first!"));
				}
            }
        }
    }
}
