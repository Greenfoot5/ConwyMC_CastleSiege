package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class DonatorKit extends Kit {

    // Coin price to unlock this kit
    private final double price;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    public DonatorKit(String name, int baseHealth, double regenAmount, double coins) {
        super(name, baseHealth, regenAmount);
        price = coins;

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
    }

    /**
     * Check if the player can select this kit
     * @param sender Source of the command
     * @param applyLimit Whether to apply the kit limit in the check
     * @param verbose Whether error messages should be sent
     * @return Whether the player can select this kit
     */
    @Override
    public boolean canSelect(CommandSender sender, boolean applyLimit, boolean verbose) {
        if (!super.canSelect(sender, applyLimit, verbose))
            return false;

        UUID uuid = ((Player) sender).getUniqueId();
        boolean hasKit = ActiveData.getData(uuid).hasKit(getSpacelessName());
        if (!hasKit && !isFriday()) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null)
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                else
                    Messenger.sendError(String.format("You don't own %s!", name), sender);
            }
            return false;
        }

        return true;
    }

    /**
     * Get this kit's price
     * @return The price to unlock this kit
     */
    public double getPrice() {
        return price;
    }

    /**
     * Get all donator kit names
     * @return All donator kit names without spaces
     */
    public static Collection<String> getKits() {
        return kits;
    }

    public static boolean isFriday() {
        return ((System.currentTimeMillis() / 1000 - 86400) % 604800) / 86400 < 1;
    }
}
