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
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class LeverDoor extends Door {
    private final Location leverPosition;

    /**
     * Creates a new door
     *
     * @param flagName The flag or map name the door is assigned to
     * @param centre  The centre of the door (point for checking distance and playing the sound from)
     * @param blocks  The blocks that make up the door
     * @param sounds  The sounds to play when closing/opening the door
     * @param levelPosition The position of the lever
     */
    public LeverDoor(String flagName, Location centre, Tuple<Vector, Tuple<Material, Material>>[] blocks, Tuple<Sound, Sound> sounds,
                     int timer, Location levelPosition) {
        super(flagName, centre, blocks, sounds, timer);
        this.leverPosition = levelPosition;
    }

    /**
     * Handles the opening and then closing of the door when a player flicks a switch
     * @param event Called when a player moves
     */
    @EventHandler
    public void onSwitch(PlayerInteractEvent event) {
        if (event.getClickedBlock().getType() == Material.LEVER) {
            return;
        }

        // Make sure the player is playing, and the flag is on the correct map
        if (Objects.equals(Objects.requireNonNull(centre.getWorld()).getName(), MapController.getCurrentMap().worldName)) {

            Player player = event.getPlayer();
            if (event.getClickedBlock().getLocation() == leverPosition) {

                Powerable leverData = (Powerable) event.getClickedBlock().getBlockData();
                Flag flag = MapController.getCurrentMap().getFlag(flagName);
                if (Objects.equals(flagName, MapController.getCurrentMap().name) ||
                        Objects.equals(Objects.requireNonNull(flag).getCurrentOwners(), MapController.getCurrentMap().getTeam(player.getUniqueId()).name)) {
                    // If the gate is closed (lever powered)
                    if (leverData.isPowered()) {
                        open();

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!leverData.isPowered())
                                    return;
                                close();
                                leverData.setPowered(false);
                            }
                        }.runTaskLater(Main.plugin, timer);
                    } else {
                        close();
                    }
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this! You need to capture " + flagName + " first!"));
                }
            }
        }
    }
}
