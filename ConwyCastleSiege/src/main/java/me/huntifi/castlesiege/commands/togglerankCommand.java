package me.huntifi.castlesiege.commands;

import java.util.ArrayList;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.tags.NametagsEvent;

public class togglerankCommand implements CommandExecutor {
	
	public static ArrayList<Player> rankers = new ArrayList<Player>();

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMcLobby");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot toggle their rank!");
			return true;
		}

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("togglerank")) {

			if(sender instanceof Player) {

				if(p.hasPermission("chatmod")) {

					if(rankers.contains(p)) {
						
						rankers.remove(p);
						p.sendMessage(ChatColor.RED + "Rank toggled on");

					} else {
						
						rankers.add(p);
						p.sendMessage(ChatColor.RED + "Rank toggled off");
					}

					NametagsEvent.GiveNametag(p);
					return true;

				}

			}
		}
		return true;

	}
}
