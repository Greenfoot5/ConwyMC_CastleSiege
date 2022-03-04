package me.huntifi.castlesiege.playerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class pingCommand implements CommandExecutor {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("ping")) {

            p.sendMessage(ChatColor.DARK_AQUA + "Your ping is: " + ChatColor.AQUA + getPing(p) + ChatColor.DARK_AQUA + " ms.");
            return true;
			}
		}
		return true;
	}
	
	public double getPing(Player player) {
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
