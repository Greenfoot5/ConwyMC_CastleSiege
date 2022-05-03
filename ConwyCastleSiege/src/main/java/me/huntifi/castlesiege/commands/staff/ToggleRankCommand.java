package me.huntifi.castlesiege.commands.staff;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.NameTag;
import org.jetbrains.annotations.NotNull;

public class ToggleRankCommand implements CommandExecutor {
	
	public static ArrayList<Player> showDonator = new ArrayList<Player>();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot toggle their rank!");
			return true;
		}

		Player p = (Player) sender;
		if (showDonator.contains(p)) {
			showDonator.remove(p);
			p.sendMessage(ChatColor.RED + "Staff rank toggled on");
		} else {
			showDonator.add(p);
			p.sendMessage(ChatColor.RED + "Staff rank toggled off");
		}

		NameTag.give(p);
		return true;

	}
}
