package me.greenfoot5.castlesiege.commands.info.leaderboard;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.LoadData;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
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
public class LeaderboardCommand implements CommandExecutor {
    
    public static final String gradient = "#13DB5D:#05B6D9:#F907FC";

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
                Messenger.sendError("Something went wrong! Please contact an administrator.", sender);
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
                    Component message = Messenger.mm.deserialize(
                            "<color:#CCCCCC>#. Player <yellow>L</yellow>evel " +
                                    "<transition:" + gradient + ":0>S</transition>core " +
                                    "<transition:" + gradient + ":0.15>K</transition>ills " +
                                    "<transition:" + gradient + ":0.4>D</transition>eaths " +
                                    "<transition:" + gradient + ":0.6>K</transition>DR " +
                                    "<transition:" + gradient + ":0.7>A</transition>ssists " +
                                    "<transition:" + gradient + ":0.8>C</transition>aptures " +
                                    "<transition:" + gradient + ":0.9>H</transition>eals " +
                                    "<transition:" + gradient + ":1>S</transition>upports"
                    );

                    int pos = offset;
                    while (top.getSecond().next()) {
                        pos++;
                        String color = pos == requested ? "aqua>" : "dark_aqua>";
                        message = message.append(addPlayer(top.getSecond(), pos, color, category));
                    }

                    top.getFirst().close();

                    // Send the message
                    Messenger.send(message, sender);

                } catch (SQLException e) {
                    e.printStackTrace();
                    Messenger.sendError("Something went wrong! " +
                                    "Please contact an administrator if this issue persists.", sender);
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    private Component addPlayer(ResultSet r, int pos, String color, String category) throws SQLException {
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat num = new DecimalFormat("0");

        return switch (category) {
            case "score" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0>" + num.format(r.getInt("score")) + "</transition> "
                    + "<transition:" + gradient + ":0.15>" + num.format(r.getDouble("kills")) + "</transition> "
                    + "<transition:" + gradient + ":0.4>" + num.format(r.getDouble("deaths")) + "</transition> "
                    + "<transition:" + gradient + ":0.6>" + dec.format(r.getDouble("kdr")) + "</transition> "
                    + "<transition:" + gradient + ":0.7>" + num.format(r.getDouble("assists")) + "</transition> "
                    + "<transition:" + gradient + ":0.8>" + num.format(r.getDouble("captures")) + "</transition> "
                    + "<transition:" + gradient + ":0.9>" + num.format(r.getDouble("heals")) + "</transition> "
                    + "<transition:" + gradient + ":1>" + num.format(r.getDouble("supports")) + "</transition>");
            case "captures" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.8>" + num.format(r.getDouble("captures")) + "</transition>");
            case "deaths" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.4>" + num.format(r.getDouble("deaths")) + "</transition>");
            case "kdr" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.6>" + dec.format(r.getDouble("kdr")) + "</transition>");
            case "kills" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.15>" + num.format(r.getDouble("kills")) + "</transition>");
            case "assists" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.7>" + num.format(r.getDouble("assists")) + "</transition>");
            case "heals" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0.9>" + num.format(r.getDouble("heals")) + "</transition>");
            case "supports" -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":1>" + num.format(r.getDouble("supports")) + "</transition>");
            default -> Messenger.mm.deserialize("<br>" +
                    "<gray>" + pos + ". <" + color + r.getString("name") + "</" + color
                    + " <yellow>" + r.getInt("level") + "</yellow> "
                    + "<transition:" + gradient + ":0>" + num.format(r.getInt("score")) + "</transition> "
                    + "<transition:" + gradient + ":0.15>" + num.format(r.getDouble("kills")) + "</transition> "
                    + "<transition:" + gradient + ":0.4>" + num.format(r.getDouble("deaths")) + "</transition> "
                    + "<transition:" + gradient + ":0.6>" + dec.format(r.getDouble("kdr")) + "</transition> "
                    + "<transition:" + gradient + ":0.7>" + num.format(r.getDouble("assists")) + "</transition> "
                    + "<transition:" + gradient + ":0.8>" + num.format(r.getDouble("captures")) + "</transition> "
                    + "<transition:" + gradient + ":0.9>" + num.format(r.getDouble("heals")) + "</transition> "
                    + "<transition:" + gradient + ":1>" + num.format(r.getDouble("supports")) + "</transition>");
        };
    }
}
