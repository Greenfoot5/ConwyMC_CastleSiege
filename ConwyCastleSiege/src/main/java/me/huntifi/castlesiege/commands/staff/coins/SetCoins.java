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
 * Sets a player's coins
 */
public class SetCoins implements CommandExecutor {

    /**
     * Set the active coin multiplier, if a positive number is supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> setCoins(sender, args));
        return true;
    }

    private void setCoins(CommandSender sender, String[] args) {
        if (!hasCorrectArgs(sender, args))
            return;

        String playerName = args[0];
        double amount = Double.parseDouble(args[1]);

        UUID uuid = getUUID(playerName);
        assert uuid != null;
        PlayerData data = ActiveData.getData(uuid);
        if (data != null)
            data.setCoins(amount);
        else
            setCoinsDatabase(uuid, amount);

        Messenger.sendInfo(String.format("%s's coins have been set to %.0f", playerName, amount), sender);
    }

    /**
     * Check if correct arguments were provided for this command
     * @param sender Source of the command
     * @param args Passed command arguments
     * @return Whether valid arguments were provided
     */
    private boolean hasCorrectArgs(CommandSender sender, String[] args) {
        // Command format is not followed
        if (args.length != 2) {
            Messenger.sendError("Use: /setcoins <player> <amount>", sender);
            return false;
        }

        // Player not found
        String playerName = args[0];
        if (getUUID(playerName) == null) {
            Messenger.sendError("Could not find player: " + playerName, sender);
            return false;
        }

        // Amount not a number
        String amount = args[1];
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            Messenger.sendError(amount + " is not a number!", sender);
            return false;
        }

        return true;
    }

    /**
     * Get the player's UUID directly from the online players or from the database.
     * @param playerName The name of the player
     * @return The player's UUID if found, null otherwise
     */
    private UUID getUUID(String playerName) {
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
     * Set the player's coins in the database
     * @param uuid The UUID of the player
     * @param amount The coins to set
     */
    private void setCoinsDatabase(UUID uuid, double amount) {
        try (PreparedStatement ps = Main.SQL.getConnection().prepareStatement(
                "UPDATE player_stats SET coins = ? WHERE uuid = ?")) {
            ps.setDouble(1, amount);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
