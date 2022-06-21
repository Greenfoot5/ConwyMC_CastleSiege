package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.LoadData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

/**
 * Selects a random kit for the user
 */
public class RandomKitCommand implements CommandExecutor {

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
            sender.sendMessage("Spectators cannot select kits!");
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;
            ArrayList<String> unlockedKits = LoadData.getAllUnlockedKits(p.getUniqueId());
            Random random = new Random();
            ((Player) sender).performCommand(unlockedKits.get(random.nextInt(unlockedKits.size())));
        }

        return true;
    }
}
