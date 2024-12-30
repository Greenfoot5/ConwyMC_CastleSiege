package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.objects.Flag;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Shows the player the teams of the current map and their player count
 */
public class FlagsCommand implements CommandExecutor {

	/**
	 * Print the name and size of each team on the current map
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Team[] teams = MapController.getCurrentMap().teams;
		Component c = Component.empty();

		for (Team t : teams) {
			c = c.append(t.getDisplayName().decorate(TextDecoration.BOLD))
					.append(Component.text("'s Flags: ", t.primaryChatColor).decorate(TextDecoration.BOLD))
					.append(Component.newline());
			boolean hasAny = false;
			Component fc = Component.empty().color(t.primaryChatColor);
			for (Flag f : MapController.getCurrentMap().flags) {
				if (Objects.equals(f.getCurrentOwners(), t.getName()) && f.isActive()) {
					if (hasAny) {
						fc = fc.append(Component.text(", "));
					}
					fc = fc.append(Component.text(f.getName()));
					hasAny = true;
				}
			}

			if (!hasAny) {
				fc = Component.text("None");
			}
			c = c.append(fc).append(Component.newline());
		}

		c = c.append(Component.text("Neutral: ", NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
				.append(Component.newline());
		boolean hasAny = false;
		Component fc = Component.empty().color(NamedTextColor.GRAY);
		for (Flag f : MapController.getCurrentMap().flags) {
			if (Objects.equals(f.getCurrentOwners(), "neutral") && f.isActive()) {
				if (hasAny) {
					fc = fc.append(Component.text(", "));
				}
				fc = fc.append(f.getDisplayName());
				hasAny = true;
			}
		}

		if (!hasAny) {
			fc = Component.text("None").color(NamedTextColor.GRAY);
		}
		c = c.append(fc);

		Messenger.send(c, sender);
		return true;
	}
}
