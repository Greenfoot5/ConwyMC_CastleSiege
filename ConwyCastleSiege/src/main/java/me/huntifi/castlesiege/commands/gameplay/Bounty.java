package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Bounty implements CommandExecutor {

    private static final ArrayList<Tuple<UUID, Integer>> bounties = new ArrayList<>();

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
            int amount = getBounty(bountied.getUniqueId());
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
            int totalBounty = getAndAddBounty(bountied.getUniqueId(), amount);
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
        int totalBounty = getAndAddBounty(bountied.getUniqueId(), amount);
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
            return true;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                bounties.sort((o1, o2) -> o2.getSecond() - o1.getSecond());

                // Send header
                sender.sendMessage(ChatColor.AQUA + "#. Player " + ChatColor.GOLD + "Bounty");

                // Send Entries
                int pos = requested < 6 ? 0 : Math.min(requested - 5, bounties.size());
                DecimalFormat num = new DecimalFormat("0");
                System.out.println(pos);
                System.out.println(bounties.size());
                while (pos < bounties.size()) {
                    ChatColor color = pos == requested ? ChatColor.AQUA : ChatColor.DARK_AQUA;
                    Tuple<UUID, Integer> bounty = bounties.get(pos);
                    sender.sendMessage(ChatColor.GRAY + num.format(pos + 1) + ". " +
                            color + Bukkit.getOfflinePlayer(bounty.getFirst()).getName() + " " +
                            ChatColor.GOLD + bounty.getSecond());
                    pos++;
                }
            }
        }.runTaskAsynchronously(Main.plugin);
        return true;
    }

    public static void grantRewards(Player bountied, Player killer) {
        int bounty = getBountyAndClear(bountied.getUniqueId());
        if (bounty <= 0) {
            return;
        }

        ActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty);

        if (bounty >= MIN_CLAIM) {
            Messenger.broadcastBountyClaimed(NameTag.color(bountied) + bountied.getName(),
                    NameTag.color(killer) + killer.getName(), bounty);
        } else {
            Messenger.sendBounty("You killed " + bountied.getName() + " and claimed "
                    + ChatColor.GOLD + (bounty) + ChatColor.YELLOW + " coins!", killer);
        }
    }

    public static void grantRewards(Player bountied, Player killer, Player assist) {
        if (assist == null || assist.getName().equals(killer.getName())) {
            grantRewards(bountied, killer);
            return;
        }

        int bounty = getBountyAndClear(bountied.getUniqueId());
        if (bounty <= 0) {
            return;
        }

        int assistAmount = bounty / 3;
       ActiveData.getData(assist.getUniqueId()).addCoinsClean(assistAmount);
       ActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty - assistAmount);

        if (bounty >= MIN_CLAIM) {
            Messenger.broadcastBountyClaimed(NameTag.color(bountied) + bountied.getName(),
                    NameTag.color(killer) + killer.getName(), NameTag.color(assist) + assist.getName(), bounty);
        } else {
            Messenger.sendBounty("You killed " + bountied.getName() + " and claimed "
                    + ChatColor.GOLD + (bounty - assistAmount) + ChatColor.YELLOW + " coins!", killer);
            Messenger.sendBounty("You assisted in killing " + bountied.getName() + " and claimed "
                    + ChatColor.GOLD + (assistAmount) + ChatColor.YELLOW + " coins!", killer);
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
            int total = getAndAddBounty(killer.getUniqueId(), amount);
            Messenger.broadcastKillstreakBounty(NameTag.color(killer) + killer.getName(),
                    ActiveData.getData(killer.getUniqueId()).getKillStreak(), total);
        }
    }

    private static int getBounty(UUID uuid) {
        for (Tuple<UUID, Integer> bounty : bounties) {

            if (Objects.equals(bounty.getFirst().toString(), uuid.toString())) {
                return bounty.getSecond();
            }
        }
        return 0;
    }

    private static void addBounty(UUID uuid, int amount) {
        int current = getBounty(uuid);
        if (current == 0) {
            bounties.add(new Tuple<>(uuid, amount));
        } else {
            for (int i = 0; i < bounties.size(); i++) {
                if (Objects.equals(bounties.get(i).getFirst().toString(), uuid.toString())) {
                    bounties.set(i, new Tuple<>(uuid, current + amount));
                }
            }
        }
    }

    private static void removeBounty(UUID uuid) {
        for (int i = 0; i < bounties.size(); i++) {
            if (Objects.equals(bounties.get(i).getFirst().toString(), uuid.toString())) {
                bounties.remove(i);
                return;
            }
        }
    }

    private static int getAndAddBounty(UUID uuid, int amount) {
        addBounty(uuid, amount);
        return getBounty(uuid);
    }

    private static int getBountyAndClear(UUID uuid) {
        int amount = getBounty(uuid);
        removeBounty(uuid);
        return amount;
    }

    public static void loadBounties() {
        PreparedStatement ps;
        try {
            ps = Main.SQL.getConnection().prepareStatement(
                    "SELECT uuid, bounty FROM player_stats WHERE bounty > 0 ORDER BY bounty");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bounties.add(new Tuple<>(UUID.fromString(rs.getString("uuid")),
                        rs.getInt("bounty")));
            }
            ps.close();
        } catch (SQLException ignored) {}
    }

    public static void saveBounties(){
        for (Tuple<UUID, Integer> bounty : bounties) {
            PreparedStatement ps;
            try {
                ps = Main.SQL.getConnection().prepareStatement(
                        "UPDATE player_stats SET bounty = ? WHERE uuid = ?");

            ps.setInt(1, bounty.getSecond());
            ps.setString(2, bounty.getFirst().toString());
            ps.executeUpdate();
            ps.close();
            } catch (SQLException ignored) {}
        }
    }
}
