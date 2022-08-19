package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * Sends a private message to a player
 */
public class PrivateMessage implements CommandExecutor {

	private static final HashMap<CommandSender, CommandSender> lastSender = new HashMap<>();

	/**
	 * Send the provided arguments as a private message to the first argument (player)
	 * @param s Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return false if too few arguments are provided, true otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Command needs a player and a message
		if (args.length < 2) {
			return false;
		}

		// Separate recipient, and message
		CommandSender r = args[0].equalsIgnoreCase("console") ?
				Bukkit.getServer().getConsoleSender() : Bukkit.getPlayer(args[0]);
		String m = String.join(" ", args).split(" ", 2)[1];

		// Make sure that a correct recipient is supplied
		// Cannot send message to yourself
		if (r == null) {
			Messenger.sendError( "Could not find player: " + ChatColor.RED + args[0], s);
			return true;
		} else if (Objects.equals(s, r)) {
			Messenger.sendWarning(ChatColor.RED + "You are not a clown. You are the entire circus.", s);
			return true;
		}

		// Send the message
		sendMessage(s, r, m);

		return true;
	}

	/**
	 * Privately send a message to a player
	 * @param s The sender of the message
	 * @param r The receiver of the message
	 * @param m The message that is sent
	 */
	public void sendMessage(CommandSender s, CommandSender r, String m) {
		// Check if the player is muted
		if (s instanceof Player && Mute.isMuted(((Player) s).getUniqueId())) {
			return;
		}

		// Get team colors
		String sColor = getTeamColor(s);
		String rColor = getTeamColor(r);

		// Send messages
		s.sendMessage(String.format("%sTo %s%s%s: %s%s",
				ChatColor.GOLD, rColor, r.getName(), ChatColor.GOLD, ChatColor.DARK_AQUA, m));
		r.sendMessage(String.format("%sFrom %s%s%s: %s%s",
				ChatColor.GOLD, sColor, s.getName(), ChatColor.GOLD, ChatColor.DARK_AQUA, m));

		// Set last message sender
		lastSender.put(r, s);
	}

	/**
	 * Get the team color of the command sender
	 * @param s The command sender to get the team color for
	 * @return The team color of a player or white
	 */
	private String getTeamColor(CommandSender s) {
		if (s instanceof Player) {
			Player p = (Player) s;
			return NameTag.color(p);
		}
		return ChatColor.WHITE.toString();
	}

	/**
	 * Get the last sender of a message to the recipient
	 * @param s The recipient
	 * @return The last sender, null if no previous message received
	 */
	public CommandSender getLastSender(CommandSender s) {
		return lastSender.get(s);
	}
}
