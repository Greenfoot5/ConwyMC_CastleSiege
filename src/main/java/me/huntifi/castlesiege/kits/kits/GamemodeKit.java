package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.Gamemode;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class GamemodeKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    private final Gamemode gamemode;

    /**
     * Create a gamemode specific kit, which are kits that can only be played on certain gamemodes.
     *
     * @param name        This kit's name
     * @param baseHealth  This kit's base health
     */
    public GamemodeKit(String name, int baseHealth, double regenAmount, Material material, Gamemode gm) {
        super(name, baseHealth, regenAmount, material, ChatColor.AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());

        gamemode = gm;
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

        boolean canPlay = MapController.getCurrentMap().gamemode.equals(gamemode);
        if (!canPlay) {
            if (verbose) {
                Messenger.sendError(String.format("You can't use %s! in this gamemode!", name), sender);
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
        text.add(color + "§lCan be played on " + gamemode.toString() + " maps.");
        return text;
    }
}
