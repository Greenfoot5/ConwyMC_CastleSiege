package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.LoadData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Shows the player the leaderboard
 */
public class Leaderboard implements CommandExecutor {

    private final Collection<String> categories = Arrays.asList(
            "score", "kills", "deaths", "assists", "captures", "heals", "supports");

    // TODO:
    // Allow specific leaderboard spot to be requested
    // Work out categories

    /**
     * Print the leaderboard to the player if a valid category was supplied
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            display(sender, "score");
        } else if (categories.contains(args[0].toLowerCase())) {
            display(sender, args[0].toLowerCase());
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Category " + ChatColor.RED + args[0] +
                    ChatColor.DARK_RED + " is invalid!");
        }

        return true;
    }

    /**
     * Print the leaderboard to the player, sorted by the supplied category
     * @param sender Source of the command
     * @param category The category to sort by
     */
    private void display(CommandSender sender, String category) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Tuple<PreparedStatement, ResultSet> top = LoadData.getTop(category);

                    // Send header
                    sender.sendMessage(ChatColor.AQUA + "#. Player " +
                            ChatColor.GOLD + "L" + ChatColor.AQUA + "evel " +
                            ChatColor.WHITE + "S" + ChatColor.AQUA + "core " +
                            ChatColor.GREEN + "K" + ChatColor.AQUA + "ills " +
                            ChatColor.RED + "D" + ChatColor.AQUA + "eaths " +
                            ChatColor.YELLOW + "K" + ChatColor.AQUA + "DR " +
                            ChatColor.GRAY + "C" + ChatColor.AQUA + "aptures ");

                    // Send entries
                    DecimalFormat dec = new DecimalFormat("0.00");
                    DecimalFormat num = new DecimalFormat("0");
                    int pos = 0;
                    while (top.getSecond().next()) {
                        pos++;
                        sender.sendMessage(ChatColor.GRAY + num.format(pos) + ". " +
                                ChatColor.DARK_AQUA + top.getSecond().getString("name") + " " +
                                ChatColor.GOLD + top.getSecond().getInt("level") + " " +
                                ChatColor.WHITE + num.format(top.getSecond().getDouble("score")) + " " +
                                ChatColor.GREEN + num.format(top.getSecond().getDouble("kills")) + " " +
                                ChatColor.RED + num.format(top.getSecond().getDouble("deaths")) + " " +
                                ChatColor.YELLOW + dec.format(top.getSecond().getDouble("kills") /
                                    top.getSecond().getDouble("deaths")) + " " +
                                ChatColor.GRAY + num.format(top.getSecond().getDouble("captures")));
                    }
                    top.getFirst().close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.DARK_RED +
                            "Something went wrong! Please contact an administrator if this issue persists.");
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
