package me.huntifi.castlesiege.commands.staff;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.commands.chat.TeamChat;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.chat.PlayerChat;
import me.huntifi.castlesiege.maps.NameTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Toggles staff-chat and sends a message to all staff members
 */
public class StaffChat implements CommandExecutor, Listener, ChatRenderer {

	private static final Collection<UUID> staffChatters = new ArrayList<>();

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onChat(AsyncChatEvent e) {
		Player p = e.getPlayer();

		if (!isStaffChatter(p.getUniqueId())) {
			return;
		}

		// Remove any players that aren't in the team
		ArrayList<Audience> toRemove = new ArrayList<>();
		e.viewers().forEach(v -> {
			if (v.get(Identity.NAME).isPresent() && !p.hasPermission("castlesiege.chatmod"))
				toRemove.add(v);
		});
		toRemove.forEach(e.viewers()::remove);

		e.renderer(this);
	}

	@Override
	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		NamedTextColor color = NamedTextColor.WHITE;

		// Console
		if (viewer.get(Identity.NAME).isEmpty()) {
			return sourceDisplayName.append(Component.text(" STAFF: ")).append(message);
		}

		if (message instanceof TextComponent) {
			String content = PlainTextComponentSerializer.plainText().serialize(message);
			if (content.contains("@" + viewer.get(Identity.NAME))) {
				PlayerChat.playTagSound(viewer);
			}
		}

		return NameTag.chatName(source, viewer).append(Component.text(" STAFF: ").color(NamedTextColor.AQUA))
				.append(message.color(color));
	}

	/**
	 * Toggle staff-chat mode if no arguments are provided
	 * Send the provided arguments as a chat message to all staff members
	 * @param sender Source of the command
	 * @param cmd Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		// Console can't toggle staff-chat mode
		if (sender instanceof ConsoleCommandSender) {
			if (args.length == 0) {
				sender.sendMessage("Console cannot toggle staff-chat mode!");
				return false;
			} else {
				String message = String.join(" ", args);
				sendMessage(message);
			}

		} else {
			Player p = (Player) sender;
			if (args.length == 0) {
				toggleStaffChat(p);
			} else {
				sendMessage(p, String.join(" ", args));
			}
		}

		return true;
	}

	/**
	 * Send a message to all staff members from console
	 * @param m The message to send
	 */
	public static void sendMessage(String m) {
		String s = "<white>CONSOLE</white> <aqua>STAFF:</aqua> " + m;

		// Send the message to all staff members
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("castlesiege.chatmod")) {
				Messenger.send(s, p);
			}
		}
	}

	/**
	 * Send a message to all staff members
	 * @param p The player that sends the message
	 * @param m The message to send
	 */
	public static void sendMessage(Player p, String m) {
		String s = "<white>" + NameTag.mmUsername(p) + " <aqua>STAFF:</aqua> " + m;

		// Send the message to all staff members
		Main.plugin.getLogger().info(s);
		for (Player receiver : Bukkit.getOnlinePlayers()) {
			if (receiver.hasPermission("castlesiege.chatmod")) {
				receiver.sendMessage(MiniMessage.miniMessage().deserialize(s));
			}
		}
	}

	/**
	 * Get the staff-chat status of a player
	 * @param uuid The unique ID of the player
	 * @return Whether the player is in staff-chat mode
	 */
	public static boolean isStaffChatter(UUID uuid) {
		return staffChatters.contains(uuid);
	}

	/**
	 * Toggle the staff-chat status for a player
	 * @param p The player for whom to toggle staff-chat
	 */
	private void toggleStaffChat(Player p) {
		UUID uuid = p.getUniqueId();
		if (staffChatters.contains(uuid)) {
			staffChatters.remove(uuid);
			Messenger.sendInfo("You are no longer talking in staff-chat!", p);
		} else {
			TeamChat.removePlayer(uuid);
			staffChatters.add(uuid);
			Messenger.sendInfo("You are now talking in staff-chat!", p);
		}
	}

	/**
	 * Remove the player from the staff chatters
	 * @param uuid The unique ID of the player
	 */
	public static void removePlayer(UUID uuid) {
		staffChatters.remove(uuid);
	}
}
