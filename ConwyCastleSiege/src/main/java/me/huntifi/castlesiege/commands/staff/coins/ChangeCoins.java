package me.huntifi.castlesiege.commands.staff.coins;

import me.huntifi.castlesiege.Main;
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

/**
 * Changes a player's coins
 */
public abstract class ChangeCoins implements CommandExecutor {

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> changeCoins(sender, args));
        return true;
    }

    /**
     * Change a player's coins.
     * @param sender Source of the command
     * @param args Passed command arguments
     */
    protected void changeCoins(CommandSender sender, String[] args) {
        if (hasIncorrectArgs(sender, args))
            return;

        String playerName = args[0];
        double amount = Double.parseDouble(args[1]);

        UUID uuid = getUUID(playerName);
        assert uuid != null;
        PlayerData data = ActiveData.getData(uuid);
        if (data != null)
            changeCoinsOnline(data, amount);
        else
            updateDatabase(uuid, amount);

        sendConfirmMessage(sender, playerName, amount);
    }

    /**
     * Check if correct arguments were provided for this command.
     * @param sender Source of the command
     * @param args Passed command arguments
     * @return Whether valid arguments were provided
     */
    protected boolean hasIncorrectArgs(CommandSender sender, String[] args) {
        // Command format is not followed
        if (args.length != 2) {
            Messenger.sendError(getCommandUsage(), sender);
            return true;
        }

        // Player not found
        String playerName = args[0];
        if (getUUID(playerName) == null) {
            Messenger.sendError("Could not find player: " + playerName, sender);
            return true;
        }

        // Amount not a number
        String amount = args[1];
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            Messenger.sendError(amount + " is not a number!", sender);
            return true;
        }

        return false;
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

        // Player is offline or doesn't exit in our database
        try {
            return LoadData.getUUID(playerName);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Update the player's coins in the database.
     * @param uuid The UUID of the player
     * @param amount The coins to set
     */
    protected void updateDatabase(UUID uuid, double amount) {
        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(getQuery())) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the message describing how to use the command.
     * @return The command usage message
     */
    protected abstract String getCommandUsage();

    /**
     * Get the query used to update the player's coins in the database.
     * @return The SQL query
     */
    protected abstract String getQuery();

    /**
     * Change the player's coins for an online player.
     * @param data The actively stored data of the online player
     * @param amount The amount of coins used in the change
     */
    protected abstract void changeCoinsOnline(PlayerData data, double amount);

    /**
     * Send a confirmation message to the source of the command.
     * @param sender Source of the command
     * @param playerName The name of the player whose coins were changed
     * @param amount The amount of coins used in the change
     */
    protected abstract void sendConfirmMessage(CommandSender sender, String playerName, double amount);
}
