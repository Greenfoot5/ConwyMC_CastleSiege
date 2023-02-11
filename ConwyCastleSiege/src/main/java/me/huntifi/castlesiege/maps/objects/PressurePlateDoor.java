package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class PressurePlateDoor extends Door {

    /**
     * Creates a new pressure plate door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param schematics The names of the two schematics
     * @param sounds The sounds to play when the door is closed/opened
     * @param timer How long the door stays open before automatically closing in ticks
     */
    public PressurePlateDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds, int timer) {
        super(flagName, centre, schematics, sounds, timer);
    }

    @Override
    protected boolean isCorrectInteraction(PlayerInteractEvent event) {
        if (!super.isCorrectInteraction(event))
            return false;

        return Objects.equals(event.getPlayer().getWorld(), centre.getWorld())
                && event.getPlayer().getLocation().distance(centre) <= 4;
    }

    @Override
    protected boolean isCorrectAction(Action action) {
        return action == Action.PHYSICAL;
    }

    @Override
    protected boolean isCorrectBlockType(Block block) {
        if (block == null)
            return false;

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
                return true;
            default:
                return false;
        }
    }
}
