package me.huntifi.castlesiege.playerCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.EntityPlayer;

public class pingCommand implements CommandExecutor {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("ping")) {

            p.sendMessage(ChatColor.DARK_AQUA + "Your ping is: " + ChatColor.AQUA + getPing(p) + ChatColor.DARK_AQUA + " ms.");
            return true;
			}
		}
		return true;
	}
	
	public double getPing(Player p) {
		
		CraftPlayer pingc = (CraftPlayer) p;
		EntityPlayer pinge = pingc.getHandle();

		return (double) pinge.ping;

	}

}
