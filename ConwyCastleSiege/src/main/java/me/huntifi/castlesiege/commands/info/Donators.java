package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.RankPoints;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
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
 * Shows the player the donator leaderboard
 */
public class Donators implements CommandExecutor {

    /**
     * Print the donator leaderboard to the player
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            display(sender, 0);
        } else {
            try {
                display(sender, Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {
                // TODO - player specified
                Messenger.sendError("Searching by player is currently not supported!", sender);
            }
        }

        return true;
    }

    /**
     * Print at most 10 donators to the player, sorted by rank points in descending order
     * @param sender Source of the command
     * @param requested The requested position
     */
    private void display(CommandSender sender, int requested) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    int offset = requested < 7 ? 0 : requested - 5;

                    // Prepare data used for the message
                    Tuple<PreparedStatement, ResultSet> donators = LoadData.getDonators(offset);
                    StringBuilder message = new StringBuilder();
                    DecimalFormat num = new DecimalFormat("0");
                    int pos = offset;

                    // Create the message
                    message.append(ChatColor.AQUA).append("#. Player Rank Points");
                    while (donators.getSecond().next()) {
                        pos++;
                        ChatColor color = pos == requested ? ChatColor.AQUA : ChatColor.DARK_AQUA;
                        String name = donators.getSecond().getString("name");
                        int points = donators.getSecond().getInt("rank_points");

                        message.append("\n");
                        message.append(ChatColor.GRAY).append(num.format(pos)).append(". ");
                        message.append(color).append(name).append(" ");
                        message.append(getRank(pos, points)).append(" ");
                        message.append(ChatColor.WHITE).append(num.format(points));
                    }

                    // Send the message
                    sender.sendMessage(message.toString());
                    donators.getFirst().close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    Messenger.sendError("Something went wrong!" +
                            " Please contact an administrator if this issue persists.", sender);
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    /**
     * Get the donator rank that corresponds to the position
     * @param position The position on the donator leaderboard
     * @return The corresponding pretty donator rank
     */
    private String getRank(int position, int points) {
        String rank;
        if (position <= 10) {
            rank = RankPoints.getTopRank(position);
        } else {
            rank = RankPoints.getRank(points);
        }
        return NameTag.convertRank(rank);
    }
}
