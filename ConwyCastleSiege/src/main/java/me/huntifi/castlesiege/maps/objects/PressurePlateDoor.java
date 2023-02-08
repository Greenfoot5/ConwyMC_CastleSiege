package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class PressurePlateDoor extends Door {

    /**
     * Creates a new door
     *
     * @param flagName The flag or map name the door is assigned to
     * @param centre   The centre of the door (point for checking distance and playing the sound from)
     * @param schematics   The names of the two schematics
     * @param sounds   The sounds to play when the door is closed/opened
     * @param timer   How long the door stays open before automatically closing in ticks
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
        if (isIncorrectAction(event.getAction()) || isIncorrectBlockType(event.getClickedBlock()))
            return;

        // Make sure the player is playing, and the flag is on the correct map
        if (Objects.equals(Objects.requireNonNull(centre.getWorld()).getName(), MapController.getCurrentMap().worldName)) {

            Player player = event.getPlayer();
            double distance = player.getLocation().distance(centre);
            if (distance <= 4) {

                if (isEnemyControlled(player))
                    return;

                activate();
            }
        }
    }

    @Override
    protected boolean isIncorrectAction(Action action) {
        return action != Action.PHYSICAL;
    }

    @Override
    protected boolean isIncorrectBlockType(Block block) {
        if (block == null)
            return true;

        switch (block.getType()) {
            case ACACIA_PRESSURE_PLATE:
            case BIRCH_PRESSURE_PLATE:
            case CRIMSON_PRESSURE_PLATE:
            case DARK_OAK_PRESSURE_PLATE:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case JUNGLE_PRESSURE_PLATE:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case OAK_PRESSURE_PLATE:
            case POLISHED_BLACKSTONE_PRESSURE_PLATE:
            case SPRUCE_PRESSURE_PLATE:
            case STONE_PRESSURE_PLATE:
            case WARPED_PRESSURE_PLATE:
                return false;
            default:
                return true;
        }
    }
}
