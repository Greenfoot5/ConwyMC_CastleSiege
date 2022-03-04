package me.huntifi.castlesiege.playerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class discordCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;
		
		p.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]" + ChatColor.DARK_GREEN + " - The link to our discord is:" + ChatColor.AQUA + " https://discord.gg/AUDqTpC");
		
		return true;

	} 

}
