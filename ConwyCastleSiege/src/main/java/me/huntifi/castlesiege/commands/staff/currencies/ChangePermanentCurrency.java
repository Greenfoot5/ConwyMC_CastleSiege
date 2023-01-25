package me.huntifi.castlesiege.commands.staff.currencies;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Changes a player's coins
 */
public abstract class ChangePermanentCurrency extends ChangeCurrency {

    /**
     * Change a player's coins asynchronously.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> changeCurrency(sender, args));
        return true;
    }

    /**
     * Change a player's coins.
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    @Override
    protected void changeCurrency(CommandSender sender, String[] args) {
        if (super.hasIncorrectArgs(sender, args))
            return;

        String playerName = args[0];
        double amount = Double.parseDouble(args[1]);

        UUID uuid = super.getUUID(playerName);
        assert uuid != null;
        PlayerData data = ActiveData.getData(uuid);
        if (data != null)
            changeCurrencyOnline(data, amount);
        else
            updateDatabase(uuid, amount);

        sendConfirmMessage(sender, playerName, amount);
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
