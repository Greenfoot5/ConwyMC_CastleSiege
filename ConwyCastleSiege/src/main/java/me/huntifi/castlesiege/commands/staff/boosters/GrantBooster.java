package me.huntifi.castlesiege.commands.staff.boosters;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.BattlepointBooster;
import me.huntifi.castlesiege.data_types.Booster;
import me.huntifi.castlesiege.data_types.CoinBooster;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class GrantBooster implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> grantBooster(sender, args));
        return true;
    }

    private void grantBooster(@NotNull CommandSender sender, @NotNull String[] args) {
        Booster booster = createBooster(sender, args);
        if (booster == null) {
            return;
        }

        UUID uuid = getUUID(args[1]);
        assert uuid != null;
        PlayerData data = ActiveData.getData(uuid);
        if (data != null) {
            if (!giveBooster(data, uuid, booster)) {
                Messenger.sendError("Failed to add booster to database. Try again?", sender);
                return;
            }
        } else if (!giveBooster(uuid, booster)) {
            Messenger.sendError("Failed to add booster to offline player. Try again?", sender);
            return;
        }

        sendConfirmMessage(sender);
    }

    private Booster createBooster(CommandSender sender, String[] args) {
        if (args.length < 3) {
            Messenger.sendError("Not enough args! Correct usage is: /grantbooster <player> <type> <duration> [type-specific args]", sender);
            return null;
        }
        String type = args[0];
        int duration;
        try {
            duration = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            Messenger.sendError("Duration not a valid number! It should be an amount of seconds", sender);
            return null;
        }
        switch (type.toUpperCase()) {
            case "COIN":
            case "COINS":
                double multiplier;
                try {
                    multiplier = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    Messenger.sendError("Multiplier not a valid number! It should be a double!", sender);
                    return null;
                }
                return new CoinBooster(duration, multiplier);
            case "BATTLEPOINT":
            case "BP":
                return new BattlepointBooster(duration);
            // TODO - Added Kit booster
            default:
                Messenger.sendError("Invalid type!", sender);
                return null;
        }
    }

    /**
     * Get the player's UUID directly from the online players or from the database.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    protected UUID getUUID(String playerName) {
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

    private boolean giveBooster(PlayerData data, UUID uuid, Booster booster) {
        if (updateDatabase(uuid, booster)) {
            data.addBooster(booster);
            return true;
        }
        return false;
    }

    private boolean giveBooster(UUID uuid, Booster booster) {
        return updateDatabase(uuid, booster);
    }

    /**
     * Update the player's currency in the database.
     * @param uuid The UUID of the player
     * @param booster The booster to set
     */
    private boolean updateDatabase(UUID uuid, Booster booster) {
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

    private String getQuery() {
        return "INSERT INTO player_boosters (uuid, booster_type, duration, boost_amount)\n" +
                "VALUES (?, ?, ?, ?);";
    }

    private void sendConfirmMessage(CommandSender sender) {

    }
}
