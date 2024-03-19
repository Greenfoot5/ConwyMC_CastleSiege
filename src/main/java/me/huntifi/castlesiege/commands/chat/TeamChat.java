package me.huntifi.castlesiege.commands.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.maps.TeamController;
import me.huntifi.conwymc.commands.chat.GlobalChatCommand;
import me.huntifi.conwymc.commands.chat.ToggleChatCommand;
import me.huntifi.conwymc.data_types.PlayerData;
import me.huntifi.conwymc.database.ActiveData;
import me.huntifi.conwymc.util.Messenger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Toggles team-chat and sends a message to all teammates
 */
public class TeamChat extends ToggleChatCommand {

	private static final Collection<UUID> teamChatters = new ArrayList<>();

	/** The string representing global chat mode */
	public static final String CHAT_MODE = "team";

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
			me.huntifi.conwymc.util.Messenger.sendInfo("You are no longer talking in team-chat!", player);
		} else {
			data.setChatMode(CHAT_MODE);
			me.huntifi.conwymc.util.Messenger.sendInfo("You are now talking in team-chat!", player);
		}
	}

	@Override
	protected ForwardingAudience getReceivers(CommandSender sender) {
		ForwardingAudience receivers = Bukkit.getServer();
		UUID uuid = ((Player) sender).getUniqueId();
		receivers.filterAudience(v -> (v.get(Identity.UUID).isEmpty() && TeamController.getTeam(uuid) == TeamController.getTeam(v.get(Identity.UUID).get())));
		return receivers;
	}

	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		String color = getChatColor(source);
		Component prefix = Messenger.mm
				.deserialize("<white>[<aqua><b>STAFF</b></aqua>] " +
						source.getName() + "</white>");

		Component content = Messenger.mm.deserialize(color + message);
		return prefix.append(Component.text(": ", NamedTextColor.AQUA))
				.append(content);
	}



//	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
//	public void onChat(AsyncChatEvent e) {
//		Player p = e.getPlayer();
//		if (!isTeamChatter(p.getUniqueId()))
//			return;
//
//		Team t = TeamController.getTeam(p.getUniqueId());
//		if (t == null) {
//			Messenger.sendInfo("You left your team and are now talking in global chat", p);
//			return;
//		}
//
//		// Remove any players that aren't in the team
//		ArrayList<Audience> toRemove = new ArrayList<>();
//		e.viewers().forEach(v -> {
//			if (v.get(Identity.NAME).isPresent() && !t.hasPlayer(v.getOrDefault(Identity.UUID, null)))
//				toRemove.add(v);
//		});
//		toRemove.forEach(e.viewers()::remove);
//
//		e.renderer(this);
//	}

//	@Override
//	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
//		NamedTextColor color = source.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(source)
//				? NamedTextColor.WHITE : NamedTextColor.GRAY;
//
//		// Console
//		if (viewer.get(Identity.NAME).isEmpty()) {
//			return sourceDisplayName.append(Component.text(" (TEAM): ")).append(message);
//		}
//
//		if (message instanceof TextComponent) {
//			String content = PlainTextComponentSerializer.plainText().serialize(message);
//			if (content.contains("@" + viewer.get(Identity.NAME))) {
//				PlayerChat.playTagSound(viewer);
//			}
//		}
//
//		return NameTag.chatName(source, viewer).append(Component.text(" TEAM: ").color(NamedTextColor.DARK_AQUA))
//				.append(message.color(color));
//	}

//	/**
//	 * Toggle team-chat mode if no arguments are provided
//	 * Send the provided arguments as a chat message to all team members
//	 * @param sender Source of the command
//	 * @param cmd Command which was executed
//	 * @param label Alias of the command which was used
//	 * @param args Passed command arguments
//	 * @return true
//	 */
//	@Override
//	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
//		if (!(sender instanceof Player)) {
//			Messenger.sendError("Team chat cannot be used from console!", sender);
//			return true;
//		} else if (!MapController.getPlayers().contains(((Player) sender).getUniqueId())) {
//			Messenger.sendError("You don't have a team!", sender);
//			return true;
//		}
//
//		// Check if the player is muted
//		Player p = (Player) sender;
//		if (Mute.isMuted(p.getUniqueId())) {
//			return true;
//		}
//
//		if (args.length == 0) {
//			toggleTeamChat(p);
//		} else {
//			sendMessage(p, String.join(" ", args));
//		}
//
//		return true;
//	}

//	/**
//	 * Send a message to all teammates of a player
//	 * @param p The player that sends the message
//	 * @param m The message to send
//     * @return true if the message was sent correctly
//	 */
//	public static boolean sendMessage(Player p, String m) {
//		Team t = TeamController.getTeam(p.getUniqueId());
//		if (t == null) {
//			Messenger.sendInfo("You left your team and are now talking in global chat", p);
//			return false;
//		}
//		NamedTextColor color = p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)
//				? NamedTextColor.WHITE : NamedTextColor.GRAY;
//
//		Main.plugin.getLogger().info(p.getName() + " (TEAM): " + m);
//
//		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
//			for (UUID uuid : t.getPlayers()) {
//				Player viewer = Bukkit.getPlayer(uuid);
//                assert viewer != null;
//                viewer.sendMessage(Component.text()
//						.append(NameTag.chatName(p, viewer))
//						.append(Component.text(" TEAM: ")
//								.color(NamedTextColor.DARK_AQUA))
//						.append(Component.text(m)
//								.color(color)));
//			}
//		});
//		return true;
//	}

	/**
	 * Get the team-chat status of a player
	 * @param uuid The unique ID of the player
	 * @return Whether the player is in team-chat mode
	 */
	public static boolean isTeamChatter(UUID uuid) {
		return teamChatters.contains(uuid);
	}

	/**
	 * Toggle the team-chat status for a player
	 * @param p The player for whom to toggle team-chat
	 */
	private void toggleTeamChat(Player p) {
		UUID uuid = p.getUniqueId();
		if (teamChatters.contains(uuid)) {
			teamChatters.remove(uuid);
			Messenger.sendInfo("You are no longer talking in team-chat", p);
		} else {
			StaffChat.removePlayer(uuid);
			teamChatters.add(uuid);
			Messenger.sendInfo("You are now talking in team-chat", p);
		}
	}

	/**
	 * Remove the player from the team chatters
	 * @param uuid The unique ID of the player
	 */
	public static void removePlayer(UUID uuid) {
		teamChatters.remove(uuid);
	}
}
