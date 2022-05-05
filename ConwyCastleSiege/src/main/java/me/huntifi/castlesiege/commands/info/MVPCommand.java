package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

import static org.bukkit.Bukkit.getPlayer;

/**
 * Shows the player the current MVP(s)
 */
public class MVPCommand implements CommandExecutor {

    /**
     * Print the current MVP(s) asynchronously
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (label.equalsIgnoreCase("MVPs")) {
                    // Print the MVP of each team
                    for (Team team : MapController.getCurrentMap().teams) {
                        for (String message : getMVPMessage(team)) {
                            sender.sendMessage(message);
                        }
                    }

                } else if (!(sender instanceof ConsoleCommandSender)) {
                    // Print the MVP of the player's team and their own stats
                    UUID uuid = ((Player) sender).getUniqueId();
                    for (String message : getMVPMessage(MapController.getCurrentMap().getTeam(uuid))) {
                        sender.sendMessage(message);
                    }
                    for (String message : getPlayerMessage(uuid)) {
                        sender.sendMessage(message);
                    }

                } else {
                    sender.sendMessage("Console is not in a team!");
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Get the MVP message for a player
     * @param uuid The unique ID of the player
     */
    private Collection<String> getPlayerMessage(UUID uuid) {
        PlayerData data = MVPStats.getStats(uuid);
        Team team = MapController.getCurrentMap().getTeam(uuid);
        return getMessage(uuid, data, team, false);
    }

    /**
     * Get the MVP message for a team
     * @param t The team for which to get the MVP
     */
    public static Collection<String> getMVPMessage(Team t) {
        Tuple<UUID, PlayerData> mvp = t.getMVP();
        if (mvp == null) {
            return Collections.singletonList(t.primaryChatColor + t.name + ChatColor.DARK_AQUA
                    + " MVP: " + ChatColor.WHITE + "N/A");
        }
        return getMessage(mvp.getFirst(), mvp.getSecond(), t, true);
    }

    /**
     * Get the MVP message for a player
     * @param data The player's data
     * @param t The team that the player is part of
     * @param mvp Whether the player is their team's MVP
     */
    private static Collection<String> getMessage(UUID uuid, PlayerData data, Team t, boolean mvp) {
        Collection<String> message = new ArrayList<>();

        // Header
        if (mvp) {
            message.add(t.primaryChatColor + t.name + ChatColor.DARK_AQUA
                    + " MVP: " + ChatColor.WHITE + Objects.requireNonNull(getPlayer(uuid)).getName());
        } else {
            message.add(t.primaryChatColor + t.name + ChatColor.DARK_AQUA + " You:");
        }

        // Stats
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");

        message.add(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + dec.format(data.getScore())
                + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + num.format(data.getKills())
                + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + num.format(data.getDeaths())
                + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + dec.format(data.getKills() / data.getDeaths())
                + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + num.format(data.getAssists()));
        message.add(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + num.format(data.getHeals())
                + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + num.format(data.getCaptures())
                + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + num.format(data.getSupports()));

        return message;
    }
}
