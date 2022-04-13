package me.huntifi.castlesiege.combat;

import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

/**
 * Displays the hit message and sound when a player hits a player or animal with an arrow
 */
public class HitMessage implements Listener {
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {

		Arrow arrow = (Arrow) e.getEntity();
		Player p = (Player) arrow.getShooter();

		// Check it was a player that fired an arrow
		if (e.getEntity() instanceof Arrow && arrow.getShooter() instanceof Player && p != null) {

			// If the player hit another player
			if (e.getHitEntity() instanceof Player) {

				Player hit = (Player) e.getHitEntity();

				if (MapController.getCurrentMap().getTeam(p.getUniqueId()) == MapController.getCurrentMap().getTeam(hit.getUniqueId())) {
					// Notifies the player
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + hit.getName() + ")"));
					playSound(p);

					// The shooter has interacted with the game
					InCombat.playerInteracted(p.getUniqueId());
				}

			// They hit an animal
			} else if (e.getHitEntity() instanceof Horse || e.getEntity() instanceof Chicken || e.getHitEntity() instanceof Cow
					|| e.getHitEntity() instanceof Wolf || e.getHitEntity() instanceof Sheep || e.getHitEntity() instanceof Bat) {
				// Notify the player
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + Objects.requireNonNull(e.getHitEntity()).getType() + ")"));
				playSound(p);

				// The shooter has interacted with the game
				InCombat.playerInteracted(p.getUniqueId());
			}
		}
	}

	/**
	 * Players the hit sound to the player
	 * @param player the player to play the sound to
	 */
	private void playSound(Player player) {

		Location location = player.getLocation();

		Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}
}
