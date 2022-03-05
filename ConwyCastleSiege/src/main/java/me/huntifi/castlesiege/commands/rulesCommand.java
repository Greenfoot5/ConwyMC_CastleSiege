package me.huntifi.castlesiege.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rulesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("rules")) {
				p.sendMessage("The list of rules everyone must follow!" + ChatColor.BOLD + " Don't break them.");
				p.sendMessage("-----------------------------------------------------");
				p.sendMessage(ChatColor.YELLOW + "1" + ChatColor.WHITE + ") Hacks and Mods are not allowed, if you attempt to hack you will get banned without warning!");
				p.sendMessage(ChatColor.YELLOW + "2" + ChatColor.GRAY + ") Xray is not allowed, that includes transparent blocks!");
				p.sendMessage(ChatColor.YELLOW + "3" + ChatColor.WHITE + ") No abusing bugs. Report a bug immediately. Do not tell others how to use the bug.");
				p.sendMessage(ChatColor.YELLOW + "4" + ChatColor.GRAY + ") No spamming. Do not say the same thing more than twice or once. Do not spam chat with arguments.");
				p.sendMessage(ChatColor.YELLOW + "5" + ChatColor.WHITE + ") No trolling. We have a zero-tolerance policy for trolling. We know what trolling is and it will not be tolerated.");
				p.sendMessage(ChatColor.YELLOW + "6" + ChatColor.GRAY + ") Use English in the server chat.");
				p.sendMessage(ChatColor.YELLOW + "7" + ChatColor.WHITE + ") Do not advertise other servers, unless they are a sub-community of Thedarkage.");
				p.sendMessage(ChatColor.YELLOW + "8" + ChatColor.GRAY + ") Do not use hacked clients. Zero-tolerance policy. You will be banned without warning!");
				p.sendMessage(ChatColor.YELLOW + "9" + ChatColor.WHITE + ") Do not insult other players and don't be a racist, no hate-speach or bullying.");
				p.sendMessage(ChatColor.YELLOW + "10" + ChatColor.GRAY + ") Staff have a final say. Do not attempt to argue against punishment decisions once a final decision is given.");
				p.sendMessage(ChatColor.YELLOW + "11" + ChatColor.WHITE + ") Do not pvp log.");
				p.sendMessage(ChatColor.YELLOW + "12" + ChatColor.GRAY + ") Do not evade punishments.");
				p.sendMessage(ChatColor.YELLOW + "13" + ChatColor.WHITE + ") Don't post NSFW content.");
				p.sendMessage("-----------------------------------------------------");
			}
		}
		return true;
	}
}
