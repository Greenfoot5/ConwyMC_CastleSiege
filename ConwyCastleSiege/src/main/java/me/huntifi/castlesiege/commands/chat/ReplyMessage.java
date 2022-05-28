
package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Replies to the last message sender
 */
public class ReplyMessage implements CommandExecutor {

	/**
	 * Send the provided arguments as a private message to last player to have messaged the command sender
	 * @param s Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return false if no message is provided, true otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Command needs a message
		if (args.length == 0) {
			return false;
		}

		// Get the recipient and check if they can be reached
		PrivateMessage pm = new PrivateMessage();
		CommandSender r = pm.getLastSender(s);
		if (r == null) {
			Messenger.sendError("Nobody has messaged you!", s);
			return true;
		} else if (r instanceof Player && !((Player) r).isOnline()) {
			Messenger.sendError("This player is no longer online!", s);
			return true;
		}

		// Send the message
		String m = String.join(" ", args);
		pm.sendMessage(s, r, m);
		return true;
	}
}
