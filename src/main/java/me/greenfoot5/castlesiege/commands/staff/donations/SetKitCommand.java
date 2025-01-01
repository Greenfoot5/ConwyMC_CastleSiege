package me.greenfoot5.castlesiege.commands.staff.donations;

import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.conwymc.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Sets the kit for a player
 */
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
        if (args.length == 1) {
            // Only a player was specified -> set random kit
            setRandom(sender, args[0]);
        } else if (args.length == 2) {
            // Both a player and kit were specified -> set specified kit
            setSpecific(sender, args[0], args[1]);
        } else {
            // An invalid amount of parameters was provided
            Messenger.sendError("Use: /setkit <player> [kit]", sender);
        }

        return true;
    }

    /**
     * Set a specific kit for a player
     * @param sender Source of the command
     * @param playerName Name of the player
     * @param kitName Name of the kit
     */
    private void setSpecific(CommandSender sender, String playerName, String kitName) {
        // Get the objects corresponding to the parameters
        Player player = Bukkit.getPlayer(playerName);
        Kit kit = Kit.getKit(kitName);

        // Ensure the parameters were valid
        if (player == null) {
            Messenger.sendError("Could not find player " + playerName, sender);
            return;
        } else if (kit == null) {
            Messenger.sendError("Could not find kit " + kitName, sender);
            return;
        }

        // Set the player's kit
        Messenger.sendInfo(String.format("You set %s's kit to %s", player.getName(), kit.name), sender);
        Messenger.sendInfo("Your kit was set by " + sender.getName(), player);
        kit.addPlayer(player.getUniqueId(), false);
    }

    /**
     * Set a random kit for a player
     * @param sender Source of the command
     * @param playerName Name of the player
     */
    private void setRandom(CommandSender sender, String playerName) {
        // Get the player object
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            Messenger.sendError("Could not find player " + playerName, sender);
            return;
        }

        // Set the player's kit
        Messenger.sendInfo(String.format("You set %s's kit to a random kit", player.getName()), sender);
        Messenger.sendInfo("Your kit was set by " + sender.getName(), player);
        player.performCommand("random");
    }
}
