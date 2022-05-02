package me.huntifi.castlesiege.commands.staff;

import me.huntifi.castlesiege.database.ActiveData;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Skips to the next map
 */
public class NextMapCommand implements CommandExecutor {

	/**
	 * Skip to the next map
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Bukkit.getServer().broadcastMessage(
					p.getDisplayName() + ChatColor.YELLOW + " has skipped to the next map!");
			MapController.endMap();

		} else if (sender instanceof ConsoleCommandSender) {
			Bukkit.getServer().broadcastMessage(
					ChatColor.DARK_AQUA + "Console" + ChatColor.YELLOW + " has skipped to the next map!");
			MapController.endMap();
		}

		return true;
	}
}
