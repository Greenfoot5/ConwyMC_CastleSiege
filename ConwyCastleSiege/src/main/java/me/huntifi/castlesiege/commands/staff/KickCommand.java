package me.huntifi.castlesiege.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Kicks a player from the server
 */
public class KickCommand implements CommandExecutor {

	/**
	 * Kicks a player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true if a player is specified, false otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			return false;
		}

		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + args[0]);
			return true;
		}

		String message = "";
		if (args.length > 1) {
			message = " for: " + String.join(" ", args).split(" ", 2)[1];
		}
		p.kickPlayer(ChatColor.RED + "You were kicked by " + ChatColor.WHITE + sender.getName()
				+ ChatColor.RED + message);

		return true;
	}
}
