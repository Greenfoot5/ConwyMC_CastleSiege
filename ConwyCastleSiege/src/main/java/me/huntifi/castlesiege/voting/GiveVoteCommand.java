package me.huntifi.castlesiege.voting;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class GiveVoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("givevote")) {

				if (StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Moderator") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Admin") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("ChatMod+") 
						|| StatsChanging.getStaffRank(p.getUniqueId()).equalsIgnoreCase("Developer")) {

					Player target = Bukkit.getServer().getPlayer(args[0]);

					if(args.length == 0) {

						p.sendMessage(ChatColor.DARK_RED + "Usage: /givevote [playername] vote[1-5]");


					} else if (args.length == 1) {

						p.sendMessage(ChatColor.DARK_RED + "Usage: /givevote [playername] vote[1-5]");

					}

					if (target == null) {

						p.sendMessage(ChatColor.DARK_RED + "Could not find this player, the player needs to be online!");

					}

					if (args.length >= 2) {

						try {

							int vote = Integer.parseInt(args[1]);

							if (vote == -1) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Your votes were removed by " + ChatColor.GREEN + p.getName());
								if (VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.removeVote(target.getUniqueId(), "V#1"); }
								if (VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.removeVote(target.getUniqueId(), "V#2"); }
								if (VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.removeVote(target.getUniqueId(), "V#3"); }
								if (VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.removeVote(target.getUniqueId(), "V#4"); }
							}

							if (vote == 0) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- You received all of your votes!");
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]: " + ChatColor.BLACK + "added all votes to " + target.getName());

								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.addVote(target.getUniqueId(), "V#1"); }
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.addVote(target.getUniqueId(), "V#2"); }
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.addVote(target.getUniqueId(), "V#3"); }
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.addVote(target.getUniqueId(), "V#4"); }
							}

							if (vote == 1) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - +2 damage on all melee weapons.");
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]: " + ChatColor.BLACK + "added vote 1 to " + target.getName());
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.addVote(target.getUniqueId(), "V#1"); }

							}

							if (vote == 2) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Depth strider II on all boots.");
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]: " + ChatColor.BLACK + "added vote 2 to " + target.getName());
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.addVote(target.getUniqueId(), "V#2"); }
							}

							if (vote == 3) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Received 2 more ladders for each kit.");
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]: " + ChatColor.BLACK + "added vote 3 to " + target.getName());
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.addVote(target.getUniqueId(), "V#3"); }

							}

							if (vote == 4) {

								target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Received the permission for voter classes.");
								Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC]: " + ChatColor.BLACK + "added vote 4 to " + target.getName());
								if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.addVote(target.getUniqueId(), "V#4"); }
							}

						} catch (IllegalArgumentException e) {

							p.sendMessage(ChatColor.DARK_RED + "/givevote player [remove;1-4]");
						}

					}
					
				} else {

					p.sendMessage(ChatColor.DARK_RED + "You do not have access to this, why don't you vote instead!");


				}
				
			}

		}
		
		return true;
	}
}
