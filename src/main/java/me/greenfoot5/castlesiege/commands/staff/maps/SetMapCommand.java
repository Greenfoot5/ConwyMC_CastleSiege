package me.greenfoot5.castlesiege.commands.staff.maps;

import me.greenfoot5.castlesiege.maps.MapController;
import me.greenfoot5.conwymc.util.Messenger;
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
			Messenger.sendError("That isn't a valid map or has already been played this restart!", sender);
			return true;
		}

		if(sender instanceof Player) {
			Player p = (Player) sender;
			MapController.setMap(mapName);
			Messenger.broadcastInfo(p.getName() + " has set the map to " + mapName + "!");


		} else if (sender instanceof ConsoleCommandSender) {
			MapController.setMap(mapName);
			Messenger.broadcastInfo("<dark_aqua>CONSOLE</dark_aqua> has set the map to " + mapName + "!");
		}

		return true;
	}
}
