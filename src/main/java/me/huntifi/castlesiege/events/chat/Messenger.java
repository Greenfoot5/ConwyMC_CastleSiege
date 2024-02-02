package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A class to handle sending messages to make the consistent
 */
public class Messenger {
    /**
     * Sends an error message to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendError(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.DARK_RED.toString());
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message);
    }

    public static void sendActionError(String message, @NotNull Player sender) {
        message = message.replaceAll("§r", ChatColor.DARK_RED.toString());
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message));
    }

    public static void sendWarning(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.RED.toString());
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.RED + message);
    }

    public static void broadcastWarning(String message) {
        message = message.replaceAll("§r", ChatColor.RED.toString());
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[!] " + ChatColor.RED + message);
    }

    /**
     * Sends a tip message to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendTip(String message, CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.AQUA.toString());
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Broadcasts a tip message to everyone on the server
     * @param message The tip message
     */
    public static void broadcastTip(String message) {
        message = message.replaceAll("§r", ChatColor.AQUA.toString());
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Sends a message about a secret to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendSecret(String message, CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.YELLOW.toString());
        sender.sendMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Broadcasts a message about a secret to everyone on the server
     * @param message The message to send
     */
    public static void broadcastSecret(String message) {
        message = message.replaceAll("§r", ChatColor.YELLOW.toString());
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Sends a success message to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendSuccess(String message, CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.GREEN.toString());
        sender.sendMessage(ChatColor.GOLD + "[+] " + ChatColor.GREEN + message);
    }

    /**
     * Sends a success message to a user via action message
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendActionSuccess(String message, @NotNull Player sender) {
        message = message.replaceAll("§r", ChatColor.GREEN.toString());
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[+] " + ChatColor.GREEN + message));
    }

    /**
     * Broadcasts a success message to everyone
     * @param message The message to send
     */
    public static void broadcastSuccess(String message) {
        message = message.replaceAll("§r", ChatColor.GREEN.toString());
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[+] " + ChatColor.GREEN + message);
    }

    public static void sendInfo(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.BLUE.toString());
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message);
    }

    public static void sendInfo(String message, @NotNull Player sender, int maximumLevel) {
        message = message.replaceAll("§r", ChatColor.BLUE.toString());
        PlayerData data = ActiveData.getData(sender.getUniqueId());
        if (data.getLevel() <= maximumLevel || data.getSetting("alwaysInfo").equals("true")) {
            sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message);
        }
    }

    public static void sendActionInfo(String message, @NotNull Player sender) {
        message = message.replaceAll("§r", ChatColor.BLUE.toString());
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message));
    }

    public static void broadcastInfo(String message) {
        message = message.replaceAll("§r", ChatColor.BLUE.toString());
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.BLUE + message);
    }

    public static void sendBounty(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.YELLOW.toString());
        sender.sendMessage(ChatColor.GOLD + "[B] " + ChatColor.YELLOW + message);
    }

    public static void broadcastPaidBounty(String payee, String bountied, int amount, int total) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + payee + ChatColor.YELLOW + " added " + amount + " to " + ChatColor.GOLD + bountied
                + ChatColor.YELLOW + "'s bounty! The total is "
                + ChatColor.GOLD + total + ChatColor.YELLOW + "!");
    }

    public static void broadcastKillStreakBounty(String bountied, int kills, int total) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " has reached a " + ChatColor.AQUA + kills
                + ChatColor.YELLOW + " kill streak! Their bounty has increased to " + ChatColor.GOLD + total + ChatColor.YELLOW + "!");
    }

    public static void broadcastBountyClaimed(String bountied, String killer, String assistant, int amount) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " was killed by "
                + killer + ChatColor.YELLOW + " and " + assistant + ChatColor.YELLOW +  "! They shared the "
                + ChatColor.GOLD + amount + ChatColor.YELLOW + " coin bounty between them.");
    }

    public static void broadcastBountyClaimed(String bountied, String killer, int amount) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " was killed by "
                + killer + ChatColor.YELLOW + " and they claimed the "
                + ChatColor.GOLD + amount + ChatColor.YELLOW + " coin bounty!");
    }

    public static void requestInput(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.LIGHT_PURPLE.toString());
        sender.sendMessage(ChatColor.GOLD + "[_] " + ChatColor.LIGHT_PURPLE + message);
    }

    /**
     * Broadcasts a cursed message to everyone
     * @param message The message to send
     */
    public static void broadcastCurse(String message) {
        message = message.replaceAll("§r", ChatColor.RED.toString());
        Main.plugin.getServer().broadcastMessage(String.format(ChatColor.GOLD + "[☠] " +
                ChatColor.RED + message));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param sender Who to send the message to
     */
    public static void sendCurse(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.RED.toString());
        sender.sendMessage(String.format(ChatColor.GOLD + "[☠] " +
                ChatColor.RED + message));
    }

    /**
     * Broadcasts a cursed end message to everyone
     * @param message The message to send
     */
    public static void broadcastCurseEnd(String message) {
        message = message.replaceAll("§r", ChatColor.GREEN.toString());
        Main.plugin.getServer().broadcastMessage(String.format(ChatColor.GOLD + "[☠] " +
                ChatColor.GREEN + message));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param sender Who to send the message to
     */
    public static void sendCurseEnd(String message, @NotNull CommandSender sender) {
        message = message.replaceAll("§r", ChatColor.GREEN.toString());
        sender.sendMessage(String.format(ChatColor.GOLD + "[☠] " +
                ChatColor.GREEN + message));
    }
}
