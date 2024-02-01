package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
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

public abstract class CoinKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    public static final List<String> boostedKits = new ArrayList<>();
    public static final List<String> donatorKits = new ArrayList<>();

    public CoinKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, ChatColor.YELLOW);

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
        if (!donatorKits.contains(getSpacelessName()))
            donatorKits.add(getSpacelessName());
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit Whether to apply the kit limit in the check
     * @param verbose Whether error messages should be sent
     * @param isRandom Whether the check is done by the random command
     * @return Whether the player can select this kit
     */
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose, boolean isRandom) {
        if (!super.canSelect(sender, applyLimit, verbose, isRandom))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        boolean hasKit = ActiveData.getData(uuid).hasKit(getSpacelessName());
        if (!hasKit && !isFree()) {
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
     * Get this kit's price
     * @return The price to unlock this kit
     */
    public static double getPrice(UUID uuid) {
        return LoadData.returnPremiumKitPrice(uuid);
    }

    /**
     * Get all donator kit names
     * @return All donator kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    /**
     * Checks if the kit is available for the player to use
     * @return If the kit is free to use
     */
    public static boolean isFree() {
        return ((System.currentTimeMillis() / 1000 - 86400) % 604800) / 86400 < 1 || MapController.allKitsFree;
    }

    /**
     * @return Displays the cost for the footer of a kit gui's lore
     */
    public ArrayList<String> getGuiCostText() {
        ArrayList<String> text = new ArrayList<>();
        text.add(" ");
        text.add(color + "§lUnlocked with coins");
        return text;
    }
}
