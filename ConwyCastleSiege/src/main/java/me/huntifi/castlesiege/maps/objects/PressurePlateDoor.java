package me.huntifi.castlesiege.maps.objects;

import com.sk89q.worldedit.WorldEditException;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PressurePlateDoor extends Door {
    protected boolean open = false;

    /**
     * Creates a new door
     *
     * @param flagName The flag or map name the door is assigned to
     * @param centre   The centre of the door (point for checking distance and playing the sound from)
     * @param schematics   The names of the two schematics
     * @param sounds   The sounds to play when the door is closed/opened
     * @param timer   How long the door stays open before automatically closing
     */
    public PressurePlateDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds, int timer) {
        super(flagName, centre, schematics, sounds, timer);
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
            if (distance <= 4) {

                Flag flag = MapController.getCurrentMap().getFlag(flagName);
				if (Objects.equals(flagName, MapController.getCurrentMap().name) ||
                        Objects.equals(Objects.requireNonNull(flag).getCurrentOwners(), MapController.getCurrentMap().getTeam(player.getUniqueId()).name)) {
					if (!open) {
                        open = true;

                        try {
                            open();
                        } catch (WorldEditException e) {
                            throw new RuntimeException(e);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    close();
                                } catch (WorldEditException e) {
                                    throw new RuntimeException(e);
                                }
                                open = false;
                            }
                        }.runTaskLater(Main.plugin, timer);
                    }
				} else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this door. You need to capture " + flagName + " first!"));
				}
            }
        }
    }
}
