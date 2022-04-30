package me.huntifi.castlesiege.commands.staffCommands;

import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("kick")) {

			if(sender instanceof Player) {

				if (ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Moderator")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Admin")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("ChatMod")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("ChatMod+")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Developer")) {

					Player kickedPlayer = Bukkit.getPlayer(args[0]);
					
					if(args.length == 0) {

						p.sendMessage(ChatColor.DARK_RED + "Try: /kick <Player> <Reason>");
						return true;

					} else if(args.length == 1) {

						if (kickedPlayer == null) {

							p.sendMessage(ChatColor.DARK_RED + "Could not find player " + args[0] + "!");
							return true;

						}

						Bukkit.broadcastMessage(ChatColor.GOLD + kickedPlayer.getName() + " was kicked by " + sender.getName());
						(kickedPlayer).kickPlayer(ChatColor.RED + "You were kicked by " + p.getName() + ChatColor.RED +  " for: " + ChatColor.DARK_RED + "Kicked");
						return true;
					}

					if(args.length >= 2){

						String message = "";

						for (int i = 1; i < args.length; i++) {

							message += args[i] + " ";

						} 

						Bukkit.broadcastMessage(ChatColor.GOLD + kickedPlayer.getName() + " was kicked by " + sender.getName() +  " for the reason: " + ChatColor.YELLOW + message);
						(kickedPlayer).kickPlayer(ChatColor.RED + "You were kicked by " + p.getName() + ChatColor.RED +  " for: " + ChatColor.DARK_RED + message);
						return true;

					}

				} else {

					p.sendMessage(ChatColor.DARK_RED + "You don't have the permission to do this!");


				}
			}
		}
		return true;
	}

}
