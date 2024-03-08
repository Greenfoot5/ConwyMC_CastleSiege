package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.events.chat.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Shows the player the rules
 */
public class RulesCommand implements CommandExecutor {

	private static final String[] rulesList = {
			"No Hacks/Mods/Xray",
			"If you find a bug please report it. Don't abuse it.",
			"No spamming or trolling.",
			"Use English in the server chat.",
			"Do not advertise other servers, unless they are a sub-community of TheDarkAge/Conwy.",
			"Do not insult other players and don't be a racist; no hate-speech or bullying.",
			"Do not post NSFW content.",
			"Do not pvp log.",
			"Do not evade punishments.",
			"Staff have a final say. Do not attempt to argue against punishment decisions once a final decision is given.",
	};
	private static final Component border = Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.WHITE).decorate(TextDecoration.STRIKETHROUGH);

	@Override
	public boolean onCommand(@NotNull CommandSender p, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Component c = Component.text("The list of rules everyone must follow!")
				.append(Component.text(" Don't break them.").decorate(TextDecoration.BOLD))
						.append(Component.newline())
				.append(border);
		int i = 0;
		NamedTextColor color; //alternates between grey and white
		for (String s : rulesList) {
			color = (i % 2 == 0) ? NamedTextColor.WHITE: NamedTextColor.GRAY;
			c = c.append(Component.newline()
					.append(Component.text((i + 1) + ") ", NamedTextColor.YELLOW))
					.append(MiniMessage.miniMessage().deserialize(s)).color(color));
			i++;
		}
		c = c.append(Component.newline().append(border).append(Component.newline()).decoration(TextDecoration.STRIKETHROUGH, false)
				.append(MiniMessage.miniMessage().deserialize("<yellow><click:open_url:https://conwymc.alchemix.dev>See our website for more info</click></yellow>")));
		Messenger.send(c, p);
		return true;
	}
}