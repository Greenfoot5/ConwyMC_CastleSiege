package me.greenfoot5.castlesiege.kits.kits;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.LoadData;
import me.greenfoot5.castlesiege.gui.BuyKitGui;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A kit that can be purchased with coins
 */
public abstract class CoinKit extends Kit {

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();
    public static final List<String> boostedKits = new ArrayList<>();
    public static final List<String> donatorKits = new ArrayList<>();

    /**
     * @param name The name of the kit
     * @param baseHealth The baseHealth of the kit
     * @param regenAmount The regenAmount of the kit
     * @param material The material to use in GUIs to represent the kit
     */
    public CoinKit(String name, int baseHealth, double regenAmount, Material material) {
        super(name, baseHealth, regenAmount, material, NamedTextColor.YELLOW);

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
        boolean hasKit = CSActiveData.getData(uuid).hasKit(getSpacelessName());
        if (!hasKit && !isFree()) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                } else {
                    new BuyKitGui(this, (int) getPrice(uuid), (Player) sender);
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Get this kit's price
     * @param uuid The uuid of the player to get the price for
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
    public ArrayList<Component> getGuiCostText() {
        ArrayList<Component> text = new ArrayList<>();
        text.add(Component.empty());
        text.add(Component.text("Unlocked with coins", color).decorate(TextDecoration.BOLD));
        return text;
    }
}
