package me.huntifi.castlesiege.commands.mojang;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;


public class WhoisCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot use /whois!");
            return true;
        }

        Player p = (Player) sender;
        OfflinePlayer target = (OfflinePlayer) p;
        if (args.length == 1) {
            target = Bukkit.getOfflinePlayer(args[0]); //get target player specified in arg

            if (target == null) { //if target does not exist/is not online
                Messenger.sendError("Could not find player: "  + ChatColor.RED + args[0]+".", p);
                return true;
            }
        }

        String innerMessage = (target == p) ? "Your " : target.getName()+"'s ";
        if (target == null) { p.sendMessage(ChatColor.RED + "No player found!"); return true; }
        p.sendMessage(ChatColor.DARK_PURPLE + " --------- " + innerMessage + "name history --------- ");
        try {
            PreviousPLayerNameEntry.PreviousPlayerNameEntry[] previousNames = NameLookup.getPlayerPreviousNames(target.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (PreviousPLayerNameEntry.PreviousPlayerNameEntry entry : previousNames) {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Name: " + ChatColor.YELLOW + entry.getPlayerName());
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Time of change: " + ChatColor.YELLOW + new Date(entry.getChangeTime()));
                    }
                    p.sendMessage(ChatColor.DARK_PURPLE + "-----------------------------------");
                }
            }.runTaskAsynchronously(Main.plugin);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        return true;
    }
}
