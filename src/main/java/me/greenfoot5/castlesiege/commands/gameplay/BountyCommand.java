package me.greenfoot5.castlesiege.commands.gameplay;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.advancements.levels.LevelAdvancements;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
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

/**
 * When a player runs the bounty command, either using or viewing the top bounties
 */
public class BountyCommand implements CommandExecutor {

    private static final ArrayList<Tuple<UUID, Integer>> bounties = new ArrayList<>();

    private static final int MIN_BOUNTY = 25;
    private static final int MIN_CLAIM = 100;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equals("Bounties")) {
            bounties(sender, args);
            return true;
        }

        // Check args count
        if (args.length != 2 && args.length != 1) {
            return false;
        }

        // Get player
        Player bountied = Bukkit.getPlayer(args[0]);
        if (bountied == null) {
            Messenger.sendError("That's not a valid player!", sender);
            return true;
        }
        CSPlayerData bountiedData = CSActiveData.getData(bountied.getUniqueId());
        if (bountiedData != null && bountiedData.getLevel() >= MIN_BOUNTY) {
            Messenger.sendError(bountied.getName() + " must be at least <green>level " + LevelAdvancements.BOUNTY_LEVEL + "</green> to gain a player-set bounty.", sender);
        }


        if (args.length == 1) {
            int amount = getBounty(bountied.getUniqueId());
            Messenger.sendBounty(CSNameTag.mmUsername(bountied) + " has a bounty of <gold>" + amount + "</gold> coins on their head.", sender);
            return true;
        }

        if (sender instanceof Player p) {
            CSPlayerData data = CSActiveData.getData(p.getUniqueId());
            if (data != null) {
                int level = data.getLevel();
                if (level < LevelAdvancements.BOUNTY_LEVEL) {
                    Messenger.sendError("You need to be <green>level " + LevelAdvancements.BOUNTY_LEVEL + "</green> to add bounties to players.", sender);
                    return true;
                }
            }
        }

        int amount = Integer.parseInt(args[1]);
        if (amount < MIN_BOUNTY) {
            Messenger.sendError("Bounties must be at least " + MIN_BOUNTY + "!", sender);
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            int totalBounty = getAndAddBounty(bountied.getUniqueId(), amount);
            Messenger.broadcastPaidBounty("<dark_aqua>CONSOLE</dark_aqua>",
                    CSNameTag.mmUsername(bountied), amount, totalBounty);
            return true;
        } else if (!(sender instanceof Player)) {
            return true;
        }

        Player payee = (Player) sender;

        if (!CSActiveData.getData(payee.getUniqueId()).takeCoins(amount)) {
            Messenger.sendError("You don't have enough coins to do that!", sender);
            return true;
        }

        int totalBounty = getAndAddBounty(bountied.getUniqueId(), amount);
        Messenger.broadcastPaidBounty(CSNameTag.mmUsername(payee),
                CSNameTag.mmUsername(bountied), amount, totalBounty);
        return true;
    }

    private void bounties(@NotNull CommandSender sender, @NotNull String[] args) {
        int requested;
        try {
            requested = args.length == 0 ? 0 : Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Messenger.sendError("Use /bounty to get the bounty for a player!", sender);
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                bounties.sort((o1, o2) -> o2.getSecond() - o1.getSecond());

                // Send header
                Messenger.send("<aqua>#. Player</aqua> <gold>Bounty</gold>", sender);

                // Send Entries
                int pos = requested < 6 ? 0 : Math.min(requested - 5, bounties.size());
                DecimalFormat num = new DecimalFormat("0");
                while (pos < bounties.size()) {
                    String color = pos == requested ? "<aqua>" : "<dark_aqua>";
                    Tuple<UUID, Integer> bounty = bounties.get(pos);
                    Messenger.send("<gray>" + num.format(pos + 1) + ". " +
                            color + Bukkit.getOfflinePlayer(bounty.getFirst()).getName() + " <gold>"
                            + bounty.getSecond(), sender);
                    pos++;
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Grants the bounty to their killer
     * @param bountied The player who had the bounty
     * @param killer The player who killed the bountied player
     */
    public static void grantRewards(Player bountied, Player killer) {
        int bounty = getBountyAndClear(bountied.getUniqueId());
        if (bounty <= 0) {
            return;
        }

        CSActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty);

        if (bounty >= MIN_CLAIM) {
            Messenger.broadcastBountyClaimed(CSNameTag.mmUsername(bountied),
                    CSNameTag.mmUsername(killer), bounty);
        } else {
            Messenger.sendBounty("You killed " + CSNameTag.mmUsername(bountied) + " and claimed <gold>"
                    + bounty + "</gold> coins!", killer);
        }
    }

    /**
     * Splits a player's bounty between the killer &amp; assistant
     * @param bountied The player who had a bounty
     * @param killer The player who dealt the final blow
     * @param assist The player who dealt the most damage
     */
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
       CSActiveData.getData(assist.getUniqueId()).addCoinsClean(assistAmount);
       CSActiveData.getData(killer.getUniqueId()).addCoinsClean(bounty - assistAmount);

        if (bounty >= MIN_CLAIM) {
            Messenger.broadcastBountyClaimed(CSNameTag.mmUsername(bountied),
                    CSNameTag.mmUsername(killer), CSNameTag.mmUsername(assist), bounty);
        } else {
            Messenger.sendBounty("You killed " + bountied.getName() + " and claimed <gold>"
                    + (bounty - assistAmount) + "</gold> coins!", killer);
            Messenger.sendBounty("You assisted in killing " + bountied.getName() + " and claimed <gold>"
                    + assistAmount + "</gold> coins!", assist);
        }
    }

    /**
     * Checks a player's kill streak and adds a bounty
     * @param killer The player whose kill streak needs checking
     */
    public static void killStreak(Player killer) {
        int amount = switch (CSActiveData.getData(killer.getUniqueId()).getKillStreak()) {
            case 5 -> 20;
            case 10 -> 40;
            case 15 -> 60;
            case 20 -> 80;
            case 35 -> 140;
            default -> 0;
        };
        if (amount > 0) {
            int total = getAndAddBounty(killer.getUniqueId(), (int) (amount * CSPlayerData.getCoinMultiplier()));
            Messenger.broadcastKillStreakBounty(CSNameTag.mmUsername(killer),
                    CSActiveData.getData(killer.getUniqueId()).getKillStreak(), total);
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

    /**
     * Loads all bounties from the database
     */
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

    /**
     * Save all bounties to the database
     */
    public static void saveBounties() {
        for (UUID uuid : CSActiveData.getPlayers()) {
            Tuple<UUID, Integer> bounty = new Tuple<>(uuid, getBounty(uuid));
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
