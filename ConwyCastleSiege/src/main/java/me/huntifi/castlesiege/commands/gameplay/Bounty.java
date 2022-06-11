package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class Bounty implements CommandExecutor {

    private static final int MIN_BOUNTY = 25;
    private static final int MIN_CLAIM = 100;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equals("Bounties")) {
            return bounties(sender, args);
        }

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
        if (amount < MIN_BOUNTY) {
            Messenger.sendError("Bounties must be at least " + MIN_BOUNTY + "!", sender);
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
            return true;
        }
        int totalBounty = ActiveData.getData(bountied.getUniqueId()).getAndAddBounty(amount);
        Messenger.broadcastPaidBounty(NameTag.color(payee) + payee.getName(),
                NameTag.color(bountied) + bountied.getName(), amount, totalBounty);
        return true;
    }

    private boolean bounties(@NotNull CommandSender sender, @NotNull String[] args) {
        int requested;
        try {
            requested = args.length == 0 ? 0 : Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Messenger.sendError("Use /bounty to get the bounty for a player!", sender);
            return false;
        }
        int finalRequested = requested;
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    int offset = finalRequested < 6 ? 0 : Math.min(finalRequested - 5, 90);

                    Tuple<PreparedStatement, ResultSet> top = LoadData.getTop("bounty", offset);

                    // Send header
                    sender.sendMessage(ChatColor.AQUA + "#. Player " + ChatColor.GOLD + "Bounty");

                    // Send entries
                    DecimalFormat num = new DecimalFormat("0");
                    int pos = offset;
                    while (top.getSecond().next()) {
                        pos++;
                        ChatColor color = pos == finalRequested ? ChatColor.AQUA : ChatColor.DARK_AQUA;
                        sender.sendMessage(ChatColor.GRAY + num.format(pos) + ". " +
                                color + top.getSecond().getString("name") + " " +
                                ChatColor.GOLD + top.getSecond().getInt("bounty"));
                    }
                    top.getFirst().close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.DARK_RED +
                            "Something went wrong! Please contact an administrator if this issue persists.");
                }
            }
        }.runTaskAsynchronously(Main.plugin);
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
        ActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty - assistAmount);

        if (bounty >= MIN_CLAIM) {
            Messenger.broadcastBountyClaimed(NameTag.color(bountied) + bountied.getName(),
                    NameTag.color(killer) + killer.getName(), NameTag.color(assist) + assist.getName(), bounty);
        }
    }

    public static void killstreak(Player killer) {
        int amount = 0;
        switch (ActiveData.getData(killer.getUniqueId()).getKillStreak()) {
            case 5:
                amount = 10;
                break;
            case 10:
                amount = 20;
                break;
            case 15:
                amount = 30;
                break;
            case 20:
                amount = 40;
                break;
            case 35:
                amount = 70;
                break;
        }
        if (amount > 0) {
            int total = ActiveData.getData(killer.getUniqueId()).getAndAddBounty(amount);
            Messenger.broadcastKillstreakBounty(NameTag.color(killer) + killer.getName(),
                    ActiveData.getData(killer.getUniqueId()).getKillStreak(), total);
        }
    }
}
