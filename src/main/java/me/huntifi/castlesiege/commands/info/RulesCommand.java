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

	private static final String[] rulesList = {
			"Hacks and Mods are not allowed, if you attempt to hack you will get banned without warning!",
			"Xray is not allowed, that includes transparent blocks!",
			"No abusing bugs. Please report them to a member of staff or on our Discord.",
			"No spamming. Do not say the same thing more than once or twice.",
			"No trolling. We have a zero-tolerance policy for trolling.",
			"Use English in the server chat.",
			"Do not advertise other servers, unless they are a sub-community of TheDarkAge/Conwy.",
			"Do not use hacked clients. Zero-tolerance policy. You will be banned without warning!",
			"Do not insult other players and don't be a racist, no hate-speech or bullying.",
			"Staff have a final say. Do not attempt to argue against punishment decisions once a final decision is given.",
			"Do not pvp log.",
			"Do not evade punishments.",
			"Do not post NSFW content."
	};
	private static final String border = "-----------------------------------------------------";

	@Override
	public boolean onCommand(@NotNull CommandSender p, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("The list of rules everyone must follow!").append(ChatColor.BOLD).append(" Don't break them.\n");
		sb.append(ChatColor.WHITE).append(border).append("\n"); //print out border
		int i = 0;
		ChatColor col; //alternates between grey and white
		for (String s : rulesList) {
			col = (i % 2 == 0) ? ChatColor.WHITE: ChatColor.GRAY;
			sb.append(ChatColor.YELLOW).append(i + 1).append(col).append(") ").append(s).append("\n");
			i++;
		}
		sb.append(ChatColor.WHITE).append(border);
		p.sendMessage(sb.toString());
		return true;
	}
}