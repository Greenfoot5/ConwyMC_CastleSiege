package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message);
    }

    public static void sendActionError(String message, @NotNull Player sender) {
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message));
    }

    /**
     * Broadcasts an error message to everyone on the server
     * @param message The error message
     */
    public static void broadcastError(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + message);
    }

    public static void sendWarning(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.RED + message);
    }

    public static void broadcastWarning(String message) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[!] " + ChatColor.RED + message);
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

    public static void sendInfo(String message, @NotNull CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "[i] " + ChatColor.AQUA + message);
    }

    public static void broadcastPaidBounty(String payee, String bountied, int amount, int total) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + payee + ChatColor.YELLOW + " added " + amount + " to " + ChatColor.GOLD + bountied
                + ChatColor.YELLOW + "'s bounty! Their total bounty is "
                + ChatColor.GOLD + total + ChatColor.YELLOW + "!");
    }

    public static void broadcastKillstreakBounty(String bountied, int kills, int amount, int total) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " has achieved a kill streak of " + ChatColor.AQUA + kills
                + ChatColor.YELLOW + "! Their bounty has increased by " + amount + ". It's now at "
                + ChatColor.GOLD + total + ChatColor.YELLOW + "!");
    }

    public static void broadcastBountyClaimed(String bountied, String killer, String assistant, int amount) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " was killed by "
                + killer + ChatColor.YELLOW + " with some help from " + assistant + " and they have claimed "
                + ChatColor.GOLD + amount + ChatColor.YELLOW + " for the kill!");
    }

    public static void broadcastBountyClaimed(String bountied, String killer, int amount) {
        Main.plugin.getServer().broadcastMessage(ChatColor.GOLD + "[B] "
                + ChatColor.YELLOW + bountied + ChatColor.YELLOW + " was killed by "
                + killer + ChatColor.YELLOW + " and they have claimed "
                + ChatColor.GOLD + amount + ChatColor.YELLOW + " for the kill!");
    }
}
