package me.huntifi.castlesiege.commands.message;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class MessageCommand implements CommandExecutor, Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static HashMap<Player, Player> Messagers = new HashMap<Player, Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player p = (Player) sender;

		Player target = Bukkit.getPlayer(args[0]);

		if (cmd.getName().equalsIgnoreCase("msg")) {


			if (!(sender instanceof Player)) {
				p.sendMessage(ChatColor.RED + "Console's can't say things to other players.");
				return true;

			}


			if (args.length == 0) {
				p.sendMessage(ChatColor.DARK_RED + "Usage: /msg Player [Message]");
				return true;
			}

			if (target == null) {

				p.sendMessage(ChatColor.DARK_RED + "Could not find player: " + ChatColor.RED + target);

			}

			if(args.length >= 2 || args.length == 1){

				String message = "";

				for (int i = 1; i < args.length; i++) {

					message += args[i] + " ";

				}

				p.sendMessage(ChatColor.GOLD + "[" + ChatColor.RED + "me " + ChatColor.GOLD + " -> " + ChatColor.RESET + "" + ChatColor.GRAY + target.getName() + ChatColor.GOLD + "] " + ChatColor.GRAY + message);

				target.sendMessage(ChatColor.GOLD + "[" + ChatColor.RESET + "" + ChatColor.GRAY + p.getName() + ChatColor.GOLD + " -> " + ChatColor.RED + "me" + ChatColor.GOLD + "] " + ChatColor.GRAY + message);

				if (returnReplier() != null) {
					
					Player replier = returnReplier();
					
					if ((Messagers.containsKey(p))) {Messagers.remove(p, replier);}
				}
			
				if (!(Messagers.containsKey(p) && Messagers.containsValue(target))) {Messagers.put(p, target);}
				


			}
		}
		return true;
	}
	
	
	//The people that message
	
	public static Player returnMessenger() {

		Player messenger = null;
	

		for (Entry<Player, Player> entry : Messagers.entrySet()) {

			messenger = entry.getKey();

		}

		return messenger;
	}
	
	//The targets

	
	public static Player returnReplier() {


		Player replier = null;
	

		for (Entry<Player, Player> entry : Messagers.entrySet()) {

			replier = entry.getValue();

		}

		return replier;
	}
	
	public void onLeave(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (MessageCommand.Messagers.containsKey(p)) {
			
			Messagers.remove(p);
			
		}
		
	}


}
