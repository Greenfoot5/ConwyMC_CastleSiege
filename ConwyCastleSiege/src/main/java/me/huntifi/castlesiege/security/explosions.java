package me.huntifi.castlesiege.security;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class explosions implements Listener {
	
	@EventHandler
    public void onGrenadeExplode(EntityExplodeEvent event) {
        event.blockList().clear();
        event.setCancelled(true);
    }

}
