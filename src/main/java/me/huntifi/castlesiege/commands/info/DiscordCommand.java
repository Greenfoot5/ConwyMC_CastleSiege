package me.huntifi.castlesiege.commands.info;

import me.huntifi.conwymc.util.Messenger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
		Messenger.sendInfo("The link to the <light_purple>[ConwyMC]</light_purple> " +
				"discord is: <yellow><click:open_url:https://discord.gg/AUDqTpC>https://discord.gg/AUDqTpC</click></yellow>", sender);
		return true;
	}
}
