package me.huntifi.castlesiege.maps.objects;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.TeamController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a button door
 */
public class ButtonDoor extends Door {

    /** Maps the button locations to their opening delay */
    private final Tuple<Location, Integer>[] buttonPositionsAndDelays;
    private final AtomicInteger openCounts = new AtomicInteger(0);

    /**
     * Creates a new button door
     * @param flagName The flag or map name the door is assigned to
     * @param centre The centre of the door (point for checking distance and playing the sound from)
     * @param schematics The blocks that make up the door
     * @param sounds The sounds to play when the door is closed/opened
     * @param timer How long the door stays open before automatically closing in ticks
     */
    public ButtonDoor(String flagName, Location centre, Tuple<String, String> schematics, Tuple<Sound, Sound> sounds,
                      int timer, Tuple<Location, Integer>[] buttonPositionsAndDelays) {
        super(flagName, centre, schematics, sounds, timer);
        this.buttonPositionsAndDelays = buttonPositionsAndDelays;
    }

    /**
     * Handle the opening and then closing of the door when a player presses one of the corresponding buttons.
     * @param event The event called when a player presses a button
     */
    @EventHandler
    public void onPress(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !isButton(event.getClickedBlock()))
            return;

        Block button = event.getClickedBlock();
        Integer buttonDelay = getButtonDelay(button);
        if (buttonDelay == null)
            return;

        Flag flag = MapController.getCurrentMap().getFlag(flagName);
        Player player = event.getPlayer();
        if (Objects.equals(flagName, MapController.getCurrentMap().name) ||
                Objects.equals(Objects.requireNonNull(flag).getCurrentOwners(), TeamController.getTeam(player.getUniqueId()).name)) {
            if (((Powerable) button.getBlockData()).isPowered())
                return;

            open();
            openCounts.getAndIncrement();

            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                if (openCounts.getAndDecrement() <= 1)
                    close();
            }, timer);
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this! You need to capture " + flagName + " first!"));
        }
    }

    /**
     * Check whether the clicked block is a button.
     * @param block The block
     * @return Whether the block is a button
     */
    private boolean isButton(Block block) {
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
}
