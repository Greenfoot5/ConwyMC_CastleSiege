package me.huntifi.castlesiege.voting;

import me.huntifi.castlesiege.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class VoteListenerCommand implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player) && sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("givevoter")) {

				if (args.length >= 2) {

					try {
						
						Player target = Bukkit.getServer().getPlayer(args[0]);

						int vote = Integer.parseInt(args[1]);

						if (vote == -1) {

							assert target != null;
							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Your votes were removed by console");
							if (VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.removeVote(target.getUniqueId(), "V#1"); }
							if (VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.removeVote(target.getUniqueId(), "V#2"); }
							if (VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.removeVote(target.getUniqueId(), "V#3"); }
							if (VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.removeVote(target.getUniqueId(), "V#4"); }
							Main.instance.getLogger().info("[ConwyMC]: removed all votes from " + target.getName());
						}

						if (vote == 0) {

							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- You received all of your votes!");
							Main.instance.getLogger().info("[ConwyMC]: added all votes to " + target.getName());

							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.addVote(target.getUniqueId(), "V#1"); }
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.addVote(target.getUniqueId(), "V#2"); }
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.addVote(target.getUniqueId(), "V#3"); }
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.addVote(target.getUniqueId(), "V#4"); }
						}

						if (vote == 1) {

							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - +2 damage on all melee weapons.");
							Main.instance.getLogger().info("[ConwyMC]: added vote 1 to " + target.getName());
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#1")) { VotesChanging.addVote(target.getUniqueId(), "V#1"); }

						}

						if (vote == 2) {

							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Depth strider II on all boots.");
							Main.instance.getLogger().info("[ConwyMC]: added vote 2 to " + target.getName());
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#2")) { VotesChanging.addVote(target.getUniqueId(), "V#2"); }
						}

						if (vote == 3) {

							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Received 2 more ladders for each kit.");
							Main.instance.getLogger().info("[ConwyMC]: added vote 3 to " + target.getName());
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#3")) { VotesChanging.addVote(target.getUniqueId(), "V#3"); }

						}

						if (vote == 4) {

							target.sendMessage(ChatColor.LIGHT_PURPLE + "[ConwyMC] " + ChatColor.DARK_GREEN + "- Thanks for voting! - Received the permission for voter classes.");
							Main.instance.getLogger().info("[ConwyMC]: added vote 4 to " + target.getName());
							if (!VotesChanging.getVotes(target.getUniqueId()).contains("V#4")) { VotesChanging.addVote(target.getUniqueId(), "V#4"); }
						}
						

					} catch (IllegalArgumentException e) {

						e.printStackTrace();

					}

				}

			}
		}
		return true;
	}

}
