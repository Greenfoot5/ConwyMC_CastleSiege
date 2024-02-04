package me.huntifi.castlesiege.events.death;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Prevents players from going too low
 */
public class VoidLocation implements Listener {

	/**
	 * Kills player when their Y level is below 0
	 * @param e The event called when a player moves
	 */
	@EventHandler(ignoreCancelled = true)
	public void onEnterVoid(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		
		if (loc.getY() < 0) {
			p.setHealth(0);
		}
	}
}
