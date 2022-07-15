package me.huntifi.castlesiege.commands.info.leaderboard;

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
            case "TopKDR":
                category = "kdr";
                break;
            case "TopKills":
                category = "kills";
                break;
            case "TopAssists":
                category = "assists";
                break;
            case "TopHeals":
                category = "heals";
                break;
            case "TopSupports":
                category = "supports";
                break;
            default:
                Messenger.sendError(ChatColor.DARK_RED + "Something went wrong! Please contact an administrator.", sender);
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

                    // Create the message header
                    StringBuilder message = new StringBuilder();
                    message.append(ChatColor.AQUA).append("#. Player ")
                            .append(ChatColor.GOLD).append("L").append(ChatColor.AQUA).append("evel ")
                            .append(ChatColor.WHITE).append("S").append(ChatColor.AQUA).append("core ")
                            .append(ChatColor.GREEN).append("K").append(ChatColor.AQUA).append("ills ")
                            .append(ChatColor.RED).append("D").append(ChatColor.AQUA).append("eaths ")
                            .append(ChatColor.YELLOW).append("K").append(ChatColor.AQUA).append("DR ")
                            .append(ChatColor.DARK_GREEN).append("A").append(ChatColor.AQUA).append("ssists ")
                            .append(ChatColor.GRAY).append("C").append(ChatColor.AQUA).append("aptures ")
                            .append(ChatColor.LIGHT_PURPLE).append("H").append(ChatColor.AQUA).append("eals ")
                            .append(ChatColor.DARK_PURPLE).append("S").append(ChatColor.AQUA).append("upports");

                    // Add the stat entries
                    DecimalFormat dec = new DecimalFormat("0.00");
                    DecimalFormat num = new DecimalFormat("0");
                    int pos = offset;
                    while (top.getSecond().next()) {
                        pos++;
                        ChatColor color = pos == requested ? ChatColor.AQUA : ChatColor.DARK_AQUA;

                        message.append("\n").append(ChatColor.GRAY).append(pos).append(". ")
                                .append(color).append(top.getSecond().getString("name")).append(" ")
                                .append(ChatColor.GOLD).append(top.getSecond().getInt("level")).append(" ")
                                .append(ChatColor.WHITE).append(num.format(top.getSecond().getDouble("score"))).append(" ")
                                .append(ChatColor.GREEN).append(num.format(top.getSecond().getDouble("kills"))).append(" ")
                                .append(ChatColor.RED).append(num.format(top.getSecond().getDouble("deaths"))).append(" ")
                                .append(ChatColor.YELLOW).append(dec.format(top.getSecond().getDouble("kdr"))).append(" ")
                                .append(ChatColor.DARK_GREEN).append(num.format(top.getSecond().getDouble("assists"))).append(" ")
                                .append(ChatColor.GRAY).append(num.format(top.getSecond().getDouble("captures"))).append(" ")
                                .append(ChatColor.LIGHT_PURPLE).append(num.format(top.getSecond().getDouble("heals"))).append(" ")
                                .append(ChatColor.DARK_PURPLE).append(num.format(top.getSecond().getDouble("supports")));
                    }
                    top.getFirst().close();

                    // Send the message
                    sender.sendMessage(message.toString());

                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.DARK_RED +
                            "Something went wrong! Please contact an administrator if this issue persists.");
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
