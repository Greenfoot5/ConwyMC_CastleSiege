package me.huntifi.castlesiege.commands.chat;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.staff.StaffChat;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Toggles team-chat and sends a message to all teammates
 */
public class TeamChat implements CommandExecutor {

	private static final Collection<UUID> teamChatters = new ArrayList<>();

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
		ChatColor color = p.hasPermission("castlesiege.chatmod") && !ToggleRankCommand.showDonator.contains(p)
				? ChatColor.WHITE : ChatColor.GRAY;

		Main.plugin.getLogger().info(p.getName() + " (TEAM): " + m);

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, () -> {
			for (UUID uuid : t.getPlayers()) {
				Player viewer = Bukkit.getPlayer(uuid);
				viewer.sendMessage(NameTag.chatName(p, viewer) + ChatColor.DARK_AQUA + " TEAM: " + color + m);
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
