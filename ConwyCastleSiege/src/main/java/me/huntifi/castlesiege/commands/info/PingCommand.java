package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.events.chat.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

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
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot use /ping!");
			return true;
		}
		Player p = (Player) sender;
		Player t = p; //set target to sender by default
		if (args.length == 1) {
			t = Bukkit.getPlayer(args[0]); //get target player specified in arg
			if (t == null) { //if target does not exist/is not online
				Messenger.sendError("Could not find player: "  + ChatColor.RED + args[0]+".", p);
				return false;
			}
		}

		String innerMessage = (t == p) ? "Your ": t.getName()+"'s ";
		Messenger.sendInfo(innerMessage + "ping is" + ChatColor.AQUA + getPing(t) + ChatColor.DARK_AQUA + "ms.", p);
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
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		return ping;
	}

}

