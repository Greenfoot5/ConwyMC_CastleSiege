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
        if (args.length != 2 && args.length != 1) {
            return false;
        }

        Player bountied = Bukkit.getPlayer(args[0]);
        if (bountied == null) {
            Messenger.sendError("That's not a valid player!", sender);
            return true;
        }

        if (args.length == 1) {
            int amount = ActiveData.getData(bountied.getUniqueId()).getBounty();
            sender.sendMessage(ChatColor.GOLD + "[B] " + ChatColor.YELLOW + NameTag.color(bountied) + bountied.getName()
                    + ChatColor.YELLOW + " has a bounty of "
                    + ChatColor.GOLD + amount + ChatColor.YELLOW + " coins on their head.");
            return true;
        }

        int amount = Integer.parseInt(args[1]);
        if (amount < 100) {
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

    public static void grantRewards(Player bountied, Player killer) {
        int bounty = ActiveData.getData(bountied.getUniqueId()).getBountyAndClear();
        if (bounty <= 0) {
            return;
        }

        ActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty);
        Messenger.broadcastBountyClaimed(NameTag.color(bountied) + bountied.getName(),
                NameTag.color(killer) + killer.getName(), bounty);
    }

    public static void grantRewards(Player bountied, Player killer, Player assist) {
        if (assist == null) {
            grantRewards(bountied, killer);
            return;
        }

        int bounty = ActiveData.getData(bountied.getUniqueId()).getBountyAndClear();
        if (bounty <= 0) {
            return;
        }

        int assistAmount = bounty / 3;
        ActiveData.getData(assist.getUniqueId()).addCoinsClean(assistAmount);
        ActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty = assistAmount);

        Messenger.broadcastBountyClaimed(NameTag.color(bountied) + bountied.getName(),
                NameTag.color(killer) + killer.getName(), NameTag.color(assist) + assist.getName(), bounty);
    }
}
