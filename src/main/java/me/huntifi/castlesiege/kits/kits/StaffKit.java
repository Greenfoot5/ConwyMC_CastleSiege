package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class StaffKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    public static final List<String> donatorKits = new ArrayList<>();

    public StaffKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, ChatColor.DARK_AQUA);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        if (!donatorKits.contains(getSpacelessName()))
            donatorKits.add(getSpacelessName());
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

        UUID uuid = ((Player) sender).getUniqueId();
        boolean hasKit = ActiveData.getData(uuid).hasKit(getSpacelessName());
        boolean allKitsFree = MapController.allKitsFree;
        if (!hasKit && !allKitsFree) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                } else {
                    Messenger.sendError(String.format("You don't own %s!", name), sender);
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Get all staff kit names
     * @return All staff kit names without spaces
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
        text.add(color + "§lUnlocked by becoming staff");
        return text;
    }
}
