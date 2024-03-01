package me.huntifi.castlesiege.events.chat;

import me.huntifi.castlesiege.data_types.PlayerData;
import me.huntifi.castlesiege.database.ActiveData;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

/**
 * A class to handle sending messages to make the consistent
 */
public class Messenger {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static void sendAction(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message);
        receiver.sendActionBar(msg);
    }

    public static void sendAction(Component msg, @NotNull CommandSender receiver) {
        receiver.sendActionBar(msg);
    }

    public static void send(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message);
        receiver.sendMessage(msg);
    }

    public static void send(Component msg, @NotNull CommandSender receiver) {
        receiver.sendMessage(msg);
    }

    /**
     * Sends an error message to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendError(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(DARK_RED);
        receiver.sendMessage(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    public static void sendActionError(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(DARK_RED);
        receiver.sendActionBar(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    public static void sendWarning(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendMessage(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    public static void broadcastWarning(String message) {
        Component msg = mm.deserialize(message).color(RED);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage( mm.deserialize("<gold>[!] </gold>").append(msg));
    }

    public static void sendActionWarning(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendActionBar(mm.deserialize("<gold>[!]</gold> ").append(msg));
    }

    /**
     * Sends a tip message to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendTip(String message, CommandSender receiver) {
        Component msg = mm.deserialize(message).color(AQUA);
        receiver.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    /**
     * Broadcasts a tip message to everyone on the server
     * @param message The tip message
     */
    public static void broadcastTip(String message) {
        Component msg = mm.deserialize(message).color(AQUA);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    /**
     * Sends a message about a secret to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendSecret(String message, CommandSender receiver) {
        Component msg = mm.deserialize(message).color(YELLOW);
        receiver.sendMessage(mm.deserialize("<gold>[\uD83D\uDDDD️]</gold> ").append(msg));
    }

    /**
     * Broadcasts a message about a secret to everyone on the server
     * @param message The message to send
     */
    public static void broadcastSecret(String message) {
        Component msg = mm.deserialize(message).color(YELLOW);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[\uD83D\uDDDD]</gold> ").append(msg));
    }

    /**
     * Sends a success message to a user
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendSuccess(String message, CommandSender receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(msg));
    }

    /**
     * Sends a success message to a user via action message
     * @param message The message to send
     * @param receiver Who to send the message to
     */
    public static void sendActionSuccess(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendActionBar(msg);
    }

    /**
     * Broadcasts a success message to everyone
     * @param message The message to send
     */
    public static void broadcastSuccess(String message) {
        Component msg = mm.deserialize(message).color(GREEN);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[+]</gold> ").append(msg));
    }

    public static void sendInfo(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(BLUE);
        receiver.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    public static void sendInfo(String message, @NotNull Player receiver, int maximumLevel) {
        Component msg = mm.deserialize(message).color(BLUE);
        PlayerData data = ActiveData.getData( receiver.getUniqueId());
        if (data.getLevel() <= maximumLevel || data.getSetting("alwaysInfo").equals("true")) {
            receiver.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
        }
    }

    public static void sendActionInfo(String message, @NotNull Player receiver) {
        Component msg = mm.deserialize(message).color(BLUE);
        receiver.sendActionBar(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    public static void broadcastInfo(String message) {
        Component msg = mm.deserialize(message).color(BLUE);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[i]</gold> ").append(msg));
    }

    public static void sendBounty(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(YELLOW);
        receiver.sendMessage(mm.deserialize("<gold>[⌖]</gold> ").append(msg));
    }

    public static void broadcastPaidBounty(String payee, String bountied, int amount, int total) {
        Component msg = mm.deserialize("<gold>[⌖]</gold> <yellow>" + payee + " added " + amount
                + " to " + bountied + "'s bounty! The new total is <GOLD>"
                + total + "</gold>!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastKillStreakBounty(String bountied, int kills, int total) {
        Component msg = mm.deserialize("<gold>[⌖]</gold> " + bountied + " has reached a <aqua>"
                + kills + "</aqua> kill streak! Their bounty has increased to <gold>" + total + "</gold>!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastBountyClaimed(String bountied, String killer, String assistant, int amount) {
        Component msg = mm.deserialize("<gradient:#F56545:#99201C>[⌖] " + bountied
                + " was killed by " + killer + " and " + assistant
                + "! They shared the <gold>" + amount + "</gold> coin bounty between them.");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void broadcastBountyClaimed(String bountied, String killer, int amount) {
        Component msg = mm.deserialize("<gradient:#F56545:#99201C>[⌖] " + bountied
                + " was killed by " + killer + " and they claimed the <gold>"
                + amount + "</gold> coin bounty!");
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(msg);
    }

    public static void requestInput(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(LIGHT_PURPLE);
        receiver.sendMessage(mm.deserialize("<gold>[_]</gold> ").append(msg));
    }

    /**
     * Broadcasts a cursed message to everyone
     * @param message The message to send
     */
    public static void broadcastCurse(String message) {
        Component msg = mm.deserialize(message).color(RED);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param receiver Who to send the message to
     */
    public static void sendCurse(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(RED);
        receiver.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Broadcasts a cursed end message to everyone
     * @param message The message to send
     */
    public static void broadcastCurseEnd(String message) {
        Component msg = mm.deserialize(message).color(GREEN);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    /**
     * Sends a cursed message to a specific user
     * @param message The message
     * @param receiver Who to send the message to
     */
    public static void sendCurseEnd(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize(message).color(GREEN);
        receiver.sendMessage(mm.deserialize("<gold>[☠]</gold> ").append(msg));
    }

    public static void sendHealing(String message, @NotNull CommandSender receiver) {
        receiver.sendMessage(mm.deserialize("<gradient:#FF1053:#F7ACCF>[❤] " + message));
    }
    
    public static void sendActionSpawn(String name, NamedTextColor primary, NamedTextColor secondary, @NotNull CommandSender receiver) {
        Component msg = Component.text("Spawning at: ").color(secondary)
                .append(Component.text(name).color(primary));
        receiver.sendActionBar(msg);
    }
    
    public static void sendActionHit(String playerHit, @NotNull CommandSender receiver) {
        Component msg = Component.text("Hit (" + playerHit + ")").color(AQUA);
        receiver.sendActionBar(msg);
    }

    public static void sendDuel(String message, @NotNull CommandSender receiver) {
        Component msg = mm.deserialize("<gradient:#48A9FE:#0BEEF9>" + message + "</gradient>");
        receiver.sendMessage(mm.deserialize("<gold>[⚔]</gold> ").append(msg));
    }

    public static void broadcastDuel(String message) {
        Component msg = mm.deserialize(message).color(BLUE);
        ForwardingAudience audience = Bukkit.getServer();
        audience.sendMessage(mm.deserialize("<gold>[⚔]</gold> ").append(msg));
    }
}
