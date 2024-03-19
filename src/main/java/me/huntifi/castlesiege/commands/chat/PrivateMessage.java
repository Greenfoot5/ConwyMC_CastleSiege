package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
			Messenger.sendError( "Could not find player: <red>" + args[0], s);
			return true;
		} else if (Objects.equals(s, r)) {
			Messenger.sendWarning("You are not a clown. You are the entire circus.", s);
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

		s.sendMessage(Component.text("To ").color(NamedTextColor.GOLD)
				.append(getUsername(r))
				.append(Component.text(": ").color(NamedTextColor.GOLD))
				.append(Component.text(m).color(NamedTextColor.DARK_AQUA)));
		r.sendMessage(Component.text("From ").color(NamedTextColor.GOLD)
				.append(getUsername(s))
				.append(Component.text(": ").color(NamedTextColor.GOLD))
				.append(Component.text(m).color(NamedTextColor.DARK_AQUA)));

		// Set last message sender
		lastSender.put(r, s);
	}

	/**
	 * Get the team color of the command sender
	 * @param s The command sender to get the team color for
	 * @return The team color of a player or white
	 */
	private Component getUsername(CommandSender s) {
		if (s instanceof Player) {
			Player p = (Player) s;
			return NameTag.username(p);
		}
		return Component.text(s.getName()).color(NamedTextColor.WHITE);
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
