package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player our discord link
 */
public class DiscordCommand implements CommandExecutor {

	/**
	 * Print our discord link to the player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Messenger.sendInfo("The link to the" ChatColor.LIGHT_PURPLE + "[ConwyMC]" + ChatColor.AQUA +
				"discord is: " + ChatColor.DARK_PURPLE + "https://discord.gg/AUDqTpC", sender);
		return true;
	}
}
