package me.huntifi.castlesiege.commands.chat;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Sends a private message to a player
 */
public class PrivateMessage implements CommandExecutor {

	private static final HashMap<UUID, UUID> lastSender = new HashMap<>();

	/**
	 * Send the provided arguments as a private message to the first argument (player)
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return false if too few arguments are provided, true otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Command needs a player and a message
		if (args.length < 2) {
			return false;
		}

		// Separate sender, target, and message
		Player p = (Player) sender;
		Player t = Bukkit.getPlayer(args[0]);
		String m = String.join(" ", args).split(" ", 2)[1];

		// Make sure that a correct recipient is supplied
		// Cannot send message to yourself
		if (t == null) {
			p.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
			return true;
		} else if (Objects.equals(p, t)) {
			p.sendMessage(ChatColor.RED + "You are not a clown. You are the entire circus.");
			return true;
		}

		// Send the message
		sendMessage(p, t, m);

		return true;
	}

	/**
	 * Privately send a message to a player
	 * @param p The sender of the message
	 * @param t The receiver of the message
	 * @param m The message that is sent
	 */
	public void sendMessage(Player p, Player t, String m) {
		// Get team colors
		Enum<ChatColor> pColor = MapController.getCurrentMap().getTeam(p.getUniqueId()).primaryChatColor;
		Enum<ChatColor> tColor = MapController.getCurrentMap().getTeam(t.getUniqueId()).primaryChatColor;

		// Send messages
		p.sendMessage(String.format("%sTo %s%s%s: %s%s",
				ChatColor.GOLD, tColor, t.getName(), ChatColor.GOLD, ChatColor.GRAY, m));
		t.sendMessage(String.format("%sFrom %s%s%s: %s%s",
				ChatColor.GOLD, pColor, p.getName(), ChatColor.GOLD, ChatColor.GRAY, m));

		// Set last message sender
		lastSender.put(t.getUniqueId(), p.getUniqueId());
	}

	/**
	 * Get the unique id of the last player to send a message to the recipient
	 * @param uuid The unique id of the recipient
	 * @return The unique id of the last sender, null if no previous message received
	 */
	public UUID getLastSender(UUID uuid) {
		return lastSender.get(uuid);
	}
}
