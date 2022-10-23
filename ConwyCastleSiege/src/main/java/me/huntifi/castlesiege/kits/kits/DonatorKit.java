package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class DonatorKit extends Kit {

    // Coin price to unlock this kit
    private final double price;

    private final double bp_price;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    public DonatorKit(String name, int baseHealth, double regenAmount, double coins, double battlepoints) {
        super(name, baseHealth, regenAmount);
        price = coins;
        bp_price = battlepoints;

        if (!kits.contains(getSpacelessName()))
            kits.add(getSpacelessName());
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
        boolean hasBP = hasEnoughBP(uuid, bp_price);
        if (!hasKit && !isFriday() && !allKitsFree) {
            if (verbose) {
                if (Kit.equippedKits.get(uuid) == null) {
                    Messenger.sendError(String.format("You no longer have access to %s!", name), sender);
                } else {
                    Messenger.sendError(String.format("You don't own %s!", name), sender);
                }
            }
            return false;
        } else if (!hasBP && hasKit) {
            if (verbose) {
                Player p = Bukkit.getPlayer(uuid);
                Messenger.sendError("You do not have sufficient battlepoints (BP) to play this!", p);
                Messenger.sendError("Perform /battlepoints or /bp to see your battlepoints.", p);
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param uuid The player's uuid of which battlepoints should be checked in order to play a kit
     * @param battlep the amount of battlepoints needed to play this kit
     * @return true or false in case they don't have sufficient bp
     */
    public static boolean hasEnoughBP(UUID uuid, double battlep) {
        PlayerData data = ActiveData.getData(uuid);
            if (data.getBattlepoints() >= battlep) {
                data.addBattlepoints(-battlep);
                return true;
        }
        return false;
    }

    /**
     * Get this kit's battlepoint price
     * @return The price to use this kit once
     */
    public double getBattlepointPrice() {
        return bp_price;
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
