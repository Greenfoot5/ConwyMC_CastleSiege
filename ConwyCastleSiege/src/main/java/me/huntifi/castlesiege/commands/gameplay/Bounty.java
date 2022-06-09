package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Bounty implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 2) {
            return false;
        }

        Player bountied = Bukkit.getPlayer(args[0]);
        int amount = Integer.parseInt(args[1]);
        if (bountied == null) {
            Messenger.sendError("That's not a valid player!", sender);
            return true;
        } else if (amount < 100) {
            Messenger.sendError("Bounties must be at least 100!", sender);
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            int totalBounty = ActiveData.getData(bountied.getUniqueId()).getAndAddBounty(amount);
            Messenger.broadcastPaidBounty(ChatColor.DARK_AQUA + "CONSOLE",
                    NameTag.color(bountied) + bountied.getName(), amount, totalBounty);
            return true;
        } else if (!(sender instanceof Player)) {
            return true;
        }

        Player payee = (Player) sender;
        if (!ActiveData.getData(payee.getUniqueId()).takeCoins(amount)) {
            Messenger.sendError("You don't have enough coins to do that!", sender);
        }
        int totalBounty = ActiveData.getData(bountied.getUniqueId()).getAndAddBounty(amount);
        Messenger.broadcastPaidBounty(NameTag.color(payee) + payee.getName(),
                NameTag.color(bountied) + bountied.getName(), amount, totalBounty);
        return true;
    }
}
