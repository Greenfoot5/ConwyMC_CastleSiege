package me.huntifi.castlesiege.commands.staff.punishments;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.Punishments;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class Warn implements CommandExecutor {

    /**
     * Warn a player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether a player and reason are supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return false;
        }

        // Attempt to warn the player asynchronously, as the database is involved
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        warnOffline(sender, args);
                    } else {
                        warn(sender, p.getUniqueId(), args);
                    }
                } catch (SQLException e) {
                    Messenger.sendError("An error occurred while trying to warn: <red>" + args[0], sender);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Get the UUID of a player that is currently offline
     * @param s Source of the command
     * @param args Passed command arguments
     */
    private void warnOffline(CommandSender s, String[] args) throws SQLException {
        UUID uuid = LoadData.getUUID(args[0]);
        if (uuid == null) {
            Messenger.sendError("Could not find player: <red>" + args[0], s);
        } else {
            warn(s, uuid, args);
        }
    }

    /**
     * Warn the player
     * @param s Source of the command
     * @param uuid Unique ID of the player to mute
     * @param args Passed command arguments
     */
    private void warn(CommandSender s, UUID uuid, String[] args) throws SQLException {
        // Get the warning reason
        String reason = String.join(" ", args).split(" ", 2)[1];

        // Apply the warning to our database
        Punishments.add(args[0], uuid, null, "warn", reason, 0);
        warnOnline(uuid, reason);
        Messenger.sendSuccess(  "Successfully warned: <green>" + args[0], s);
    }

    /**
     * Warn the online player
     * @param uuid The unique ID of the player
     * @param reason The reason for the warning
     */
    private void warnOnline(UUID uuid, String reason) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            Messenger.sendError("You were warned for: <red>" + reason, p);
        }
    }
}
