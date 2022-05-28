package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message);
    }

    /**
     * Broadcasts an error message to everyone on the server
     * @param message The error message
     */
    public static void broadcastError(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.DARK_RED + message);
    }

    /**
     * Sends a tip message to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendTip(String message, CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Broadcasts a tip message to everyone on the server
     * @param message The tip message
     */
    public static void broadcastTip(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    /**
     * Sends a message about a secret to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendSecret(String message, CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Broadcasts a message about a secret to everyone on the server
     * @param message The message to send
     */
    public static void broadcastSecret(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[S] " + ChatColor.YELLOW + message);
    }

    /**
     * Sends a lore message to a user
     * @param message The message to send
     * @param sender Who to send the message to
     */
    public static void sendLore(String message, CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[l] " + ChatColor.BLUE + message);
    }

    /**
     * Broadcasts a lore message to everyone
     * @param message The message to send
     */
    public static void broadcastLore(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[l] " + ChatColor.BLUE + message);
    }
}
