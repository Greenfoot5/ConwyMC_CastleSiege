package me.huntifi.castlesiege.commands.mojang;

import me.huntifi.castlesiege.Main;
import me.huntifi.conwymc.util.Messenger;
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
            message.append("<dark_purple> <st>━━━━━━━━━</st> ")
                    .append(args.length == 0 ? "Your" : args[0] + "'s")
                    .append(" name history <st>━━━━━━━━━</st> \n");

            // Get the name history and add to the message
            PreviousPlayerNameEntry[] previousNames = NameLookup.getPlayerPreviousNames(uuid);
            for (PreviousPlayerNameEntry entry : previousNames) {
                if (entry.isPlayersInitialName()) {
                    message.append("<light_purple>Original Name: </light_purple><yellow>")
                            .append(entry.getPlayerName()).append("</yellow><br>");
                } else {
                    message.append("<light_purple>Name: </light_purple><yellow>")
                            .append(entry.getPlayerName()).append("</yellow><br>");
                    message.append("<light_purple>Time of change: </light_purple><yellow>")
                            .append(new Date(entry.getChangeTime())).append("</yellow><br>");
                }
            }

            // Add the message footer and send the name history message
            message.append("<dark_purple> <st>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</st> ");
            Messenger.send(message.toString(), sender);

        } catch (IOException e) {
            Messenger.sendError("An error occurred while performing the command! " +
                    "Please contact staff if this issue persists.", sender);
            throw new RuntimeException(e);
        }
    }
}
