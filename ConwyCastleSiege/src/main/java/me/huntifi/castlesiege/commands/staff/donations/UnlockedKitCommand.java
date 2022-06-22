package me.huntifi.castlesiege.commands.staff.donations;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.punishments.PunishmentTime;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.KitList;
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
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class UnlockedKitCommand implements CommandExecutor {

    /**
     * Add / Remove a kit from a player.
     *
     * @param sender Source of the command
     * @param cmd    Command which was executed
     * @param label  Alias of the command which was used
     * @param args   Passed command arguments
     * @return whether valid arguments were supplied
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 3) {
            Messenger.sendError("Use: /unlockkit <Name> <add/remove> <Kit name> <Time> <true/false>", sender);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Timestamp timestamp;
                try {

                    UUID uuid = LoadData.getUUID(args[0]);

                    switch (args[1].toLowerCase()) {
                        case "add":
                            if (args.length < 5) {
                                Messenger.sendError("Use: /unlockkit <Name> <add/remove> <Kit name> <Time> <true/false>", sender);
                            }

                            if (uuid == null) {
                                Messenger.sendError("This player is not found!", sender);
                                return;
                            }

                            long duration = PunishmentTime.getDuration(args[3]);

                            Tuple<PreparedStatement, ResultSet> rs = LoadData.getActiveKit(uuid, args[2]);
                            timestamp = LoadData.getKitTimestamp(rs.getSecond());

                            //Should the player enter 0 time, return error message
                            if (duration == 0) {
                                PunishmentTime.wrongFormat(sender);
                                return;
                            }

                            //If there is a timestamp already add the duration to it.
                            if (timestamp != null) {
                                //Timestamp exists
                                if (timestamp.getTime() > getCurrentTime()) {
                                    //Timestamp is younger/longer than current time so add duration on top of it.
                                    duration = (timestamp.getTime() + duration);
                                    Bukkit.getConsoleSender().sendMessage("Yeah uhm the duration for this is: " + duration);
                                } else {
                                    //Timestamp is older/shorter than current time so change the duration.
                                    duration = (System.currentTimeMillis() + duration);
                                    Bukkit.getConsoleSender().sendMessage("Yeah uhm the duration for this is: " + duration);
                                }
                            } else {
                                //Timestamp is null, so create a new timestamp.
                                duration = (System.currentTimeMillis() + duration);
                                Bukkit.getConsoleSender().sendMessage("Yeah uhm the duration for this is: " + duration);
                            }

                            //This is the kit, the kit should be in the kits list in order for it to be an existing one.
                            if (!KitList.getAllKits().contains(args[2])) {
                                Messenger.sendError("An invalid kit was provided.", sender);
                                return;
                            }

                                //If true then this means the player donated, usually only the console will do this.
                                if (args[4].equalsIgnoreCase("true")) {

                                    StoreData.addUnlockedKit(uuid, args[2], duration, true);

                                    Messenger.sendInfo("Successfully added " + args[2] + " to "
                                            + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                                    //If false then this means the player got it without donating.
                                } else if (args[4].equalsIgnoreCase("false")) {

                                    StoreData.addUnlockedKit(uuid, args[2], duration, false);

                                    Messenger.sendInfo("Successfully added " + args[2] + " to "
                                            + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                                    //If an illegal argument will be given, it will be set to false by default.
                                } else {
                                    StoreData.addUnlockedKit(uuid, args[2], duration, false);

                                    Messenger.sendInfo("Successfully added " + args[2] + " to "
                                            + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                                }

                            break;
                        case "remove":

                            //This is the kit, the kit should be in the kits list in order for it to be an existing one.
                            if (!KitList.getAllKits().contains(args[2])) {
                                Messenger.sendError("An invalid kit was provided.", sender);
                                return;
                            }

                                StoreData.endUnlockedKit(uuid, args[2]);

                                Messenger.sendInfo("Successfully removed " + args[2] + " from "
                                        + Bukkit.getOfflinePlayer(uuid).getName(), sender);



                            break;
                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "The operation " + ChatColor.RED + args[1]
                                    + ChatColor.DARK_RED + " is not supported!");
                            sender.sendMessage(ChatColor.DARK_RED + "Please use one of the following: "
                                    + ChatColor.RED + "add, remove");
                            break;
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "The argument " + ChatColor.RED + args[3]
                            + ChatColor.DARK_RED + " is not a number!");
                } catch (SQLException e) {
                    sender.sendMessage(ChatColor.DARK_RED + "An error occurred while trying to perform your command!");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }


    public long getCurrentTime() {
        //Getting the current date
        Date date = new Date();
        //This method returns the time in millis
        long timeMilli = date.getTime();

        return timeMilli;
    }
}
