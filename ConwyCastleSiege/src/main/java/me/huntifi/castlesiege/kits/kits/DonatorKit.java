package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class DonatorKit extends Kit {

    // Coin price to unlock this kit
    private final double price;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    public DonatorKit(String name, int baseHealth, double regenAmount, double coins) {
        super(name, baseHealth, regenAmount);
        price = coins;

        kits.add(getSpacelessName());
    }

    /**
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Console cannot select kits!", sender);
            return true;
        }

        UUID uuid = ((Player) sender).getUniqueId();
        if (MapController.isSpectator(uuid)) {
            Messenger.sendError("Spectators cannot select kits!", sender);
            return true;
        }

        boolean hasKit = ActiveData.getData(uuid).hasKit(getSpacelessName());
        if (!hasKit && !isFriday()) {
            Messenger.sendError("You don't own this kit!", sender);
            if (Kit.equippedKits.get(uuid) == null)
                Kit.getKit("Swordsman").addPlayer(uuid);
        } else
            addPlayer(uuid);

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
