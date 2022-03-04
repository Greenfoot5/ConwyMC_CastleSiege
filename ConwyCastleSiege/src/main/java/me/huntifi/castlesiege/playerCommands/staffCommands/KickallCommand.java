package me.huntifi.castlesiege.playerCommands.staffCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class KickallCommand implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("kickall")) {

			if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin")) {

					if(args.length == 0) {

						p.sendMessage(ChatColor.DARK_RED + "Try: /kickall <Reason>");
						return true;
					} 

					if(args.length >= 1){

						String message = "";

						for (int i = 0; i < args.length; i++) {

							message += args[i] + " ";

						} 
						
						for (Player all : Bukkit.getOnlinePlayers()) {
						
						all.kickPlayer(ChatColor.RED + "Everyone was kicked by: " + ChatColor.WHITE + p.getName() + ChatColor.RED +  " for: " + ChatColor.DARK_RED + message);
						
						}
						return true;

					}

			} else {

				p.sendMessage("You do not have the permissions to use this command!");


			}
		}
		return true;
	}
}