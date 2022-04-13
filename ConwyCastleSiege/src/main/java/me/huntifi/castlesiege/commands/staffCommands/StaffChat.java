package me.huntifi.castlesiege.commands.staffCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.events.join.stats.StatsChanging;

public class StaffChat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("s")) {

			if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") 
					|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") 
					|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod") 
					|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+") 
					|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "Console's can't say things in staffchat.");
					return true;

				}


				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: /s [message]");
					return true;
				}


				if(args.length >= 2 || args.length == 1){

					String message = "";

					for (int i = 0; i < args.length; i++) {

						message += args[i] + " ";

					} 

					for (Player online : Bukkit.getOnlinePlayers()) {

						if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("ChatMod")) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + message);

						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("Admin")) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + message);

						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("Moderator")) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + message);

						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("ChatMod+")) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + message);
							
						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("Developer")) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.AQUA + " STAFF: " + ChatColor.WHITE + message);
						}

					}
				} 

			} else {
				
				p.sendMessage(ChatColor.DARK_RED + "You don't have access to this command!");
				
				
			}

		}



		return true;
	}

}
