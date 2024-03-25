package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
    private final String sign;

    /**
     * Create a map specific kit, which are kits that can only be played on a specific map but are not bound by teams.
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     */
    public MapKit(String name, int baseHealth, double regenAmount, Material material, String mapName, String signName) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        map = mapName;
        sign = signName;
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param verbose Whether error messages should be sent
     * @param isRandom If the kit is selected by the random command
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, applyLimit, verbose, isRandom))
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
     * Get all map kit names
     * @return All map kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Can be played on " + map + "!", color).decorate(TextDecoration.BOLD));
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
            Sign asign = (Sign) e.getClickedBlock().getState();
            String mapKit = PlainTextComponentSerializer.plainText().serialize(asign.getSide(Side.FRONT).line(0));
            String kitName = PlainTextComponentSerializer.plainText().serialize(asign.getSide(Side.FRONT).line(2));
            if (mapKit.contains("Map Kit") && kitName.contains(sign)) {
                e.getPlayer().performCommand(name.toLowerCase());
            }
        }
    }
}
