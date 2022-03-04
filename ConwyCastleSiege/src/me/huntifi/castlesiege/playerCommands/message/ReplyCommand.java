
package me.huntifi.castlesiege.playerCommands.message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("r")) {


			if (!(sender instanceof Player)) {
				p.sendMessage(ChatColor.RED + "Console's can't say things to other players.");
				return true;

			}


			if (args.length == 0) {
				p.sendMessage(ChatColor.DARK_RED + "Usage: /r [message]");
				return true;
			}

			Player target = MessageCommand.returnMessenger();
			Player mes = MessageCommand.returnReplier();

			if (target == null) {

				p.sendMessage(ChatColor.DARK_RED + "This person is not online at the moment.");
			}

			if(args.length >= 2 || args.length == 1){

				String message = "";

				for (int i = 0; i < args.length; i++) {

					message += args[i] + " ";

				}

				if (MessageCommand.Messagers.containsKey(p)) {


					if (target != p) {

						mes.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "me " + ChatColor.GOLD + " -> " + ChatColor.RESET + "" + ChatColor.GRAY + target.getName() + ChatColor.GOLD + "] " + ChatColor.GRAY + message);

						target.sendMessage(ChatColor.GOLD + "[" + ChatColor.RESET + "" + ChatColor.GRAY + mes.getName() + ChatColor.GOLD + " -> " + ChatColor.RED + "me" + ChatColor.GOLD + "] " + ChatColor.GRAY + message);

					} else if (target == p) {

						mes.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "me " + ChatColor.GOLD + " -> " + ChatColor.RESET + "" + ChatColor.GRAY + target.getName() + ChatColor.GOLD + "] " + ChatColor.GRAY + message);

						target.sendMessage(ChatColor.GOLD + "[" + ChatColor.RESET + "" + ChatColor.GRAY + mes.getName() + ChatColor.GOLD + " -> " + ChatColor.RED + "me" + ChatColor.GOLD + "] " + ChatColor.GRAY + message);
					}

				} else  {
					
					p.sendMessage(ChatColor.RED + "No one has messaged you!");
					
				}

			}

		}

		return true;
	}

}
