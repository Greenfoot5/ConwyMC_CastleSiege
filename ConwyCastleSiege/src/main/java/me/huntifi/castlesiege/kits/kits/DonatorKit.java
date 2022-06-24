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

public abstract class DonatorKit extends Kit {

    protected static double price;


    public DonatorKit(String name, int baseHealth, double regenAmount, double coins) {
        super(name, baseHealth, regenAmount);
        price = coins;
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

                    if (!hasKit) {
                        Messenger.sendError("You don't own this kit!", sender);
                        return;
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            addPlayer(player.getUniqueId());
                        }
                    }.runTask(Main.plugin);
                }
            }.runTaskAsynchronously(Main.plugin);
            return true;
        }
        return false;
    }


    public static double getPremiumPrice(String kitname) {
    double coinprice = 0.0;
        switch (kitname) {

            case "Cavalry":
            case "Engineer":
            case "Warhound":
            case "Crossbowman":
            case "Halberdier":
                coinprice = 7500;
                break;
            case "Medic":
            case "Executioner":
            case "Berserker":
            case "Ranger":
                coinprice = 5000;
                break;
            case "Viking":
            case "Maceman":
            case "Vanguard":
                coinprice = 6000;
                break;
            default:
                coinprice = 5500;
                break;
        }
        return coinprice;
    }

    public static double getTeamkitPrice(String kitname) {
        double coinprice = 0.0;
        switch (kitname) {

            case "Elytrier":
            case "Abyssal":
            case "Lancer":
                coinprice = 5000;
                break;
            case "MoriaOrc":
            case "Hellsteed":
            case "UrukBerserker":
            case "Ranged_Cavalry":
                coinprice = 2500;
                break;
            case "Fallen":
                coinprice = 1000;
                break;
            default:
                coinprice = 2000;
                break;
        }
        return coinprice;
    }
}
