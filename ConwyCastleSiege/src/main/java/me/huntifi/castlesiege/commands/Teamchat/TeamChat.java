package me.huntifi.castlesiege.commands.Teamchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
//import me.huntifi.castlesiege.teams.PlayerTeam;

public class TeamChat implements CommandExecutor {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("t")) {

				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "Console's can't say things in teamchat.");
					return true;

				}


				if (args.length == 0) {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: /t [message]");
					return true;
				}


				if(args.length >= 2 || args.length == 1){

					String message = "";

					for (int i = 0; i < args.length; i++) {

						message += args[i] + " ";

					} 

					/*for (Player online : Bukkit.getOnlinePlayers()) {

						if (PlayerTeam.getPlayerTeam(online) == PlayerTeam.getPlayerTeam(p)) {

							String str = p.getDisplayName();

							online.sendMessage(str + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + message);
							

						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("ChatMod")) {
							
							String str = p.getDisplayName();
							
							online.sendMessage(str + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + message);
							
						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("Admin")) {
							
							String str = p.getDisplayName();
							
							online.sendMessage(str + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + message);
							
						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("Moderator")) {
							
							String str = p.getDisplayName();
							
							online.sendMessage(str + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + message);
							
						} else if (StatsChanging.getStaffRank(online.getUniqueId()).equalsIgnoreCase("ChatMod+")) {
							
							String str = p.getDisplayName();
							
							online.sendMessage(str + ChatColor.DARK_AQUA + " TEAM: " + ChatColor.GRAY + message);
						}

					}*/
				} 

		} 



		return true;



	}

}
