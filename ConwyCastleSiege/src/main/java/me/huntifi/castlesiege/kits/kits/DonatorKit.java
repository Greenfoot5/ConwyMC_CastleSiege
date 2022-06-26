package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class DonatorKit extends Kit {

    // Coin price to unlock this kit
    private final double price;

    // Kit Tracking
    private static final Collection<String> kits = new ArrayList<>();

    public DonatorKit(String name, int baseHealth, double regenAmount, double coins) {
        super(name, baseHealth, regenAmount);
        price = coins;

        kits.add(name.replaceAll(" ", ""));
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
        if (sender instanceof ConsoleCommandSender) {
            Messenger.sendError("Console cannot select kits!", sender);
            return true;

        } else if (sender instanceof Player) {
            if (MapController.isSpectator(((Player) sender).getUniqueId())) {
                Messenger.sendError("Spectators cannot select kits!", sender);
                return true;
            }

            Player player = (Player) sender;

            new BukkitRunnable() {
                @Override
                public void run() {

                    boolean hasKit = LoadData.hasKit(((Player) sender).getUniqueId(), name.replace(" ", ""));

                    if (!hasKit && !isFriday()) {
                        Messenger.sendError("You don't own this kit!", sender);
                        return;
                    }
                    add(player);
                }
            }.runTaskAsynchronously(Main.plugin);
            return true;
        }
        return false;
    }

    /**
     * Set the player's kit synchronously after asynchronous database operation
     * @param p The player
     */
    public void add(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                addPlayer(p.getUniqueId());
            }
        }.runTask(Main.plugin);
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

    public static double getPremiumPrice(String kitname) {
    double coinPrice;
        switch (kitname) {

            case "Cavalry":
            case "Engineer":
            case "Warhound":
            case "Crossbowman":
            case "Halberdier":
                coinPrice = 7500;
                break;
            case "Medic":
            case "Executioner":
            case "Berserker":
            case "Ranger":
                coinPrice = 5000;
                break;
            case "Viking":
            case "Maceman":
            case "Vanguard":
                coinPrice = 6000;
                break;
            default:
                coinPrice = 5500;
                break;
        }
        return coinPrice;
    }

    public static double getTeamkitPrice(String kitname) {
        double coinPrice;
        switch (kitname) {

            case "Elytrier":
            case "Abyssal":
            case "Lancer":
                coinPrice = 5000;
                break;
            case "MoriaOrc":
            case "Hellsteed":
            case "UrukBerserker":
            case "Ranged_Cavalry":
                coinPrice = 2500;
                break;
            case "Fallen":
                coinPrice = 1000;
                break;
            default:
                coinPrice = 2000;
                break;
        }
        return coinPrice;
    }

    public static boolean isFriday() {
        System.out.println(System.currentTimeMillis());
        System.out.println((((System.currentTimeMillis() / 1000) - 86400) % 604800) / 86400);
        return ((System.currentTimeMillis() / 1000 - 86400) % 604800) / 86400 < 1;
    }
}
