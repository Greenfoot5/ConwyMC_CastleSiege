package me.huntifi.castlesiege.commands.staffCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class FlyCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("fly")) {

			if(sender instanceof Player) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+") 
						|| p.isOp() 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

					if (p.getAllowFlight() == true) {
						
						p.setAllowFlight(false);
						p.setFlying(false);
						
						p.sendMessage(ChatColor.DARK_GREEN + " Flying has been disabled for you!");
						return true;

					} else if (p.getAllowFlight() == false) {
						
						p.setAllowFlight(true);
						p.setFlying(true);
						
						p.sendMessage(ChatColor.DARK_GREEN + " Flying has been enabled for you, enjoy your flight!");


					}

				} else {
					
					p.sendMessage(ChatColor.DARK_RED + "You are not permitted to use this command!");
					
				}

			}
		}
		return true;

	}

}
