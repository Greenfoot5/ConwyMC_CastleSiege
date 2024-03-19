package me.huntifi.castlesiege.commands.staff.punishments;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.Punishments;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * Unmutes a player
 */
public class Unmute implements CommandExecutor {

    /**
     * Unmute a player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a player is specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        // Attempt to unmute the player asynchronously, as the database is involved
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Punishments.end(args[0], "mute");
                    unmuteOnline(args[0]);
                    Messenger.sendSuccess("Successfully unmuted: <green>" + args[0], sender);
                } catch (SQLException e) {
                    Messenger.sendError("An error occurred while trying to unmute: <red>" + args[0], sender);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Unmute the online player
     * @param name The name of the player
     */
    private void unmuteOnline(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            ActiveData.getData(p.getUniqueId()).setMute(null, null);
            Messenger.sendSuccess("Your mute has been removed!", p);
        }
    }
}
