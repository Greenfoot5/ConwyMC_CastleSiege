package me.huntifi.castlesiege.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Database.SQLStats;
import me.huntifi.castlesiege.Database.StatsStrings;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class KitsCommand implements CommandExecutor {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("Kit")) {

			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

				if (LobbyPlayer.containsPlayer(p)) {

					if (args[0].equalsIgnoreCase("Swordsman")) {

						SQLStats.setKit(p.getUniqueId(), "Swordsman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { 	ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 

					if (args[0].equalsIgnoreCase("Archer")) {

						SQLStats.setKit(p.getUniqueId(), "Archer");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("Spearman")) {

						SQLStats.setKit(p.getUniqueId(), "Spearman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("Skirmisher")) {

						SQLStats.setKit(p.getUniqueId(), "Skirmisher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						 
					} 


					if (args[0].equalsIgnoreCase("Shieldman")) {

						SQLStats.setKit(p.getUniqueId(), "Shieldman");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						
					} 


					if (args[0].equalsIgnoreCase("FireArcher")) {


						SQLStats.setKit(p.getUniqueId(), "FireArcher");
						StatsStrings.returnKit(p);
						Bukkit.getScheduler().runTask(plugin, () -> { ClearSlots.clearAllSlots(p); EnderchestRefill.refill(p); });
						 
					} 

				} else {

					p.sendMessage(ChatColor.RED + "You can't use this command on the battlefield!");
				}
			});

			return true;

		}
		return true; 

	}

}
