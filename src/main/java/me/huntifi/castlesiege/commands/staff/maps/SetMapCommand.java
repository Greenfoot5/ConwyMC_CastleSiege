package me.huntifi.castlesiege.commands.staff.maps;

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
 * Skips maps to another one
 */
public class SetMapCommand implements CommandExecutor {

	/**
	 * Skip ahead to a specified map in the cycle
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		String mapName = String.join(" ", args);
		if (MapController.getUnplayedMap(mapName) == null) {
			sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "That isn't a valid map or has already been played this restart!");
			return true;
		}

		if(sender instanceof Player) {
			Player p = (Player) sender;
			MapController.setMap(mapName);
			Bukkit.getServer().broadcastMessage(
					p.getName() + ChatColor.YELLOW + " has set the map to " + mapName + "!");


		} else if (sender instanceof ConsoleCommandSender) {
			MapController.setMap(mapName);
			Bukkit.getServer().broadcastMessage(
					ChatColor.DARK_AQUA + "Console" + ChatColor.YELLOW + " has set the map to " + mapName + "!");
		}

		return true;
	}
}
