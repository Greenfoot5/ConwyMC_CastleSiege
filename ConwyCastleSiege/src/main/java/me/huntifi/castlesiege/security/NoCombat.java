package me.huntifi.castlesiege.security;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class NoCombat implements Listener {

	@EventHandler
	public void onhurt(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getEntity();

			if (LobbyPlayer.containsPlayer(p)) {

				e.setCancelled(true);

			}
		}
	}

}
