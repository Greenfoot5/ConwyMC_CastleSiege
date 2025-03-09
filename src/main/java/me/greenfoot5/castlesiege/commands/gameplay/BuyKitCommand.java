package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static me.greenfoot5.advancements.levels.LevelAdvancements.BUYKIT_LEVEL;

/**
 * Purchases a kit for a player
 */
public class BuyKitCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player && CSActiveData.getData(player.getUniqueId()) != null && CSActiveData.getData(player.getUniqueId()).getLevel() < BUYKIT_LEVEL) {
            Messenger.sendError("You must be at least <green>level " + BUYKIT_LEVEL + "</green> to buy kits!", sender);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
            // Only players can buy kits
            if (!(sender instanceof Player buyer)) {
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
            if (!(kit instanceof CoinKit) && !(kit instanceof SignKit)) {
                Messenger.sendError(kitName + " is not a donator or sign kit", sender);
                return;
            }

            // Get the kit's buyer and receiver
            Player receiver;
            if (args.length == 2) {
                receiver = Bukkit.getPlayer(args[1]);
                if (receiver == null) {
                    Messenger.sendError("Could not find player: <red>" + args[1], sender);
                    return;
                }
            } else {
                receiver = buyer;
            }

            // Only unowned kits can be unlocked
            if (CSActiveData.getData(receiver.getUniqueId()).hasKit(kitName)) {
                Messenger.sendError("This kit is already unlocked!", sender);
                return ;
            }

            // Get the kit's price
            double coinPrice;
            if (kit instanceof CoinKit) {
                coinPrice = CoinKit.getPrice(buyer.getUniqueId());
            } else {
                coinPrice = ((SignKit) kit).getCost();
            }

            if (coinPrice <= 0) {
                Messenger.sendError("The coinPrice is " + coinPrice + ", report this!", sender);
                return;
            }

            // Charge the player for buying the kit
            if (!CSActiveData.getData(buyer.getUniqueId()).takeCoins(coinPrice)) {
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Get the kit's buyer and receiver
        Player buyer = (Player) sender;
        Player receiver;
        if (args.length == 2) {
            receiver = Bukkit.getPlayer(args[1]);
            if (receiver == null) {
                Messenger.sendError("Could not find player: <red>" + args[1], sender);
                receiver = buyer;
            }
        } else {
            receiver = buyer;
        }
        final Player finalReceiver = receiver;

        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            Stream<String> values = new ArrayList<>(CoinKit.getKits()).stream();
            values = Stream.concat(values, new ArrayList<>(SignKit.getKits()).stream());
            values = values.filter(name -> !CSActiveData.getData(finalReceiver.getUniqueId()).hasKit(args[0]));
            StringUtil.copyPartialMatches(args[0], Arrays.asList(values.toArray(String[]::new)), options);
            return options;
        }

        return null;
    }
}
