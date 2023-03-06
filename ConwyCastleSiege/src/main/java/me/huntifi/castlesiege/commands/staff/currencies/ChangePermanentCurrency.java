package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Changes a player's permanent currency
 */
public abstract class ChangePermanentCurrency extends ChangeCurrency {

    /**
     * Change a player's permanent currency.
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    @Override
    protected void changeCurrency(CommandSender sender, String[] args) {
        if (hasIncorrectArgs(sender, args))
            return;

        String playerName = args[0];
        double amount = Double.parseDouble(args[1]);
        boolean verbose = true;
        if (args.length == 3)
            verbose = Boolean.parseBoolean(args[2]);

        UUID uuid = getUUID(playerName);
        assert uuid != null;
        PlayerData data = ActiveData.getData(uuid);
        if (data != null)
            changeCurrencyOnline(data, amount);
        else
            updateDatabase(uuid, amount);

        sendConfirmMessage(sender, playerName, amount);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && verbose)
            sendTargetMessage(player, amount);
    }

    /**
     * Get the player's UUID directly from the online players or from the database.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    @Override
    protected UUID getUUID(String playerName) {
        // Player is online
        UUID uuid = super.getUUID(playerName);
        if (uuid != null)
            return uuid;

        // Player is offline or doesn't exist in our database
        try {
            return LoadData.getUUID(playerName);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Update the player's currency in the database.
     * @param uuid The UUID of the player
     * @param amount The amount of currency to set
     */
    private void updateDatabase(UUID uuid, double amount) {
        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(getQuery())) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the query used to update the player's coins in the database.
     * @return The SQL query
     */
    protected abstract String getQuery();
}
