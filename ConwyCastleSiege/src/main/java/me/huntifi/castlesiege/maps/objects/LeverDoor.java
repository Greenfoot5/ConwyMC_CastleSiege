package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.data_types.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class LeverDoor extends Door {
    private final Location leverPosition;

    /**
     * Creates a new lever door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param schematics The names of the two schematics
     * @param sounds The sounds to play when closing/opening the door
     * @param timer How long the door stays open before automatically closing in ticks
     * @param leverPosition The position of the lever
     */
    public LeverDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds,
                     int timer, Location leverPosition) {
        super(flagName, centre, schematics, sounds, timer);
        this.leverPosition = leverPosition;
    }

    @Override
    protected boolean isCorrectInteraction(PlayerInteractEvent event) {
        if (!super.isCorrectInteraction(event))
            return false;

        assert event.getClickedBlock() != null;
        return Objects.equals(event.getClickedBlock().getWorld(), leverPosition.getWorld())
                && event.getClickedBlock().getLocation().distance(leverPosition) == 0;
    }

    @Override
    protected void activate(PlayerInteractEvent event) {
        if (openCounts.get() == 0) {
            super.activate(event);
        } else {
            openCounts.set(0);
            close();
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
    protected boolean isCorrectAction(Action action) {
        return action == Action.RIGHT_CLICK_BLOCK;
    }

    @Override
    protected boolean isCorrectBlockType(Block block) {
        return block != null && block.getType() == Material.LEVER;
    }
}
