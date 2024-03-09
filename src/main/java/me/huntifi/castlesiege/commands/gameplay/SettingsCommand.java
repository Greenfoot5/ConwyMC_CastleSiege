package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.data_types.Setting;
import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.gui.Gui;
import me.huntifi.castlesiege.maps.Scoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SettingsCommand implements TabExecutor {
    private static final HashMap<UUID, Gui> guis = new HashMap<>();
    public static final Setting[] SETTINGS = Setting.generateSettings();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Only players can perform this command!", sender);
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            // Register and open a settings GUI for the player
            Gui gui = new Gui(Component.text("Settings"), 1, true);
            guis.put(player.getUniqueId(), gui);

            setGuiItems(player.getUniqueId());

            gui.open(player);
            return true;
        }

        Setting setting = getSetting(args[0]);
        if (setting == null) {
            Messenger.sendError("<aqua>" + args[0] + "</aqua> isn't a setting.", sender);
            return true;
        }

        UUID uuid = player.getUniqueId();
        PlayerData data = ActiveData.getData(uuid);
        if (args.length == 1) {
            Messenger.sendInfo("Current value is <dark_aqua>" + data.getSetting(setting.key) + "</dark_aqua>. " +
                    "All possible values: <dark_aqua>" + Arrays.toString(setting.values), sender);
            return true;
        }

        String value = args[1];
        if (Arrays.asList(setting.values).contains(value)) {
            data.setSetting(uuid, setting.key, value);

            if (args[0].equalsIgnoreCase("scoreboard"))
                Scoreboard.clearScoreboard(player);

            if (guis.containsKey(player.getUniqueId()))
                setGuiItems(player.getUniqueId());
            else
                Messenger.sendInfo("Setting Updated", player);

        } else if (value.equals("reset")) {
            data.setSetting(uuid, setting.key, setting.values[0]);
            Messenger.sendInfo(setting + " reset to " + setting.values[0], sender);
        } else {
            Messenger.sendError("Invalid Value. Possible values: " + Arrays.toString(setting.values), sender);
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            List<String> values = new ArrayList<>(getKeys());
            StringUtil.copyPartialMatches(args[0], values, options);
        }

        if (args.length == 2) {
            ArrayList<String> values = new ArrayList<>();
            Collections.addAll(values, Objects.requireNonNull(getSetting(args[0])).values);
            values.add("reset");
            StringUtil.copyPartialMatches(args[1], values, options);
        }

        return options;
    }

    /**
     * Creates the GUI
     * @param player The player for whom to create the GUI
     */
    private void setGuiItems(UUID player) {
        Gui gui = guis.get(player);

        for (int i = 0; i < SETTINGS.length; i++) {
            Setting setting = new Setting(SETTINGS[i]);
            String currentValue = ActiveData.getData(player).getSetting(setting.key);
            String nextValue = setting.values[(Arrays.asList(setting.values).indexOf(currentValue) + 1) % setting.values.length];
            String command = String.format("settings %s %s", setting.key, nextValue);
            List<Component> lore = new ArrayList<>(setting.itemLore);
            lore.add(MiniMessage.miniMessage().deserialize("<color:#87cbf8>Current Value:</color> <dark_aqua>" + currentValue + "</dark_aqua>"));

            gui.addItem(setting.displayName, setting.material, lore, i, command, false);
        }
//
//
//
//        switch (setting.key) {
//            case "randomDeath":
//                gui.addItem(itemName, Material.COOKIE, Collections.singletonList(
//                        Component.text("Each time you die, runs /random to give you a new random class", NamedTextColor.BLUE)),
//                        0, command, false);
//                break;
//            case "deathMessages":
//                gui.addItem(itemName, Material.OAK_SIGN, Collections.singletonList(
//                        Component.text("View all death messages, not just your own", NamedTextColor.BLUE)),
//                        1, command, false);
//                break;
//            case "joinPing":
//                gui.addItem(itemName, Material.NOTE_BLOCK, Collections.singletonList(
//                        Component.text("Get a ping sound when another player joins the server", NamedTextColor.BLUE)),
//                        2, command, false);
//                break;
//            case "statsBoard":
//                gui.addItem(itemName, Material.DIAMOND, Collections.singletonList(
//                        Component.text("The scoreboard will show your current game stats instead of flag names", NamedTextColor.BLUE)),
//                        3, command, false);
//                break;
//            case "woolmapTitleMessage":
//                gui.addItem(itemName, Material.PAPER, Collections.singletonList(
//                        Component.text("Displays a title message reminding you of the woolmap", NamedTextColor.BLUE)),
//                        4, command, false);
//                break;
//            case "alwaysInfo":
//                gui.addItem(itemName, Material.BLUE_WOOL, Collections.singletonList(
//                        Component.text("Always display level dependent info messages", NamedTextColor.BLUE)),
//                        6, command, false);
//                break;
//        }
    }

    private static List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (Setting setting : SETTINGS) {
            keys.add(setting.key);
        }
        return keys;
    }

    public static Setting getSetting(String name) {
        for (Setting setting : SETTINGS) {
            if (setting.displayName.content().equals(name) || Objects.equals(setting.key, name))
                return setting;
        }
        return null;
    }
}
