package me.huntifi.castlesiege.stats.mystats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MystatsCommand implements CommandExecutor {
	
	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		
		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("Mystats")) {

				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { p.openBook(MystatsBook.newBook(p)); });

			}
		}

		return true;


	}
}


