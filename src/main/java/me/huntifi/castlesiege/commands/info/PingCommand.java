package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player their current ping
 */
public class PingCommand implements CommandExecutor {

	/**
	 * Print the ping of the player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof ConsoleCommandSender && args.length == 0) {
			Messenger.sendError("Console cannot use <yellow>/ping</yellow>!", sender);
			return true;
		}

		Player t;
		if (args.length == 0) { // No player was specified
			t = (Player) sender;
		} else {
			t = Bukkit.getPlayer(args[0]); //get target player specified in arg
			if (t == null) { //if target does not exist/is not online
				Messenger.sendError("Could not find player: <red>" + args[0], sender);
				return true;
			}
		}

		String innerMessage = args.length == 0 ? "Your ": t.getName() + "'s ";
		Messenger.sendInfo(innerMessage + "ping is <aqua>" + getPing(t) + "</aqua>ms.", sender);
		return true;
	}

	/**
	 * Gets the ping of a player
	 * @param player The player for whom to get the ping
	 * @return The ping of the player
	 */
	private int getPing(Player player) {
		int ping = -1;
		try {
			ping = player.getPing();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ping;
	}
}
