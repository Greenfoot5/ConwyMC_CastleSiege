package me.huntifi.castlesiege.playerCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.stats.MVP.MVPstats;

public class suicideCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("Sui") || cmd.getName().equalsIgnoreCase("Suicide")) {
				
				p.setHealth(0);
				p.sendMessage("You have commited suicide (+2 deaths)");
				MVPstats.setKillstreak(p.getUniqueId(), 0);
				MVPstats.addDeaths(p.getUniqueId(), 1);

			}
		}
		
		return true;
	}

}
