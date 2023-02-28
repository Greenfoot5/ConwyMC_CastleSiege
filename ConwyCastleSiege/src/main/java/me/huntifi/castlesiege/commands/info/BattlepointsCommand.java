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

import java.text.DecimalFormat;

/**
 * A command to check your own battlepoints
 */
public class BattlepointsCommand implements CommandExecutor {

    /**
     * Gets the battlepoints of the player
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

        if (args.length == 0) { // No player was specified
            Player player = (Player) sender;
            PlayerData data = ActiveData.getData(player.getUniqueId());
            Messenger.sendInfo("Your Battlepoints (BP): " + ChatColor.DARK_AQUA + new DecimalFormat("0.00").format(data.getBattlepoints()), sender);
            Messenger.sendInfo("Battlepoints can be used to use purchased kits during a game", player);
        } else {
            Player player = Bukkit.getPlayer(args[0]); //get target player specified in arg
            if (player == null) { //if target does not exist/is not online
                Messenger.sendError("Could not find player: " + ChatColor.RED + args[0], sender);
                return true;
            }

            PlayerData data = ActiveData.getData(player.getUniqueId());
            Messenger.sendInfo(player.getName() + "'s Battlepoints (BP): " + ChatColor.DARK_AQUA + new DecimalFormat("0.00").format(data.getBattlepoints()), sender);
        }
        return true;
    }
}
