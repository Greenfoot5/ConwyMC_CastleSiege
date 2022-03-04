package me.huntifi.castlesiege.Deathmessages;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathmessageDisable implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeadminesDeath(PlayerDeathEvent e) {
		
		e.setDeathMessage(null);

	}
}
