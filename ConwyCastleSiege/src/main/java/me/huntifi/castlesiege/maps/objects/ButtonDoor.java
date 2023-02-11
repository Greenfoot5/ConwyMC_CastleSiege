package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * Represents a button door
 */
public class ButtonDoor extends Door {

    /** Maps the button locations to their opening delay */
    private final Tuple<Location, Integer>[] buttonPositionsAndDelays;

    /**
     * Creates a new button door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param schematics The blocks that make up the door
     * @param sounds The sounds to play when the door is closed/opened
     * @param timer How long the door stays open before automatically closing in ticks
     * @param buttonPositionsAndDelays The positions and delays until opening the door
     */
    public ButtonDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds,
                      int timer, Tuple<Location, Integer>[] buttonPositionsAndDelays) {
        super(flagName, centre, schematics, sounds, timer);
        this.buttonPositionsAndDelays = buttonPositionsAndDelays;
    }

    @Override
    protected boolean isCorrectInteraction(PlayerInteractEvent event) {
        if (!super.isCorrectInteraction(event))
            return false;

        Block button = event.getClickedBlock();
        assert button != null;
        return !((Powerable) button.getBlockData()).isPowered() && getButtonDelay(button) != null;
    }

    /**
     * Get the delay between clicking the button and opening the door,
     * or null if the button does not belong to this door.
     * @param button The button
     * @return The button delay or null if not applicable
     */
    private Integer getButtonDelay(Block button) {
        if (buttonPositionsAndDelays.length == 0 ||
                !Objects.equals(buttonPositionsAndDelays[0].getFirst().getWorld(), button.getWorld()))
            return null;

        for (Tuple<Location, Integer> buttonPositionAndDelay : buttonPositionsAndDelays) {
            if (buttonPositionAndDelay.getFirst().distance(button.getLocation()) == 0)
                return buttonPositionAndDelay.getSecond();
        }

        return null;
    }

    @Override
    protected boolean isCorrectAction(Action action) {
        return action == Action.RIGHT_CLICK_BLOCK;
    }

    @Override
    protected boolean isCorrectBlockType(Block block) {
        if (block == null)
            return false;

        switch (block.getType()) {
            case ACACIA_BUTTON:
            case BIRCH_BUTTON:
            case CRIMSON_BUTTON:
            case DARK_OAK_BUTTON:
            case JUNGLE_BUTTON:
            case OAK_BUTTON:
            case POLISHED_BLACKSTONE_BUTTON:
            case SPRUCE_BUTTON:
            case STONE_BUTTON:
            case WARPED_BUTTON:
                return true;
            default:
                return false;
        }
    }
}
