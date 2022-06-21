package me.huntifi.castlesiege.commands.staff.donations;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.punishments.PunishmentTime;
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

import java.sql.SQLException;
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
        if (args.length < 5) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();

                try {

                    switch (args[1].toLowerCase()) {
                        case "add":

                            long duration = PunishmentTime.getDuration(args[3]);
                            if (duration == 0) {
                                PunishmentTime.wrongFormat(sender);
                                return;
                            }


                            if (KitList.getAllKits().contains(args[2])) {

                                if (args[4].equalsIgnoreCase("true")) {
                                    StoreData.addUnlockedKit(uuid, args[2], duration, true);

                                    Messenger.sendInfo("Successfully added " + args[2] + " to "
                                            + Bukkit.getPlayer(uuid).getName() + " for " + duration, sender);
                                } else if (args[4].equalsIgnoreCase("false")) {
                                    StoreData.addUnlockedKit(uuid, args[2], duration, false);

                                    Messenger.sendInfo("Successfully added " + args[2] + " to "
                                            + Bukkit.getPlayer(uuid).getName() + " for " + duration, sender);
                                } else {
                                    Messenger.sendError("Requires is donated true or false.", sender);
                                }

                            } else {
                                Messenger.sendError("An invalid kit was provided.", sender);
                            }

                            break;
                        case "remove":

                            if (KitList.getAllKits().contains(args[2])) {

                                StoreData.endUnlockedKit(uuid, args[2]);

                                Messenger.sendInfo("Successfully removed " + args[2] + " from "
                                        + Bukkit.getPlayer(uuid).getName(), sender);

                            } else {
                                Messenger.sendError("An invalid kit was provided.", sender);
                            }

                            break;
                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "The operation " + ChatColor.RED + args[1]
                                    + ChatColor.DARK_RED + " is not supported!");
                            sender.sendMessage(ChatColor.DARK_RED + "Please use one of the following: "
                                    + ChatColor.RED + "set, add, remove");
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
}
