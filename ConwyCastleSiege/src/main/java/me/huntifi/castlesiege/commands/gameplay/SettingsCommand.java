package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import me.huntifi.castlesiege.maps.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SettingsCommand implements CommandExecutor, Listener {
    public static final HashMap<String, String[]> defaultSettings = new HashMap<String, String[]>(){{
        put("randomDeath", new String[]{"false", "true"});
        put("deathMessages", new String[]{"false", "true"});
        // TODO: put("language", new String[]{"EnglishUK", "Pirate"});
        put("joinPing", new String[]{"false", "true"});
        put("statsBoard", new String[]{"false", "true"});
        put("woolmapTitleMessage", new String[]{"true", "false"});
        put("showBattlepoints", new String[]{"true", "false"});
        put("alwaysInfo", new String[]{"false", "true"});
    }};

    private static final HashMap<HumanEntity, Gui> guis = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Only players can perform this command!", sender);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            // Register and open a settings GUI for the player
            Gui gui = new Gui(ChatColor.GOLD + "Settings", 1);
            guis.put(player, gui);

            for (String setting : defaultSettings.keySet())
                setGuiItem(player, setting);

            Main.plugin.getServer().getPluginManager().registerEvents(gui, Main.plugin);
            gui.open(player);
            return true;
        }

        String setting = args[0];
        if (defaultSettings.get(setting) == null) {
            Messenger.sendError("\"" + setting + "\" isn't a setting.", sender);
            Messenger.sendInfo("Valid settings are: \n" +
                            ChatColor.GOLD + "randomDeath (true/false) - " + ChatColor.BLUE + "Each time you die, runs /random to give you a new random class\n" +
                            ChatColor.GOLD + "deathMessages (true/false) - " + ChatColor.BLUE + "View all death messages, not just your own\n" +
                            ChatColor.GOLD + "joinPing (true/false) - " + ChatColor.BLUE + "Get a ping sound when another player joins the server\n" +
                            ChatColor.GOLD + "woolmapTitleMessage (true/false) - " + ChatColor.BLUE + "Shows the title message related to the wool map\n" +
                            ChatColor.GOLD + "showBattlepoints (true/false) - " + ChatColor.BLUE + "Shows your battlepoints in the flags scoreboard\n" +
                            ChatColor.GOLD + "statsBoard (true/false) - " + ChatColor.BLUE + "The scoreboard will show your current game stats instead of flag names",
                    sender);
            return true;
        }

        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);
        if (args.length == 1) {
            String currentValue = data.getSetting(setting);
            Messenger.sendInfo("Current value is " + currentValue + ". All possible values: " + Arrays.toString(defaultSettings.get(setting)), sender);
            return true;
        }

        String value = args[1];
        if (Arrays.asList(defaultSettings.get(setting)).contains(value)) {
            data.setSetting(uuid, setting, value);

            if (args[0].equalsIgnoreCase("statsBoard") || args[0].equalsIgnoreCase("showBattlepoints"))
                Scoreboard.clearScoreboard(player);

            if (guis.containsKey(player))
                setGuiItem(player, setting);
            else
                Messenger.sendInfo("Setting Updated", player);

        } else if (value.equals("reset")) {
            data.setSetting(uuid, setting, defaultSettings.get(setting)[0]);
            Messenger.sendInfo(setting + " reset to " + defaultSettings.get(setting)[0], sender);
        } else {
            Messenger.sendError("Invalid Value. Possible values: " + Arrays.toString(defaultSettings.get(setting)), sender);
        }

        return true;
    }

    /**
     * Set an item in a player's settings GUI.
     * @param player The player for whom to set an item
     * @param setting The setting which the item represents
     */
    private void setGuiItem(Player player, String setting) {
        Gui gui = guis.get(player);

        String currentValue = ActiveData.getData(player.getUniqueId()).getSetting(setting);
        String itemName = String.format("%s%s: %s", ChatColor.GOLD, setting, currentValue);

        String[] options = defaultSettings.get(setting);
        String nextValue = options[(Arrays.asList(options).indexOf(currentValue) + 1) % options.length];
        String command = String.format("settings %s %s", setting, nextValue);

        switch (setting) {
            case "randomDeath":
                gui.addItem(itemName, Material.COOKIE, Collections.singletonList(
                        ChatColor.BLUE + "Each time you die, runs /random to give you a new random class"),
                        0, command, false);
                break;
            case "deathMessages":
                gui.addItem(itemName, Material.OAK_SIGN, Collections.singletonList(
                        ChatColor.BLUE + "View all death messages, not just your own"),
                        1, command, false);
                break;
            case "joinPing":
                gui.addItem(itemName, Material.NOTE_BLOCK, Collections.singletonList(
                                ChatColor.BLUE + "Get a ping sound when another player joins the server"),
                        2, command, false);
                break;
            case "statsBoard":
                gui.addItem(itemName, Material.DIAMOND, Collections.singletonList(
                                ChatColor.BLUE + "The scoreboard will show your current game stats instead of flag names"),
                        3, command, false);
                break;
            case "woolmapTitleMessage":
                gui.addItem(itemName, Material.PAPER, Collections.singletonList(
                                ChatColor.BLUE + "Disable the Title bar message related to the Wool-map"),
                        4, command, false);
                break;
            case "showBattlepoints":
                gui.addItem(itemName, Material.BLUE_GLAZED_TERRACOTTA, Collections.singletonList(
                                ChatColor.BLUE + "Shows your battlepoints in the flags scoreboard"),
                        5, command, false);
                break;
            case "alwaysInfo":
                gui.addItem(itemName, Material.BLUE_WOOL, Collections.singletonList(
                                ChatColor.BLUE + "Always display level dependent info messages"),
                        6, command, false);
                break;
        }
    }

    /**
     * Unregister the player's settings GUI when they close it.
     * @param event The event called when an inventory is closed.
     */
    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {
        if (guis.containsKey(event.getPlayer())) {
            HandlerList.unregisterAll(guis.get(event.getPlayer()));
            guis.remove(event.getPlayer());
        }
    }
}
