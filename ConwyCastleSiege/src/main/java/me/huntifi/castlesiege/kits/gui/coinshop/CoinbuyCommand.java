package me.huntifi.castlesiege.kits.gui.coinshop;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.KitList;
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
        }

        Player p = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        String kitname = args[2];
        String type = args[1];
        double coinPrice;

        if (target == null) {
            Messenger.sendError("Target not found!", sender);
            return true;
        }

        //This is the kit, the kit should be in the kits list in order for it to be an existing one.
        if (!KitList.getAllKits().contains(args[2])) {
            Messenger.sendError("An invalid kit was provided.", sender);
            return true;
        }

        if (type.equalsIgnoreCase("donator") || type.equalsIgnoreCase("premium")) {
            coinPrice = DonatorKit.getPremiumPrice(args[2]);
            if (coinPrice == 0.0) {
                Messenger.sendError("The coinprice is 0, report this!", sender);
                return true;
            }

            if (!ActiveData.getData(p.getUniqueId()).takeCoins(coinPrice)) {
                Messenger.sendError("You don't have enough coins to buy this kit!", sender);
                return true;
            }

            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "Unlockkit " + target.getName() + " add " + kitname + " 30d " + "false";
            Bukkit.dispatchCommand(console, command);
            if (p == target) {
                Messenger.sendInfo("You have bought " + kitname + " for 30 days!", p);
            } else {
                Messenger.sendInfo(p.getName() + " has bought you " + kitname + " for 30 days!", target);
            }

        } else if (type.equalsIgnoreCase("map") || type.equalsIgnoreCase("team")) {
            coinPrice = DonatorKit.getTeamkitPrice(args[2]);
            if (coinPrice == 0.0) {
                Messenger.sendError("The coinprice is 0, report this!", sender);
                return true;
            }

            if (!ActiveData.getData(p.getUniqueId()).takeCoins(coinPrice)) {
                Messenger.sendError("You don't have enough coins to buy this kit!", sender);
                return true;
            }

            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "Unlockkit " + target.getName() + " add " + kitname + " 10y " + "false";
            Bukkit.dispatchCommand(console, command);
            if (p == target) {
                Messenger.sendInfo("You have bought " + kitname + " for ever!", p);
            } else {
                Messenger.sendInfo(p.getName() + " has bought you " + kitname + " for ever!", target);
            }

        }

        return true;
    }
}
