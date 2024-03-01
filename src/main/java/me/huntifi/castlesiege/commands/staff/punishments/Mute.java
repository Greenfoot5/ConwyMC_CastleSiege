package me.huntifi.castlesiege.commands.staff.punishments;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Mutes a player
 */
public class Mute implements CommandExecutor, Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();

        // Check if the player is muted
        if (isMuted(p.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    /**
     * Mute a player
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

        // Attempt to mute the player asynchronously, as the database is involved
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        muteOffline(sender, args);
                    } else {
                        mute(sender, p.getUniqueId(), args);
                    }
                } catch (SQLException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "An error occurred while trying to mute: "
                            + ChatColor.RED + args[0]);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Mute the player
     * @param s Source of the command
     * @param uuid Unique ID of the player to mute
     * @param args Passed command arguments
     */
    private void mute(CommandSender s, UUID uuid, String[] args) throws SQLException {
        // Get the mute duration
        long duration = PunishmentTime.getDuration(args[1]);
        if (duration == 0) {
            PunishmentTime.wrongFormat(s);
            return;
        }

        // Get the mute reason
        String reason = String.join(" ", args).split(" ", 3)[2];

        // Apply the mute to our database
        Punishments.add(args[0], uuid, null, "mute", reason, duration);
        muteOnline(uuid, reason, args[1]);
        s.sendMessage(ChatColor.DARK_GREEN + "Successfully muted: " + ChatColor.GREEN + args[0]);
    }

    /**
     * Mute the online player
     * @param uuid The unique ID of the player
     * @param reason The reason for the mute
     * @param duration The duration of the mute
     */
    private void muteOnline(UUID uuid, String reason, String duration) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            ActiveData.getData(uuid).setMute(reason, new Timestamp(System.currentTimeMillis() + PunishmentTime.getDuration(duration)));
            p.sendMessage(ChatColor.DARK_RED + "You were muted for: " + ChatColor.RED + reason);
            p.sendMessage(ChatColor.DARK_RED + "This mute expires in: " + PunishmentTime.getExpire(duration));
        }
    }

    /**
     * Get the UUID of a player that is currently offline
     * @param s Source of the command
     * @param args Passed command arguments
     */
    private void muteOffline(CommandSender s, String[] args) throws SQLException {
        UUID uuid = LoadData.getUUID(args[0]);
        if (uuid == null) {
            s.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
        } else {
            mute(s, uuid, args);
        }
    }

    /**
     * Check if the player is muted and inform muted players of their muted
     * @param uuid The unique ID of the player
     * @return Whether the player is muted
     */
    public static boolean isMuted(UUID uuid) {
        PlayerData data = ActiveData.getData(uuid);
        if (data == null || data.getMute() == null) {
            return false;
        }

        Player p = Bukkit.getPlayer(uuid);
        assert p != null;
        Tuple<String, Timestamp> mute = data.getMute();
        if (mute.getSecond().after(new Timestamp(System.currentTimeMillis()))) {
            p.sendMessage(ChatColor.DARK_RED + "You are muted for: " + ChatColor.RED + mute.getFirst());
            p.sendMessage(ChatColor.DARK_RED + "This mute expires in: " + PunishmentTime.getExpire(mute.getSecond()));
            return true;
        } else {
            data.setMute(null, null);
            p.sendMessage(ChatColor.GREEN + "Your mute has expired!");
            return false;
        }
    }
}
