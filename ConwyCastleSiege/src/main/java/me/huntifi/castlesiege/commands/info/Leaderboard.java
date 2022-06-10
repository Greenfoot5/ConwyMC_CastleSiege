package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
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

/**
 * Shows the player the leaderboard
 */
public class Leaderboard implements CommandExecutor {

    /**
     * Print the score leaderboard to the player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String category;
        switch (cmd.getName()) {
            case "Top":
                category = "score";
                break;
            case "TopCaptures":
                category = "captures";
                break;
            case "TopDeaths":
                category = "deaths";
                break;
            case "TopKills":
                category = "kills";
                break;
            default:
                sender.sendMessage(ChatColor.DARK_RED + "Something went wrong! Please contact an administrator.");
                return true;
        }

        if (args.length == 0) {
            display(sender, category, 0);
        } else {
            try {
                display(sender, category, Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                // TODO - player specified
                Messenger.sendError("Searching by player is currently not supported!", sender);
            }
        }

        return true;
    }

    /**
     * Print the leaderboard to the player, sorted by the supplied category
     * @param sender Source of the command
     * @param category The category to sort by
     * @param requested The requested position
     */
    private void display(CommandSender sender, String category, int requested) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    int offset = requested < 6 ? 0 : Math.min(requested - 5, 90);

                    Tuple<PreparedStatement, ResultSet> top = LoadData.getTop(category, offset);

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
                    int pos = offset;
                    while (top.getSecond().next()) {
                        pos++;
                        ChatColor color = pos == requested ? ChatColor.AQUA : ChatColor.DARK_AQUA;
                        sender.sendMessage(ChatColor.GRAY + num.format(pos) + ". " +
                                color + top.getSecond().getString("name") + " " +
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
