package me.huntifi.castlesiege.commands.gameplay;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class SettingsCommand implements CommandExecutor {
    public static final HashMap<String, String[]> defaultSettings = new HashMap<String, String[]>(){{
        put("randomDeath", new String[]{"false", "true"});
        put("deathMessages", new String[]{"false", "true"});
        put("language", new String[]{"EnglishUK", "Pirate"});
        put("joinPing", new String[]{"false", "true"});
        put("statsBoard", new String[]{"false", "true"});
        put("customJoinLeaveMessages", new String[]{"true", "false"});
        put("joinLeaveMessages", new String[]{"true", "false"});
    }};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Messenger.sendError("Only players can perform this command!", sender);
            return true;
        }
        if (args.length < 1) {
            // List the current settings and what they do
            Messenger.sendInfo("Valid settings are: \n" +
                            ChatColor.GOLD + "randomDeath (true/false) - " + ChatColor.BLUE + "Each time you die, runs /random to give you a new random class\n" +
                            ChatColor.GOLD + "deathMessages (true/false) - " + ChatColor.BLUE + "View all death messages, not just your own\n" +
                            ChatColor.GOLD + "joinPing (true/false) - " + ChatColor.BLUE + "Get a ping sound when another player joins the server\n" +
                            ChatColor.GOLD + "statsBoard (true/false) - " + ChatColor.BLUE + "The scoreboard will show your current game stats instead of flag names\n" +
                            ChatColor.GOLD + "joinLeaveMessages (true/false) - " + ChatColor.BLUE + "Toggle join messages off/on\n" +
                            ChatColor.GOLD + "customJoinLeaveMessages (true/false) - " + ChatColor.BLUE + "Toggle donator join messages off/on" ,
                    sender);
            return true;
        }
        if (defaultSettings.get(args[0]) == null) {
            Messenger.sendError("\"" + args[0] + "\" isn't a setting.", sender);
            Messenger.sendInfo("Valid settings are: \n" +
                            ChatColor.GOLD + "randomDeath (true/false) - " + ChatColor.BLUE + "Each time you die, runs /random to give you a new random class\n" +
                            ChatColor.GOLD + "deathMessages (true/false) - " + ChatColor.BLUE + "View all death messages, not just your own\n" +
                            ChatColor.GOLD + "joinPing (true/false) - " + ChatColor.BLUE + "Get a ping sound when another player joins the server\n" +
                            ChatColor.GOLD + "statsBoard (true/false) - " + ChatColor.BLUE + "The scoreboard will show your current game stats instead of flag names\n" +
                            ChatColor.GOLD + "JoinLeaveMessages (true/false) - " + ChatColor.BLUE + "Toggle join messages off/on\n" +
                            ChatColor.GOLD + "customJoinLeaveMessages (true/false) - " + ChatColor.BLUE + "Toggle donator join messages off/on " ,
                    sender);
            return true;
        }

        String setting = args[0];
        Player p = (Player) sender;
        if (args.length == 1) {
            String currentValue = ActiveData.getData(p.getUniqueId()).getSetting(setting);
            Messenger.sendInfo("Current value is " + currentValue + ". All possible values: " + Arrays.toString(defaultSettings.get(setting)), sender);
            return true;
        }

        String value = args[1];
        if (Arrays.asList(defaultSettings.get(setting)).contains(value)) {
            ActiveData.getData(p.getUniqueId()).setSetting(p.getUniqueId(), setting, value);
            Messenger.sendInfo("Setting Updated", sender);
        } else if (value.equals("reset")) {
            ActiveData.getData(p.getUniqueId()).setSetting(p.getUniqueId(), setting, defaultSettings.get(setting)[0]);
            Messenger.sendInfo(setting + " reset to " + defaultSettings.get(setting)[0], sender);
        } else {
            Messenger.sendError("Invalid Value. Possible values: " + Arrays.toString(defaultSettings.get(setting)), sender);
        }

        return true;
    }
}
