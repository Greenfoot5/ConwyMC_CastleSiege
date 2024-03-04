package me.huntifi.castlesiege.commands.staff.punishments;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Kicks all players from the server
 */
public class KickAll implements CommandExecutor {

	/**
	 * Kicks all players
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true if a player is specified, false otherwise
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

		String message = "";
		if (args.length > 1) {
			message = " for: " + String.join(" ", args);
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kick(MiniMessage.miniMessage().deserialize("<red>You were kicked by <white>" + sender.getName()
					+ "</white>" + message));
		}

		return true;
	}
}