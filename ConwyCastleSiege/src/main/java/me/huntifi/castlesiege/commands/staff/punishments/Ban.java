package me.huntifi.castlesiege.commands.staff.punishments;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

/**
 * Bans a player from the server
 */
public class Ban implements CommandExecutor {

    /**
     * Ban a player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a player, time, and reason are specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 3) {
            return false;
        }

        // Attempt to ban the player asynchronously, as the database is involved
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        banOffline(sender, args);
                    } else {
                        ban(sender, p.getUniqueId(), Objects.requireNonNull(p.getAddress()).getAddress(), args);
                    }
                } catch (SQLException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "An error occurred while trying to ban: "
                            + ChatColor.RED + args[0]);
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
    private void banOffline(CommandSender s, String[] args) throws SQLException {
        UUID uuid = LoadData.getUUID(args[0]);
        if (uuid == null) {
            s.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
        } else {
            ban(s, uuid, null, args);
        }
    }

    /**
     * Ban the player
     * @param s Source of the command
     * @param uuid Unique ID of the player to ban
     * @param args Passed command arguments
     */
    private void ban(CommandSender s, UUID uuid, InetAddress ip, String[] args) throws SQLException {
        // Get the ban duration
        long duration = PunishmentTime.getDuration(args[1]);
        if (duration == 0) {
            PunishmentTime.wrongFormat(s);
            return;
        }

        // Get the ban reason
        String reason = String.join(" ", args).split(" ", 3)[2];

        // Apply the ban to our database
        Punishments.add(args[0], uuid, ip, "ban", reason, duration);
        s.sendMessage(ChatColor.DARK_GREEN + "Successfully banned: " + ChatColor.GREEN + args[0]);

        // Kick the player if they are online
        kick(uuid, reason, args[1]);
    }

    /**
     * Kick the banned player from the server
     * @param uuid The unique ID of the player
     * @param reason The reason for the ban
     * @param duration The duration of the ban
     */
    private void kick(UUID uuid, String reason, String duration) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    p.kickPlayer(ChatColor.DARK_RED + "\n[BAN] " + ChatColor.RED + reason
                            + ChatColor.DARK_RED + "\n[EXPIRES IN] " + ChatColor.RED + PunishmentTime.getExpire(duration));
                }
            }
        }.runTask(Main.plugin);
    }
}
