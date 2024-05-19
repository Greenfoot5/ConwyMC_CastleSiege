package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.gui.KitGUIs;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.gui.Gui;
import me.huntifi.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows the player to select a kit
 */
public class KitCommand implements TabExecutor {

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
            Messenger.sendError("Console cannot select kits!", sender);
            return true;
        }

        assert sender instanceof Player;
        Player p = (Player) sender;

        if (!MapController.getPlayers().contains(p.getUniqueId())) {
            Messenger.sendError("You must be on a team to select a kit!", sender);
            return true;
        }

        Gui gui;
        if (args.length == 0) {
            // No arguments passed -> open kit selector GUI
            gui = KitGUIs.getGUI(null, p);
        } else {
            gui = KitGUIs.getGUI(args[0], p);
        }

        if (gui != null)
        {
            gui.open(p);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(KitGUIs.OPTIONS), options);
            return options;
        }

        return null;
    }
}
