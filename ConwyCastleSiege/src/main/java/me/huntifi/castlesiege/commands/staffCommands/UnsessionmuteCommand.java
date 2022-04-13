package me.huntifi.castlesiege.commands.staffCommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.events.join.stats.StatsChanging;

public class UnsessionmuteCommand implements CommandExecutor {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMcStats");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("unsessionmute")) {

			if(sender instanceof Player) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+") 
						|| p.isOp() 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

					Player mutedPlayer = Bukkit.getPlayer(args[0]);

					if(args.length == 0) {

						p.sendMessage(ChatColor.DARK_RED + "Try: /unsessionmute <Player>");
						return true;

					} else if(args.length == 1) {

						if (mutedPlayer == null) {

							p.sendMessage(ChatColor.DARK_RED + "Could not find player " + args[0] + "!");
							return true;

						}
						
						if (SessionMuteCommand.sessionmuted.contains(mutedPlayer)) {

						mutedPlayer.sendMessage(ChatColor.GOLD + "You have been un-sessionmuted");
						p.sendMessage("You have un-sessionmuted " + mutedPlayer.getName());
						SessionMuteCommand.sessionmuted.remove(mutedPlayer);
						return true;
						
						} else {
							
							p.sendMessage(ChatColor.RED + "This player is currently not session muted");
							return true;
							
						}
					}

				} else {

					p.sendMessage(ChatColor.DARK_RED + "You are not permitted to do this!");


				}
			}
		}
		return true;

	}

}