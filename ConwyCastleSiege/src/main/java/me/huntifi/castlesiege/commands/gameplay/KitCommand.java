package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.kits.gui.KitGui;
import me.huntifi.castlesiege.kits.gui.KitGuiController;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Allows the player to select a kit
 */
public class KitCommand implements CommandExecutor {

    /**
     * Opens the kit selector GUI for the command source if no arguments are passed
     * Opens the specific kits GUI for the command source if a sub-GUI is specified
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if no or a valid sub-GUI was specified, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Console cannot select kits!");
            return true;
        } else if (sender instanceof Player && MapController.isSpectator(((Player) sender).getUniqueId())) {
            Messenger.sendError("Spectators cannot select kits!", sender);
            return true;
        }

        assert sender instanceof Player;
        Player p = (Player) sender;
        if (args.length == 0) {
            // No arguments passed -> open kit selector GUI
            KitGuiController.get("selector").open(p);
        } else {
            // Arguments passed -> open specified GUI
            String category = String.join(" ", args);
            KitGui gui = get(p.getUniqueId(), category.toLowerCase());
            if (gui == null) {
                Messenger.sendError("Unknown category: " + ChatColor.RED + args[0], p);
            } else {
                gui.open(p);
            }
        }
        return true;
    }

    /**
     * Retrieve the specified kit GUI
     * @param uuid The unique id of the player who wants to open the GUI
     * @param category The category that should be opened
     * @return The specified GUI, or the player's team GUI if the category is "team"
     */
    private KitGui get(UUID uuid, String category) {
        if (category.equals("team")) {
            String team = MapController.getCurrentMap().getTeam(uuid).name;
            return KitGuiController.get(team.toLowerCase());
        }
        return KitGuiController.get(category);
    }
}
