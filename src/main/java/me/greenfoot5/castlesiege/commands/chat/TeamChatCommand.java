package me.greenfoot5.castlesiege.commands.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.conwymc.commands.chat.GlobalChatCommand;
import me.greenfoot5.conwymc.commands.chat.ToggleChatCommand;
import me.greenfoot5.conwymc.data_types.PlayerData;
import me.greenfoot5.conwymc.database.ActiveData;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Toggles team-chat and sends a message to all teammates
 */
public class TeamChatCommand extends ToggleChatCommand {

	/** The string representing team chat mode */
	public static final String CHAT_MODE = "team";

	/**
	 * @param e Sets the renderer if the player is chatting in team chat
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChat(AsyncChatEvent e) {

		PlayerData data = ActiveData.getData(e.getPlayer().getUniqueId());
		// This can be overridden by changing the message before
		if (e.originalMessage() == e.message() && data.getChatMode().equalsIgnoreCase(CHAT_MODE))
			e.renderer(this);
	}

	@Override
	protected void toggleChatMode(Player player) {
		PlayerData data = ActiveData.getData(player.getUniqueId());
		if (data.getChatMode().equals(CHAT_MODE)) {
			data.setChatMode(GlobalChatCommand.CHAT_MODE);
			Messenger.sendInfo("You are no longer talking in team-chat!", player);
		} else {
			data.setChatMode(CHAT_MODE);
			Messenger.sendInfo("You are now talking in team-chat!", player);
		}
	}

	@Override
	protected Audience getReceivers(CommandSender sender) {
		Audience receivers = Bukkit.getServer();
		UUID uuid = ((Player) sender).getUniqueId();
		receivers = receivers.filterAudience(v -> (v.get(Identity.UUID).isEmpty() || TeamController.getTeam(uuid) == TeamController.getTeam(v.get(Identity.UUID).get())));
		return receivers;
	}

	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		String color = getChatColor(source);
		Component prefix = Messenger.mm
				.deserialize("<white>[<dark_aqua>TEAM</dark_aqua>]</white> ");

		sourceDisplayName = CSNameTag.level(source, viewer).append(sourceDisplayName);
		Component content = Messenger.mm.deserialize(color + Messenger.clean(message));
		return prefix.append(sourceDisplayName).append(Component.text(": ", NamedTextColor.AQUA))
				.append(content);
	}
}
