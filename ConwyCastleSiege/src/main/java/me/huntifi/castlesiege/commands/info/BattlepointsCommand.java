package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BattlepointsCommand implements CommandExecutor {

    /**
     * Print the ping of the player
     *
     * @param sender Source of the command
     * @param cmd    Command which was executed
     * @param label  Alias of the command which was used
     * @param args   Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender && args.length == 0) {
            sender.sendMessage("Console cannot use /battlepoints!");
            return true;
        }

        Player t;
        if (args.length == 0) { // No player was specified
            Player p = (Player) sender;
            PlayerData data = ActiveData.getData(p.getUniqueId());
            Messenger.sendInfo(ChatColor.DARK_AQUA + "Your Battlepoints (BP): " + ChatColor.BLUE + data.getBattlepoints(), sender);
            Messenger.sendInfo("Battlepoints can be used to use purchased kits during a game", p);
        } else {
            t = Bukkit.getPlayer(args[0]); //get target player specified in arg
            if (t == null) { //if target does not exist/is not online
                Messenger.sendError("Could not find player: " + ChatColor.RED + args[0], sender);
                return true;
            }

            PlayerData data = ActiveData.getData(t.getUniqueId());
            Messenger.sendInfo(ChatColor.DARK_AQUA + t.getName() + "'s Battlepoints (BP): " + ChatColor.BLUE + data.getBattlepoints(), sender);
        }
        return true;
    }
}
