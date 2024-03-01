package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import me.huntifi.castlesiege.maps.Scoreboard;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SettingsCommand implements TabExecutor {
    public static final HashMap<String, String[]> defaultSettings = new HashMap<>(){{
        put("randomDeath", new String[]{"false", "true"});
        put("deathMessages", new String[]{"false", "true"});
        // TODO: put("language", new String[]{"EnglishUK", "Pirate"});
        put("joinPing", new String[]{"false", "true"});
        put("statsBoard", new String[]{"false", "true"});
        put("woolmapTitleMessage", new String[]{"true", "false"});
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
            Gui gui = new Gui("<gold>Settings</gold>", 1, true);
            guis.put(player, gui);

            for (String setting : defaultSettings.keySet())
                setGuiItem(player, setting);

            gui.open(player);
            return true;
        }

        String setting = args[0];
        if (defaultSettings.get(setting) == null) {
            Messenger.sendError("<aqua>" + setting + "</aqua> isn't a setting.", sender);
            Messenger.sendInfo("Valid settings are: <br>" +
                            "<gold>randomDeath (true/false) - <blue>Each time you die, runs /random to give you a new random class<br>" +
                            "<gold>deathMessages (true/false) - <blue>View all death messages, not just your own<br>" +
                            "<gold>joinPing (true/false) - <blue>Get a ping sound when another player joins the server<br>" +
                            "<gold>woolmapTitleMessage (true/false) - <blue>Shows the title message related to the wool map<br>" +
                            "<gold>statsBoard (true/false) - <blue>The scoreboard will show your current game stats instead of flag names<br>" +
                            "<gold>alwaysInfo (false/true) - <blue>Shows info messages after you've reached the level required to hide them",
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

            if (args[0].equalsIgnoreCase("statsBoard"))
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            List<String> values = new ArrayList<>(defaultSettings.keySet());
            StringUtil.copyPartialMatches(args[0], values, options);
        }

        if (args.length == 2) {
            ArrayList<String> values = new ArrayList<>();
            Collections.addAll(values, defaultSettings.get(args[0]));
            values.add("reset");
            StringUtil.copyPartialMatches(args[1], values, options);
        }

        return options;
    }

    /**
     * Set an item in a player's settings GUI.
     * @param player The player for whom to set an item
     * @param setting The setting which the item represents
     */
    private void setGuiItem(Player player, String setting) {
        Gui gui = guis.get(player);

        String currentValue = ActiveData.getData(player.getUniqueId()).getSetting(setting);
        String itemName = String.format("<gold>%s: %s", setting, currentValue);

        String[] options = defaultSettings.get(setting);
        String nextValue = options[(Arrays.asList(options).indexOf(currentValue) + 1) % options.length];
        String command = String.format("settings %s %s", setting, nextValue);

        switch (setting) {
            case "randomDeath":
                gui.addItem(itemName, Material.COOKIE, Collections.singletonList(
                        "<blue>Each time you die, runs /random to give you a new random class"),
                        0, command, false);
                break;
            case "deathMessages":
                gui.addItem(itemName, Material.OAK_SIGN, Collections.singletonList(
                        "<blue>View all death messages, not just your own"),
                        1, command, false);
                break;
            case "joinPing":
                gui.addItem(itemName, Material.NOTE_BLOCK, Collections.singletonList(
                                "<blue>Get a ping sound when another player joins the server"),
                        2, command, false);
                break;
            case "statsBoard":
                gui.addItem(itemName, Material.DIAMOND, Collections.singletonList(
                                "<blue>The scoreboard will show your current game stats instead of flag names"),
                        3, command, false);
                break;
            case "woolmapTitleMessage":
                gui.addItem(itemName, Material.PAPER, Collections.singletonList(
                                "<blue>Displays a title message reminding you of the woolmap"),
                        4, command, false);
                break;
            case "alwaysInfo":
                gui.addItem(itemName, Material.BLUE_WOOL, Collections.singletonList(
                                "<blue>Always display level dependent info messages"),
                        6, command, false);
                break;
        }
    }
}
