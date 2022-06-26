package me.huntifi.castlesiege.commands.staff.donations;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetKitCommand implements CommandExecutor {

    /**
     * Set a kit for a player.
     *
     * @param sender Source of the command
     * @param cmd    Command which was executed
     * @param label  Alias of the command which was used
     * @param args   Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            Messenger.sendError("Use: /setkit <player> <kit>", sender);
            return true;
        }

        // Assign the command parameters to variables
        Player player = Bukkit.getPlayer(args[0]);
        Kit kit = Kit.getKit(args[1]);

        // Ensure the parameters were valid
        if (player == null) {
            Messenger.sendError("Could not find player " + args[0], sender);
            return true;
        } else if (kit == null) {
            Messenger.sendError("Could not find kit " + args[1], sender);
            return true;
        }

        // Set the player's kit
        Messenger.sendInfo(String.format("You set %s's kit to %s", player.getName(), kit.name), sender);
        Messenger.sendInfo("Your kit was set by " + sender.getName(), player);
        kit.addPlayer(player.getUniqueId());

        return true;
    }
}
