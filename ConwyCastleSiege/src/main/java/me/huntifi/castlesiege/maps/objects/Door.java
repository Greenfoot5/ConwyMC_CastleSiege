package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Door implements Listener {
    private boolean open;
    private final String flagName;
    private final Location centre;
    private final Tuple<Location, Material>[] doorBlocks;

    public Door(String flagName, Location centre, Tuple<Location, Material>[] blocks) {
        open = false;
        this.flagName = flagName;
        this.centre = centre;
        doorBlocks = blocks;
    }


    @EventHandler
    public void onPressure(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        if (flag != null) {

            double distance = player.getLocation().distance(centre);

            if (player.getLocation().getBlock().getType().equals(Material.STONE_PRESSURE_PLATE) && distance <= 3) {

				if (Objects.equals(flag.currentOwners, MapController.getCurrentMap().getTeam(player.getUniqueId()).name)) {

					if (!open) {

                        open = true;
                        
                        for (Tuple<Location, Material> tuple : doorBlocks) {
                            tuple.getFirst().getBlock().setType(Material.AIR);
                        }

                        Objects.requireNonNull(centre.getWorld()).playSound(centre, Sound.BLOCK_WOODEN_DOOR_OPEN, 3, 1);

                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                for (Tuple<Location, Material> tuple : doorBlocks) {
                                    tuple.getFirst().getBlock().setType(tuple.getSecond());
                                }

                                Objects.requireNonNull(centre.getWorld()).playSound(centre, Sound.BLOCK_WOODEN_DOOR_OPEN, 3, 1);

                                open = false;
                            }

                        }.runTaskLater(Main.plugin, 40);
                    }

				} else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this door."));
				}
            }
        }
    }
}
