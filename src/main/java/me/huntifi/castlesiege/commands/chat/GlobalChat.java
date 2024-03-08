package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        String OC = message;

        if (message.isEmpty()) {
            // No arguments were provided
            if (sender instanceof Player) {
                Player p = (Player) sender;
                TeamChat.removePlayer(p.getUniqueId());
                StaffChat.removePlayer(p.getUniqueId());
                Messenger.sendInfo("You are now talking in global-chat!", p);
            } else {
                return false;
            }

        } else {
            //AsyncChatEvent event = new AsyncChatEvent(false, (Player) sender, Bukkit.getServer(), message, OC);
            //Bukkit.getPluginManager().callEvent(event);
        }

        return true;
    }
}
