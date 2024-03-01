package me.huntifi.castlesiege.commands.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.chat.PlayerChat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Toggles team-chat and sends a message to all teammates
 */
public class TeamChat implements CommandExecutor, Listener, ChatRenderer {

	private static final Collection<UUID> teamChatters = new ArrayList<>();

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChat(AsyncChatEvent e) {
		Player p = e.getPlayer();
		if (!isTeamChatter(p.getUniqueId()))
			return;

		Team t = TeamController.getTeam(p.getUniqueId());
		if (t == null) {
			Messenger.sendInfo("You left your team and are now talking in global chat", p);
			return;
		}

		// Remove any players that aren't in the team
		ArrayList<Audience> toRemove = new ArrayList<>();
		e.viewers().forEach(v -> {
			if (v.get(Identity.NAME).isPresent() && !t.hasPlayer(v.getOrDefault(Identity.UUID, null)))
				toRemove.add(v);
		});
		toRemove.forEach(e.viewers()::remove);

		e.renderer(this);
	}

	@Override
	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		NamedTextColor color = source.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(source)
				? NamedTextColor.WHITE : NamedTextColor.GRAY;

		// Console
		if (viewer.get(Identity.NAME).isEmpty()) {
			return sourceDisplayName.append(Component.text(" (TEAM): ")).append(message);
		}

		if (message instanceof TextComponent) {
			String content = ((TextComponent) message).content();
			if (content.contains("@" + viewer.get(Identity.NAME))) {
				PlayerChat.playTagSound(viewer);
			}
		}

		return NameTag.chatName(source, viewer).append(Component.text(" TEAM: ").color(NamedTextColor.DARK_AQUA))
				.append(message.color(color));
	}

	/**
	 * Toggle team-chat mode if no arguments are provided
	 * Send the provided arguments as a chat message to all team members
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			Messenger.sendError("Team chat cannot be used from console!", sender);
			return true;
		} else if (!MapController.getPlayers().contains(((Player) sender).getUniqueId())) {
			Messenger.sendError("You don't have a team!", sender);
			return true;
		}

		// Check if the player is muted
		Player p = (Player) sender;
		if (Mute.isMuted(p.getUniqueId())) {
			return true;
		}

		if (args.length == 0) {
			toggleTeamChat(p);
		} else {
			sendMessage(p, String.join(" ", args));
		}

		return true;
	}

	/**
	 * Send a message to all teammates of a player
	 * @param p The player that sends the message
	 * @param m The message to send
     * @return true if the message was sent correctly
	 */
	public static boolean sendMessage(Player p, String m) {
		Team t = TeamController.getTeam(p.getUniqueId());
		if (t == null) {
			Messenger.sendInfo("You left your team and are now talking in global chat", p);
			return false;
		}
		NamedTextColor color = p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)
				? NamedTextColor.WHITE : NamedTextColor.GRAY;

		Main.plugin.getLogger().info(p.getName() + " (TEAM): " + m);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			for (UUID uuid : t.getPlayers()) {
				Player viewer = Bukkit.getPlayer(uuid);
				((Audience) viewer).sendMessage(Component.text()
						.append(NameTag.chatName(p, viewer))
						.append(Component.text(" TEAM: ")
								.color(NamedTextColor.DARK_AQUA))
						.append(Component.text(m)
								.color(color)));
			}
		});
		return true;
	}

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
			p.sendMessage(ChatColor.DARK_AQUA + "You are no longer talking in team-chat!");
		} else {
			StaffChat.removePlayer(uuid);
			teamChatters.add(uuid);
			p.sendMessage(ChatColor.DARK_AQUA + "You are now talking in team-chat!");
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
