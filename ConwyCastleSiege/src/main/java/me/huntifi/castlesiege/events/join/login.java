package me.huntifi.castlesiege.events.join;

import me.huntifi.castlesiege.kits.kits.Swordsman;
import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles what happens when someone logs in
 */
public class login implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		
		p.setFoodLevel(20);
		p.setHealthScaled(true);

		MapController.joinATeam(p.getUniqueId());
		new Swordsman().addPlayer(p.getUniqueId());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		MapController.leaveTeam(p.getUniqueId());
	}
}
