package me.huntifi.castlesiege.commands.info.leaderboard;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.maps.SpectateCommand;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.MVPStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.DARK_AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

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
                        sender.sendMessage(getMVPMessage(team));
                    }

                } else if (!(sender instanceof ConsoleCommandSender)) {
                    // Print the MVP of the player's team and their own stats
                    Player p = (Player) sender;

                    if (SpectateCommand.spectators.contains(p.getUniqueId())) {
                        Messenger.sendError("You aren't on a team!", sender);
                        return;
                    }

                    UUID uuid = p.getUniqueId();
                    Team t = TeamController.getTeam(uuid);
                    sender.sendMessage(getMVPMessage(t));
                    if (t.getMVP().getFirst() != uuid) { // Only print sender stats if sender is not MVP
                        sender.sendMessage(getPlayerMessage(uuid));
                    }

                } else {
                    Messenger.sendError("Console is not in a team!", sender);
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Get the MVP message for a player
     * @param uuid The unique ID of the player
     */
    private Component getPlayerMessage(UUID uuid) {
        PlayerData data = MVPStats.getStats(uuid);
        Team team = TeamController.getTeam(uuid);
        return getMessage(uuid, data, team, false);
    }

    /**
     * Get the MVP message for a team
     * @param t The team for which to get the MVP
     * @return The component message
     */
    public static Component getMVPMessage(Team t) {
        Tuple<UUID, PlayerData> mvp = t.getMVP();
        if (mvp == null || mvp.getSecond() == null) {
            return Component.text(t.name, t.primaryChatColor)
                    .append(Component.text(" MVP: ", DARK_AQUA))
                    .append(Component.text("N/A", NamedTextColor.WHITE));
        }
        return getMessage(mvp.getFirst(), mvp.getSecond(), t, true);
    }

    /**
     * Get the MVP message for a player
     * @param data The player's data
     * @param t The team that the player is part of
     * @param mvp Whether the player is their team's MVP
     */
    private static Component getMessage(UUID uuid, PlayerData data, Team t, boolean mvp) {
        Component c;
        // Header
        if (mvp) {
            c = Component.text(t.name, t.primaryChatColor)
                    .append(Component.text(" MVP ", DARK_AQUA))
                    .append(Component.text(Bukkit.getOfflinePlayer(uuid).getName(), WHITE));
        } else {
            c = Component.text(t.name, t.primaryChatColor)
                    .append(Component.text(" You:", DARK_AQUA));
        }

        // Stats
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");

        c = c.append(Component.newline())
                .append(Component.text("Score ", DARK_AQUA))
                .append(Component.text(dec.format(data.getScore()), WHITE))
                .append(Component.text(" | Kills ", DARK_AQUA))
                .append(Component.text(num.format(data.getKills()), WHITE))
                .append(Component.text(" | Deaths ", DARK_AQUA))
                .append(Component.text(num.format(data.getDeaths()), WHITE))
                .append(Component.text(" | KDR ", DARK_AQUA))
                .append(Component.text(dec.format(data.getKills() / data.getDeaths()), WHITE))
                .append(Component.text(" | Assists ", DARK_AQUA))
                .append(Component.text(num.format(data.getAssists()), WHITE));
        c = c.append(Component.newline())
                .append(Component.text(" | Captures ", DARK_AQUA))
                .append(Component.text(num.format(data.getCaptures()), WHITE))
                .append(Component.text(" | Heals ", DARK_AQUA))
                .append(Component.text(num.format(data.getCaptures()), WHITE))
                .append(Component.text(" | Supports ", DARK_AQUA))
                .append(Component.text(num.format(data.getSupports()), WHITE));

        return c;
    }
}
