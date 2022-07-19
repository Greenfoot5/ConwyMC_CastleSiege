package me.huntifi.castlesiege.commands.mojang;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

/**
 * Shows the name history of the requested player
 */
public class WhoisCommand implements CommandExecutor {

    /**
     * Checks if valid arguments were provided for the whois command.
     * Shows the name history of the requested player when this is the case.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(sender instanceof Player) && args.length == 0)
                    Messenger.sendError("Only players can check their own name history!", sender);
                else if (args.length > 1)
                    Messenger.sendError("Only one player's name history can be retrieved at a time!", sender);
                else
                    whoIs(sender, args);
            }
        }.runTaskAsynchronously(Main.plugin);

        return true;
    }

    /**
     * Shows the name history of the requested player.
     * @param sender Source of the command
     * @param args Either contains only the name of the requested player or is empty
     */
    private void whoIs(CommandSender sender, String[] args) {
        try {
            // Get the UUID for the requested name lookup
            String uuid;
            if (args.length == 0)
                uuid = ((Player) sender).getUniqueId().toString();
            else {
                String retrieved = NameLookup.getPlayerUUID(args[0]);
                // Check if a UUID was found
                if (retrieved == null || retrieved.isEmpty()) {
                    Messenger.sendError(String.format("The player %s does not currently exist!", args[0]), sender);
                    return;
                }
                uuid = retrieved.replaceAll("\"", "");
            }

            // Create the message header
            StringBuilder message = new StringBuilder();
            message.append(ChatColor.DARK_PURPLE).append(" --------- ")
                    .append(args.length == 0 ? "Your" : args[0] + "'s")
                    .append(" name history --------- \n");

            // Get the name history and add to the message
            PreviousPlayerNameEntry[] previousNames = NameLookup.getPlayerPreviousNames(uuid);
            for (PreviousPlayerNameEntry entry : previousNames) {
                if (entry.isPlayersInitialName()) {
                    message.append(ChatColor.LIGHT_PURPLE).append("Original Name: ")
                            .append(ChatColor.YELLOW).append(entry.getPlayerName()).append("\n");
                } else {
                    message.append(ChatColor.LIGHT_PURPLE).append("Name: ")
                            .append(ChatColor.YELLOW).append(entry.getPlayerName()).append("\n");
                    message.append(ChatColor.LIGHT_PURPLE).append("Time of change: ")
                            .append(ChatColor.YELLOW).append(new Date(entry.getChangeTime())).append("\n");
                }
            }

            // Add the message footer and send the name history message
            message.append(ChatColor.DARK_PURPLE).append(" ----------------------------------- ");
            sender.sendMessage(message.toString());

        } catch (IOException e) {
            Messenger.sendError("An error occurred while performing the command!\n" +
                    "Please contact staff if this issue persists.", sender);
            throw new RuntimeException(e);
        }
    }
}
