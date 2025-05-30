package me.greenfoot5.castlesiege.commands.staff.boosters;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.Booster;
import me.greenfoot5.castlesiege.data_types.CSPlayerData;
import me.greenfoot5.castlesiege.data_types.CoinBooster;
import me.greenfoot5.castlesiege.data_types.KitBooster;
import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.database.LoadData;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.kits.kits.StaffKit;
import me.greenfoot5.conwymc.util.Messenger;
import me.greenfoot5.conwymc.util.PunishmentTime;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Create a new booster and grant it to a player
 */
public class GrantBoosterCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> grantBooster(sender, args, true));
        return true;
    }

    public static void grantBooster(@NotNull CommandSender sender, @NotNull String[] args, boolean verbose) {
        Booster booster = createBooster(sender, args, verbose);
        if (booster == null) {
            return;
        }

        UUID uuid = getUUID(args[0]);
        assert uuid != null;
        CSPlayerData data = CSActiveData.getData(uuid);
        if (data != null) {
            if (!giveBooster(data, uuid, booster)) {
                if (verbose)
                    Messenger.sendError("Failed to add booster to database. Try again?", sender);
                return;
            }
        } else if (!giveBooster(uuid, booster)) {
            if (verbose)
                Messenger.sendError("Failed to add booster to offline player. Try again?", sender);
            return;
        }

        if (verbose)
            sendConfirmMessage(sender);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return null;
        }
        if (args.length == 2) {
            return List.of("coin", "kit");
        }
        if (args.length == 3) {
            if (Objects.equals(args[2], "kit"))
                return new ArrayList<>(Kit.getKits());
            else
                return List.of("<duration>");
        }
        return null;
    }

    private static Booster createBooster(CommandSender sender, String[] args, boolean verbose) {
        if (args.length < 3) {
            if (verbose)
                Messenger.sendError("Not enough args! Correct usage is: /grantbooster <player> <type> <duration> [type-specific args]", sender);
            return null;
        }
        String type = args[1];
        int duration;
        try {
            duration = (int) PunishmentTime.getDuration(args[2]) / 1000;
        } catch (NumberFormatException ex) {
            if (verbose)
                Messenger.sendError("Duration not a valid number! It should be an amount of seconds", sender);
            return null;
        }
        double multiplier;
        switch (type.toUpperCase()) {
            case "COIN":
            case "COINS":
            case "C":
                if (args.length != 4) {
                    if (verbose)
                        Messenger.sendError("Incorrect args! Correct usage is: /grantbooster <player> coin <duration> <multiplier>", sender);
                    return null;
                }

                try {
                    multiplier = Double.parseDouble(args[3]);
                } catch (NumberFormatException ex) {
                    if (verbose)
                        Messenger.sendError("Multiplier not a valid number! It should be a double!", sender);
                    return null;
                }
                return new CoinBooster(duration, multiplier);
            case "KIT":
            case "K":
                if (args.length != 4) {
                    if (verbose)
                        Messenger.sendError("Incorrect args! Correct usage is: /grantbooster <player> kit <duration> <kit_name>", sender);
                    return null;
                }

                String kitName = args[3];
                if (CoinKit.getKit(kitName) == null
                        && StaffKit.getKit(kitName) == null
                        && !kitName.equalsIgnoreCase("WILD")
                        && !kitName.equalsIgnoreCase("RANDOM")) {
                    if (verbose)
                        Messenger.sendError("Invalid kit name, nor \"wild\" nor \"random\"!", sender);
                    return null;
                }
                return new KitBooster(duration, kitName);
            default:
                if (verbose)
                    Messenger.sendError("Invalid type!", sender);
                return null;
        }
    }

    /**
     * Get the player's UUID directly from the online players or from the database.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    private static UUID getUUID(String playerName) {
        // Player is online
        Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            return player.getUniqueId();

        // Player is offline or doesn't exist in our database
        try {
            return LoadData.getUUID(playerName);
        } catch (SQLException e) {
            return null;
        }
    }

    private static boolean giveBooster(CSPlayerData data, UUID uuid, Booster booster) {
        if (updateDatabase(uuid, booster)) {
            data.addBooster(booster);
            return true;
        }
        return false;
    }

    private static boolean giveBooster(UUID uuid, Booster booster) {
        return updateDatabase(uuid, booster);
    }

    /**
     * Update the player's currency in the database.
     * @param uuid The UUID of the player
     * @param booster The booster to set
     * @return if the booster was added successfully
     */
    public static boolean updateDatabase(UUID uuid, Booster booster) {
        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(getQuery())) {
            ps.setString(1, uuid.toString());
            ps.setString(2, booster.getBoostType());
            ps.setInt(3, booster.duration);
            ps.setString(4, booster.getValue());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getQuery() {
        return "INSERT INTO player_boosters (uuid, booster_type, duration, boost_value)\n" +
                "VALUES (?, ?, ?, ?);";
    }

    private static void sendConfirmMessage(CommandSender sender) {
        Messenger.sendSuccess("Booster granted", sender);
    }
}
