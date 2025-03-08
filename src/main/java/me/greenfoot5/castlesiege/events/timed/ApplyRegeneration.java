package me.greenfoot5.castlesiege.events.timed;

import me.greenfoot5.castlesiege.database.CSActiveData;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.TeamController;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Objects;

/**
 * Applies regeneration to all online players
 */
public class ApplyRegeneration implements Runnable {

	/**
	 * Grants players health back depending on class
	 */
	@Override
	public void run() {
		for (Player online : Bukkit.getOnlinePlayers()) {
			// Check the player isn't dead and they're playing Castle Siege
			if(!online.isDead() && TeamController.isPlaying(online.getUniqueId()) && !InCombat.isPlayerInCombat(online.getUniqueId())) {
				// Make sure the player has been assigned a kit already
				Kit kit = Kit.equippedKits.get(online.getUniqueId());
				if (kit != null) {
					online.heal(kit.getRegen(), EntityRegainHealthEvent.RegainReason.SATIATED);
				}
			}
		}
	}
}


