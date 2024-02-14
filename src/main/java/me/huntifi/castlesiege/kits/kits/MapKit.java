package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.Gamemode;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class MapKit extends Kit implements Listener {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    private final String map;

    /**
     * Create a map specific kit, which are kits that can only be played on a specific map but are not bound by teams.
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     */
    public MapKit(String name, int baseHealth, double regenAmount, Material material, String mapName) {
        super(name, baseHealth, regenAmount, material, ChatColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        map = mapName;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom If the kit is selected by the random command
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, verbose, isRandom))
            return false;

        boolean canPlay = MapController.getCurrentMap().worldName.equalsIgnoreCase(map);
        if (!canPlay) {
            if (verbose) {
                Messenger.sendError(String.format("You can't use %s on this map!", name), sender);
            }
            return false;
        }

        return true;
    }

    /**
     * Get all level kit names
     * @return All level kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public ArrayList<String> getGuiCostText() {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add(color + "§lCan be played on " + map + "!");
        return text;
    }

    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (!InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                Objects.requireNonNull(e.getClickedBlock()).getState() instanceof Sign) {
            Sign sign = (Sign) e.getClickedBlock().getState();
            if (sign.getSide(Side.FRONT).getLine(0).contains("Map Kit") && sign.getSide(Side.FRONT).getLine(2).contains(name)) {
                e.getPlayer().performCommand(name.toLowerCase());
            }
        }
    }
}
