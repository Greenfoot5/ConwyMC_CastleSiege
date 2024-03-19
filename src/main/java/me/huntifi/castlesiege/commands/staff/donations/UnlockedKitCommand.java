package me.huntifi.castlesiege.commands.staff.donations;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.punishments.PunishmentTime;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.database.StoreData;
import me.huntifi.conwymc.util.Messenger;
import me.huntifi.castlesiege.kits.kits.CoinKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UnlockedKitCommand implements TabExecutor {

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
            return true;
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
                            rs.getFirst().close();

                            //Should the player enter 0 time, return error message
                            if (duration == 0) {
                                PunishmentTime.wrongFormat(sender);
                                return;
                            }

                            //If there is a timestamp already add the duration to it.
                            if (timestamp != null) {
                                //Timestamp exists
                                if (timestamp.getTime() > System.currentTimeMillis()) {
                                    //Timestamp is younger/longer than current time so add duration on top of it.
                                    duration = (timestamp.getTime() + duration);
                                } else {
                                    //Timestamp is older/shorter than current time so change the duration.
                                    duration = (System.currentTimeMillis() + duration);
                                }
                            } else {
                                //Timestamp is null, so create a new timestamp.
                                duration = (System.currentTimeMillis() + duration);
                            }

                            //This is the kit, the kit should be in the kits list in order for it to be an existing one.
                            if (!Kit.getKits().contains(args[2])) {
                                Messenger.sendError("An invalid kit was provided.", sender);
                                return;
                            }

                            //If true then this means the player donated, usually only the console will do this.
                            if (args[4].equalsIgnoreCase("true")) {

                                StoreData.addUnlockedKit(uuid, args[2], duration, true);
                                ActiveData.getData(uuid).addKit(args[2]);

                                Messenger.sendInfo("Successfully added " + args[2] + " to "
                                        + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                                //If false then this means the player got it without donating.
                            } else if (args[4].equalsIgnoreCase("false")) {

                                StoreData.addUnlockedKit(uuid, args[2], duration, false);
                                ActiveData.getData(uuid).addKit(args[2]);

                                Messenger.sendInfo("Successfully added " + args[2] + " to "
                                        + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                                //If an illegal argument will be given, it will be set to false by default.
                            } else {
                                StoreData.addUnlockedKit(uuid, args[2], duration, false);
                                ActiveData.getData(uuid).addKit(args[2]);

                                Messenger.sendInfo("Successfully added " + args[2] + " to "
                                        + Bukkit.getOfflinePlayer(uuid).getName() + " for " + duration, sender);

                            }

                            break;
                        case "remove":

                            //This is the kit, the kit should be in the kits list in order for it to be an existing one.
                            if (!Kit.getKits().contains(args[2])) {
                                Messenger.sendError("An invalid kit was provided.", sender);
                                return;
                            }

                            StoreData.endUnlockedKit(uuid, args[2]);
                            ActiveData.getData(uuid).removeKit(args[2]);

                            Messenger.sendInfo("Successfully removed " + args[2] + " from "
                                    + Bukkit.getOfflinePlayer(uuid).getName(), sender);


                            break;
                        default:
                            Messenger.sendError(String.format("The operation %s is not supported\n " +
                                    "Please use one of the following: add, remove", args[1]), sender);
                            break;
                    }

                } catch (NumberFormatException e) {
                    Messenger.sendError(String.format("The argument %s is not a number!", args[3]), sender);
                } catch (SQLException e) {
                    Messenger.sendError("An error occurred while trying to perform your command!", sender);
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Use: /unlockkit <Name> <add/remove> <Kit name> <Time> <true/false>
        List<String> options = new ArrayList<>();
        if (args.length == 1 || args.length == 4) {
            return null;
        }

        if (args.length == 2) {
            List<String> values = List.of("add", "remove");
            StringUtil.copyPartialMatches(args[args.length - 1], values, options);
        }

        if (args.length == 3) {
            Collection<String> values = CoinKit.getKits();
            StringUtil.copyPartialMatches(args[args.length - 1], values, options);
        }

        if (args.length == 5) {
            List<String> values = List.of("true", "false");
            StringUtil.copyPartialMatches(args[args.length - 1], values, options);
        }

        return options;
    }
}
