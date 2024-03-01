package me.huntifi.castlesiege.events.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.huntifi.castlesiege.commands.staff.ToggleRankCommand;
import me.huntifi.castlesiege.commands.staff.punishments.Mute;
import me.huntifi.castlesiege.events.curses.BlindnessCurse;
import me.huntifi.castlesiege.events.curses.CurseExpired;
import me.huntifi.castlesiege.events.curses.TrueBlindnessCurse;
import me.huntifi.castlesiege.maps.NameTag;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL;

/**
 * Customises a player's chat message
 */
public class PlayerChat implements Listener, ChatRenderer {

	private static final ArrayList<String> owners = new ArrayList<>();

	public static boolean hidePlayerName = false;
	public static boolean trueHidePlayerName = false;

	public PlayerChat() {
		owners.add("Huntifi");
		owners.add("Greenfoot5");
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChat(AsyncChatEvent e) {
		Player p = e.getPlayer();

		// Check if the player is muted
		if (Mute.isMuted(p.getUniqueId())) {
			e.setCancelled(true);
			return;
		}

		e.renderer(this);
	}

	public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
		NamedTextColor color = NamedTextColor.GRAY;

		if (!ToggleRankCommand.showDonator.contains(source)) {
			if (source.hasPermission("castlesiege.chatmod")) {
				color = NamedTextColor.WHITE;
			}

			switch (source.getName()) {
				case "Huntifi":
					color = NamedTextColor.DARK_PURPLE;
					break;
				case "Greenfoot5":
					color = NamedTextColor.DARK_GREEN;
					break;
				default:
					break;
			}
		}

		if (hidePlayerName || trueHidePlayerName)
			color = NamedTextColor.GRAY;

		if (message instanceof TextComponent) {
			String content = ((TextComponent) message).content();
			if (content.contains("@" + viewer.get(Identity.NAME))) {
				playTagSound(viewer);
			}
		}

		// Console
		if (viewer.get(Identity.NAME).isEmpty()) {
			return sourceDisplayName.append(Component.text(": ")).append(message);
		}

		return NameTag.chatName(source, viewer).append(Component.text(": "))
				.append(message.color(color));
	}

	private void playTagSound(Audience viewer) {
		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		Sound sound = Sound.sound().type(BLOCK_NOTE_BLOCK_BELL)
				.pitch(pitch).volume(volume).build();

		viewer.playSound(sound);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void beginHidingNames(BlindnessCurse curse) {
		hidePlayerName = true;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void trueBeginHidingNames(BlindnessCurse curse) {
		trueHidePlayerName = true;
	}

	@EventHandler(ignoreCancelled = true)
	public void blindnessExpired(CurseExpired curse) {
		if (Objects.equals(curse.getDisplayName(), BlindnessCurse.name)) {
			hidePlayerName = false;
		} else if (Objects.equals(curse.getDisplayName(), TrueBlindnessCurse.name)) {
			trueHidePlayerName = false;
		}
	}
}


