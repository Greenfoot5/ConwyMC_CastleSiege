package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class LeverDoor extends Door {
    private final Location leverPosition;

    /**
     * Creates a new lever door
     * @param flagName The flag or map name the door is assigned to
     * @param centre  The centre of the door (point for checking distance and playing the sound from)
     * @param schematics  The names of the two schematics
     * @param sounds  The sounds to play when closing/opening the door
     * @param timer How long the door stays open before automatically closing in ticks
     * @param leverPosition The position of the lever
     */
    public LeverDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds,
                     int timer, Location leverPosition) {
        super(flagName, centre, schematics, sounds, timer);
        this.leverPosition = leverPosition;
    }

    /**
     * Handles the opening and then closing of the door when a player flicks a switch
     * @param event Called when a player moves
     */
    @EventHandler
    public void onSwitch(PlayerInteractEvent event) {
        if (isIncorrectAction(event.getAction()) || isIncorrectBlockType(event.getClickedBlock()))
            return;

        // Make sure the player is playing, and the flag is on the correct map
        if (Objects.equals(Objects.requireNonNull(centre.getWorld()).getName(), MapController.getCurrentMap().worldName)) {

            Player player = event.getPlayer();
            if (event.getClickedBlock().getLocation().distanceSquared(leverPosition) < 1) {

                if (isEnemyControlled(player))
                    return;

                // If the gate is closed (lever powered)
                if (((Powerable) event.getClickedBlock().getBlockData()).isPowered())
                    activate();
                else
                    close();
            }
        }
    }

    @Override
    protected void close() {
        super.close();
        Powerable leverData = (Powerable) leverPosition.getBlock().getBlockData();
        leverData.setPowered(true);
        leverPosition.getBlock().setBlockData(leverData);
    }

    @Override
    protected boolean isIncorrectAction(Action action) {
        return action != Action.RIGHT_CLICK_BLOCK;
    }

    @Override
    protected boolean isIncorrectBlockType(Block block) {
        return block == null || block.getType() != Material.LEVER;
    }
}
