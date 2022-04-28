
package me.huntifi.castlesiege.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Replies to the last message sender
 */
public class ReplyMessage implements CommandExecutor {

	/**
	 * Send the provided arguments as a private message to last player to have messaged the command sender
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return false if no message is provided, true otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Command needs a message
		if (args.length == 0) {
			return false;
		}

		// Get the sender and receiver
		PrivateMessage pm = new PrivateMessage();
		Player p = (Player) sender;
		UUID uuid = pm.getLastSender(p.getUniqueId());
		if (uuid == null) {
			p.sendMessage(ChatColor.DARK_RED + "Nobody has messaged you!");
			return true;
		}

		// Convert receiver to a player
		Player t = Bukkit.getPlayer(uuid);
		if (t == null) {
			p.sendMessage(ChatColor.DARK_RED + "This player is no longer online!");
			return true;
		}

		// Send the message
		String m = String.join(" ", args);
		pm.sendMessage(p, t, m);
		return true;
	}
}
