package me.huntifi.castlesiege.commands.info;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player the rules
 */
public class RulesCommand implements CommandExecutor {
	/**
	 * Print the rules to the player
	 * @param p Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	private final static String[] rulesList = {
			"Hacks and Mods are not allowed, if you attempt to hack you will get banned without warning!",
			"Xray is not allowed, that includes transparent blocks!",
			"No abusing bugs. Report a bug immediately. Do not tell others how to use the bug.",
			"No spamming. Do not say the same thing more than once or twice. Do not spam chat with arguments.",
			"No trolling. We have a zero-tolerance policy for trolling. We know what trolling is and it will not be tolerated.",
			"Use English in the server chat.",
			"Do not advertise other servers, unless they are a sub-community of TheDarkAge/Conwy.",
			"Do not use hacked clients. Zero-tolerance policy. You will be banned without warning!",
			"Do not insult other players and don't be a racist, no hate-speech or bullying.",
			"Staff have a final say. Do not attempt to argue against punishment decisions once a final decision is given.",
			"Do not pvp log.",
			"Do not evade punishments.",
			"Do not post NSFW content."
	};
	private final static String border = "-----------------------------------------------------";

	@Override
	public boolean onCommand(@NotNull CommandSender p, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("The list of rules everyone must follow!" + ChatColor.BOLD + " Don't break them.\n");
		sb.append(ChatColor.WHITE + border+"\n"); //print out border
		int i = 0;
		ChatColor col; //alternates between grey and white
		for (String s : rulesList) {
			if (i % 2 == 0) { //so 0 would be white, 1 grey, 2 white, 3 grey, 4 white etc
				col = ChatColor.WHITE;
			} else { col = ChatColor.GRAY; }
			sb.append(ChatColor.YELLOW + "" + (i+1) + col + ") " + s + "\n");
			i++;
		}
		sb.append(ChatColor.WHITE + border);
		p.sendMessage(sb.toString());
		return true;
	}
}