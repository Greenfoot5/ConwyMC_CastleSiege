package me.huntifi.castlesiege.kits.gui.coinshop;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoinbuyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (args.length <= 2) {
            Messenger.sendError("Use: /buykit <playername> <type> <kitname>", sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            Messenger.sendError("Not a console command.", sender);
            return true;
        }

        // Assign the command parameters to variables
        Player p = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        String type = args[1];
        String kitName = args[2];

        // Player must be online
        if (target == null) {
            Messenger.sendError("Target not found!", sender);
            return true;
        }

        // Only donator/premium and map/team types are allowed
        String time;
        String timeMessage;
        switch (type.toLowerCase()) {
            case "donator":
            case "premium":
                time = "30d";
                timeMessage = "for 30 days";
                break;
            case "map":
            case "team":
                time = "10y";
                timeMessage = "permanently";
                break;
            default:
                Messenger.sendError("Type must be donator/premium or map/team!", sender);
                return true;
        }

        // Only donator kits can be unlocked
        if (!DonatorKit.getKits().contains(kitName)) {
            Messenger.sendError("An invalid kit was provided.", sender);
            return true;
        }

        // Get the kit's price
        Kit kit = Kit.getKit(kitName);
        double coinPrice = ((DonatorKit) kit).getPrice();
        if (coinPrice <= 0) {
            Messenger.sendError("The coinprice is " + coinPrice + ", report this!", sender);
            return true;
        }

        // Charge the player for buying the kit
        if (!ActiveData.getData(p.getUniqueId()).takeCoins(coinPrice)) {
            Messenger.sendError("You don't have enough coins to buy this kit!", sender);
            return true;
        }

        // Give the kit to the player
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String command = String.format("UnlockKit %s add %s %s false", target.getName(), kitName, time);
        Bukkit.dispatchCommand(console, command);

        // Inform the player about their unlocked kit
        if (p == target) {
            Messenger.sendInfo(String.format("You have bought %s %s!", kitName, timeMessage), p);
        } else {
            Messenger.sendInfo(String.format("%s has bought you %s %s!", p.getName(), kitName, timeMessage), p);
        }

        return true;
    }
}
