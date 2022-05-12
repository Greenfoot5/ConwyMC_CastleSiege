package me.huntifi.castlesiege.commands.staff.punishments;

import java.util.ArrayList;

import me.huntifi.castlesiege.database.ActiveData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class SessionMuteCommand implements Listener, CommandExecutor {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyMcStats");
	
	public static ArrayList<Player> sessionmuted = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("sessionmute")) {

			if(sender instanceof Player) {

				if (ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Moderator")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Admin")
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("ChatMod+")
						|| p.isOp() 
						|| ActiveData.getData(p.getUniqueId()).getStaffRank().equalsIgnoreCase("Developer")) {

					Player mutedPlayer = Bukkit.getPlayer(args[0]);

					if(args.length == 0) {

						p.sendMessage(ChatColor.DARK_RED + "Try: /sessionmute <Player> <Reason>");
						return true;

					} else if(args.length == 1) {

						if (mutedPlayer == null) {

							p.sendMessage(ChatColor.DARK_RED + "Could not find player " + args[0] + "!");
							return true;

						}
						
						if (!sessionmuted.contains(mutedPlayer)) {

						Bukkit.broadcastMessage(ChatColor.GOLD + mutedPlayer.getName() + " was session muted by " + sender.getName());
						sessionmuted.add(mutedPlayer);
						return true;
						
						} else {
							
							p.sendMessage(ChatColor.RED + "This player is already session muted!");
							
						}
					}

					if(args.length >= 2){
						
						if (!sessionmuted.contains(mutedPlayer)) {

						String message = "";

						for (int i = 1; i < args.length; i++) {

							message += args[i] + " ";

						} 

						Bukkit.broadcastMessage(ChatColor.GOLD + mutedPlayer.getName() + " was session muted by " + sender.getName() +  " for the reason: " + ChatColor.YELLOW + message);
						sessionmuted.add(mutedPlayer);
						return true;
						
						} else {
							
							p.sendMessage(ChatColor.RED + "This player is already session muted!");
							
						}

					}

				} else {

					p.sendMessage(ChatColor.DARK_RED + "You are not permitted to do this!");


				}
			}
		}
		return true;

	}
	
	@EventHandler
	public void on(AsyncPlayerChatEvent event) {
		
		if (sessionmuted.contains(event.getPlayer())) {
			
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You were session muted, if you think this was unfair let us know on discord. Use: /discord");
			event.getPlayer().sendMessage(ChatColor.RED + "Session muted means that you won't be able to speak till someone unmutes you or restarts the server.");
			
		}
		
	}
	

}