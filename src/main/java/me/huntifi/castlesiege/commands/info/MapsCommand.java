package me.huntifi.castlesiege.commands.info;

import me.huntifi.castlesiege.maps.Map;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Shows the player the maps in rotation
 */
public class MapsCommand implements CommandExecutor {

	/**
	 * Print the maps in rotation to the player
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Component c = Component.text().build();

		// Add all maps to the string builder and color the active map green

        List<Map> maps = MapController.maps;
        for (int i = 0; i < maps.size(); i++) {
            Map map = maps.get(i);
            if (Objects.equals(MapController.getCurrentMap(), map)) {
                c = c.append(Component.text(map.name, NamedTextColor.DARK_GREEN));
            } else {
                c = c.append(Component.text(map.name, NamedTextColor.GRAY));
            }
			float phase = ((float)i + 1f) / ((float)maps.size());
            c = c.append(Messenger.mm.deserialize("<transition:#3EADCF:#ABE9CD:" + phase + "> > </transition>"));
        }
		c = c.append(Component.text("Restart", NamedTextColor.GRAY));

		// Print the message
		Messenger.send(c, sender);
		return true;
	}
}
