package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Allows the user to switch to global chat
 */
public class GlobalChat implements CommandExecutor {

    /**
     * Toggle global-chat mode if no arguments are provided.
     * Send the provided arguments as a chat message to all players.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return False if console a non-player did not provide arguments, true otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String message = String.join(" ", args);

        if (message.isEmpty()) {
            // No arguments were provided
            if (sender instanceof Player) {
                Player p = (Player) sender;
                TeamChat.removePlayer(p.getUniqueId());
                StaffChat.removePlayer(p.getUniqueId());
                p.sendMessage(ChatColor.DARK_AQUA + "You are now talking in global-chat!");
            } else {
                return false;
            }

        } else {
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(false, (Player) sender, message, Set.copyOf(Bukkit.getOnlinePlayers()));
            Bukkit.getPluginManager().callEvent(event);
        }

        return true;
    }
}
