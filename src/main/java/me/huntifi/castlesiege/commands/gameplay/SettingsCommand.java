package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.data_types.CSPlayerData;
import me.huntifi.castlesiege.data_types.CSSetting;
import me.huntifi.castlesiege.database.CSActiveData;
import me.huntifi.castlesiege.maps.Scoreboard;
import me.huntifi.conwymc.data_types.Setting;
import me.huntifi.conwymc.gui.Gui;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
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

/**
 * Allows players to view and edit their Castle Siege settings
 */
public class SettingsCommand implements TabExecutor {
    private static final HashMap<UUID, Gui> guis = new HashMap<>();
    public static final Setting[] CS_SETTINGS = CSSetting.generateSettings();


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
        CSPlayerData data = CSActiveData.getData(uuid);
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

        for (int i = 0; i < CS_SETTINGS.length; i++) {
            Setting setting = new Setting(CS_SETTINGS[i]);
            String currentValue = CSActiveData.getData(player).getSetting(setting.key);
            String nextValue = setting.values[(Arrays.asList(setting.values).indexOf(currentValue) + 1) % setting.values.length];
            String command = String.format("settings %s %s", setting.key, nextValue);
            List<Component> lore = new ArrayList<>(setting.itemLore);
            lore.add(Messenger.mm.deserialize("<color:#87cbf8>Current Value:</color> <dark_aqua>" + currentValue + "</dark_aqua>"));

            gui.addItem(setting.displayName, setting.material, lore, i, command, false);
        }
    }

    private static List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (Setting setting : CS_SETTINGS) {
            keys.add(setting.key);
        }
        return keys;
    }

    /**
     * @param name The setting to get
     * @return The player's value, or the default if not set, or null if no setting exists
     */
    public static Setting getSetting(String name) {
        for (Setting setting : CS_SETTINGS) {
            if (setting.displayName.content().equals(name) || Objects.equals(setting.key, name))
                return setting;
        }
        return null;
    }
}
