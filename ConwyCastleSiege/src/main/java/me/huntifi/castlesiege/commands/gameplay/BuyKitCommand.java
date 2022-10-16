package me.huntifi.castlesiege.commands.gameplay;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.kits.kits.TeamKit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyKitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Only players can buy kits
            if (!(sender instanceof Player)) {
                Messenger.sendError("Not a console command.", sender);
                return;
            }

            // Command must have kit parameter and may have player parameter
            if (args.length != 1 && args.length != 2) {
                Messenger.sendError("Use: /buykit <kit> [player]", sender);
                return;
            }

            // Get the kit
            String kitName = args[0];
            Kit kit = Kit.getKit(kitName);
            if (!(kit instanceof DonatorKit)) {
                Messenger.sendError(kitName + " is not a donator kit", sender);
                return;
            }

            // Get the kit's buyer and receiver
            Player buyer = (Player) sender;
            Player receiver;
            if (args.length == 2) {
                receiver = Bukkit.getPlayer(args[1]);
                if (receiver == null) {
                    Messenger.sendError("Could not find player: " + ChatColor.RED + args[1], sender);
                    return;
                }
            } else
                receiver = buyer;

            // Only unowned kits can be unlocked
            if (ActiveData.getData(receiver.getUniqueId()).hasKit(kitName)) {
                Messenger.sendError("This kit is already unlocked!", sender);
                return ;
            }

            // Get the kit's price
            double coinPrice = ((DonatorKit) kit).getPrice();
            if (coinPrice <= 0) {
                Messenger.sendError("The coinPrice is " + coinPrice + ", report this!", sender);
                return;
            }

            // Charge the player for buying the kit
            if (!ActiveData.getData(buyer.getUniqueId()).takeCoins(coinPrice)) {
                Messenger.sendError("You don't have enough coins to buy this kit!", sender);
                return;
            }

            // Get the time to unlock the kit for
            String time;
            String timeMessage;
            time = "10y";
            timeMessage = "permanently";

            // Give the kit to the player
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = String.format("unlockKit %s add %s %s false", receiver.getName(), kitName, time);
            Bukkit.getScheduler().runTask(Main.plugin, () -> Bukkit.dispatchCommand(console, command));

            // Inform the player about their unlocked kit
            if (buyer == receiver)
                Messenger.sendInfo(String.format("You have bought %s %s!", kit.name, timeMessage), receiver);
            else
                Messenger.sendInfo(String.format("%s has bought you %s %s!", buyer.getName(), kit.name, timeMessage), receiver);
        });

        return true;
    }
}
