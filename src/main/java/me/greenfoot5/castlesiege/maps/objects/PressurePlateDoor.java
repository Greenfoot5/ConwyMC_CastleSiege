package me.greenfoot5.castlesiege.maps.objects;

import me.greenfoot5.conwymc.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * A door that's opened with a pressure plate
 */
public class PressurePlateDoor extends Door {

    /** The default closed sound */
    public static final String defaultClosedSound = "BLOCK_WOODEN_DOOR_OPEN";

    /** The default open sound */
    public static final String defaultOpenSound = "BLOCK_WOODEN_DOOR_OPEN";

    /** The default timer in seconds */
    public static final int defaultTimer = 2;

    /** The maximum distance a player is allowed to be from the centre of the door to open it */
    private static final int maxDistance = 4;

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
                && event.getPlayer().getLocation().distance(centre) <= maxDistance;
    }

    @Override
    protected boolean isCorrectAction(Action action) {
        return action == Action.PHYSICAL;
    }

    @Override
    protected boolean isCorrectBlockType(Block block) {
        if (block == null)
            return false;

        //noinspection EnhancedSwitchMigration
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
