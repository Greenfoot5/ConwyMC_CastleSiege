package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.kits.gui.FreeKitGUI;
import me.huntifi.castlesiege.kits.gui.UnlockedKitGUI;
import me.huntifi.castlesiege.kits.gui.SelectorKitGUI;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Allows the player to select a kit
 */
public class KitCommand implements CommandExecutor {

    private final HashMap<String, Inventory> kitGUI;
    private final HashMap<String, Inventory> selGUI;

    /**
     * Create instances of GUIs used by the kit command
     */
    public KitCommand() {
        // Kit GUIs
        kitGUI = new HashMap<>();
        kitGUI.put("free", new FreeKitGUI().getGui());
        kitGUI.put("unlocked", new UnlockedKitGUI().getGui());

        // Selector GUIs
        selGUI = new HashMap<>();
        selGUI.put("Rohan", new SelectorKitGUI().setTeam("Rohan"));
        selGUI.put("Uruk-hai", new SelectorKitGUI().setTeam("Uruk-hai"));
        selGUI.put("Thunderstone Guard", new SelectorKitGUI().setTeam("Thunderstone Guard"));
        selGUI.put("Cloud Crawlers", new SelectorKitGUI().setTeam("Cloud Crawlers"));
    }

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
        }

        Player p = (Player) sender;
        String team = MapController.getCurrentMap().getTeam(p.getUniqueId()).name;

        if (args.length == 0) {
            // No arguments passed -> open kit selector GUI
            Inventory gui = selGUI.get(team);
            if (gui == null) {
                gui = new SelectorKitGUI().setTeam(null);
            }
            p.openInventory(gui);
            return true;
        } else if (args.length == 1) {
            // One argument passed -> open sub-GUI
            return subGUI(p, team, args[0]);
        }

        return false;
    }

    /**
     * Opens the specified kits GUI
     * @param p The player for whom to open the GUI
     * @param team The name of the player's team
     * @param arg The specified kit GUI
     * @return true if a valid sub-GUI was specified, false otherwise
     */
    private boolean subGUI(Player p, String team, String arg) {
        if (arg.equalsIgnoreCase("free") || arg.equalsIgnoreCase("unlocked")) {
            p.openInventory(kitGUI.get(arg.toLowerCase()));
            return true;
        } else if (arg.equalsIgnoreCase("team") || arg.equalsIgnoreCase(team)) {
            p.sendMessage(ChatColor.DARK_RED + "This feature is currently unavailable!");
            // p.openInventory(kitGUI.get(team));
            return true;
        }

        return false;
    }
}
