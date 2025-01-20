package me.greenfoot5.castlesiege.commands.info.leaderboard;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.data_types.CSStats;
import me.greenfoot5.castlesiege.database.MVPStats;
import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                    showMVPs(sender);

                } else if (sender instanceof Player p && TeamController.isPlaying(p)) {
                    UUID uuid = p.getUniqueId();
                    Team t = TeamController.getTeam(uuid);
                    Messenger.send(getMVPMessage(t), sender);
                    if (t.getMVP().getFirst() != uuid) { // Only print sender stats if sender is not MVP
                        Messenger.send(getPlayerMessage(uuid), sender);
                    }
                } else {
                    showMVPs(sender);
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Shows all the MVPs for the map
     * @param sender who to show
     */
    private void showMVPs(CommandSender sender) {
        // Print the MVP of each team
        for (Team team : MapController.getCurrentMap().teams) {
            Messenger.send(getMVPMessage(team), sender);
        }
    }

    /**
     * Get the MVP message for a player
     * @param uuid The unique ID of the player
     */
    private Component getPlayerMessage(UUID uuid) {
        CSStats data = MVPStats.getStats(uuid);
        Team team = TeamController.getTeam(uuid);
        return getMessage(uuid, data, team, false);
    }

    /**
     * Get the MVP message for a team
     * @param t The team for which to get the MVP
     * @return The component message
     */
    public static Component getMVPMessage(Team t) {
        Tuple<UUID, CSStats> mvp = t.getMVP();
        if (mvp == null || mvp.getSecond() == null) {
            return t.getDisplayName()
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
    private static Component getMessage(UUID uuid, CSStats data, Team t, boolean mvp) {
        Component c;
        // Header
        if (mvp) {
            c = t.getDisplayName()
                    .append(Component.text(" MVP ", DARK_AQUA))
                    .append(Component.text(Bukkit.getOfflinePlayer(uuid).getName(), WHITE));
        } else {
            c = t.getDisplayName()
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
                .append(Component.text(num.format(data.getHeals()), WHITE))
                .append(Component.text(" | Supports ", DARK_AQUA))
                .append(Component.text(num.format(data.getSupports()), WHITE));

        return c;
    }
}
