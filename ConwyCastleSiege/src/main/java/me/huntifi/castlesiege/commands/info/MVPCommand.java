package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

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
                    for (Team team : MapController.getCurrentMap().teams) {
                        printMVP(sender, team);
                    }
                } else {
                    UUID uuid = ((Player) sender).getUniqueId();
                    printMVP(sender, MapController.getCurrentMap().getTeam(uuid));
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Print the MVP to the recipient
     * @param r The recipient for whom to print
     * @param t The team whose MVP to print
     */
    private void printMVP(CommandSender r, Team t) {
        // Empty team -> no MVP
        Tuple<UUID, PlayerData> mvp = t.getMVP();
        if (mvp == null) {
            r.sendMessage(t.primaryChatColor + t.name + ChatColor.DARK_AQUA
                    + " MVP: " + ChatColor.WHITE + "N/A");
            return;
        }

        // Print MVP
        r.sendMessage(t.primaryChatColor + t.name + ChatColor.DARK_AQUA
                + " MVP: " + ChatColor.WHITE + Objects.requireNonNull(getPlayer(mvp.getFirst())).getName());
        r.sendMessage(ChatColor.DARK_AQUA + "Score " + ChatColor.WHITE + mvp.getSecond().getScore()
                + ChatColor.DARK_AQUA + " | Kills " + ChatColor.WHITE + mvp.getSecond().getKills()
                + ChatColor.DARK_AQUA + " | Deaths " + ChatColor.WHITE + mvp.getSecond().getDeaths()
                + ChatColor.DARK_AQUA + " | KDR " + ChatColor.WHITE + mvp.getSecond().getKills() / mvp.getSecond().getDeaths()
                + ChatColor.DARK_AQUA + " | Assists " + ChatColor.WHITE + mvp.getSecond().getAssists());
        r.sendMessage(ChatColor.DARK_AQUA + "| Heals " + ChatColor.WHITE + mvp.getSecond().getHeals()
                + ChatColor.DARK_AQUA + " | Captures " + ChatColor.WHITE + mvp.getSecond().getCaptures()
                + ChatColor.DARK_AQUA + " | Supports " + ChatColor.WHITE + mvp.getSecond().getSupports());
    }
}
