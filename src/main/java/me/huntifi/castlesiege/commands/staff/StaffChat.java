package me.huntifi.castlesiege.commands.staff;

import io.papermc.paper.chat.ChatRenderer;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Toggles staff-chat and sends a message to all staff members
 */
public class StaffChat implements CommandExecutor {

	private static final Collection<UUID> staffChatters = new ArrayList<>();

	/**
	 * Toggle staff-chat mode if no arguments are provided
	 * Send the provided arguments as a chat message to all staff members
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Console can't toggle staff-chat mode
		if (sender instanceof ConsoleCommandSender) {
			if (args.length == 0) {
				sender.sendMessage("Console cannot toggle staff-chat mode!");
				return false;
			} else {
				String message = String.join(" ", args);
				sendMessage(message);
			}

		} else {
			Player p = (Player) sender;
			if (args.length == 0) {
				toggleStaffChat(p);
			} else {
				sendMessage(p, String.join(" ", args));
			}
		}

		return true;
	}

	/**
	 * Send a message to all staff members from console
	 * @param m The message to send
	 */
	public static void sendMessage(String m) {
		String s = ChatColor.WHITE + "CONSOLE" + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + m;

		// Send the message to all staff members
		for (Player q : Bukkit.getOnlinePlayers()) {
			if (q.hasPermission("castlesiege.chatmod")) {
				q.sendMessage(s);
			}
		}
	}

	/**
	 * Send a message to all staff members
	 * @param p The player that sends the message
	 * @param m The message to send
	 */
	public static void sendMessage(Player p, String m) {
		String s = p.getDisplayName() + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + m;

		// Send the message to all staff members
		System.out.println(s);
		for (Player q : Bukkit.getOnlinePlayers()) {
			if (q.hasPermission("castlesiege.chatmod")) {
				q.sendMessage(s);
			}
		}
	}

	/**
	 * Get the staff-chat status of a player
	 * @param uuid The unique ID of the player
	 * @return Whether the player is in staff-chat mode
	 */
	public static boolean isStaffChatter(UUID uuid) {
		return staffChatters.contains(uuid);
	}

	/**
	 * Toggle the staff-chat status for a player
	 * @param p The player for whom to toggle staff-chat
	 */
	private void toggleStaffChat(Player p) {
		UUID uuid = p.getUniqueId();
		if (staffChatters.contains(uuid)) {
			staffChatters.remove(uuid);
			p.sendMessage(ChatColor.DARK_AQUA + "You are no longer talking in staff-chat!");
		} else {
			TeamChat.removePlayer(uuid);
			staffChatters.add(uuid);
			p.sendMessage(ChatColor.DARK_AQUA + "You are now talking in staff-chat!");
		}
	}

	/**
	 * Remove the player from the staff chatters
	 * @param uuid The unique ID of the player
	 */
	public static void removePlayer(UUID uuid) {
		staffChatters.remove(uuid);
	}
}
