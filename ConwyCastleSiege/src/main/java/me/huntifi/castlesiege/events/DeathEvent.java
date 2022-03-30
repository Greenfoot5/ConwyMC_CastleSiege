package me.huntifi.castlesiege.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        // TODO - Let player know they died
        // TODO - Add death stats
        // TODO - Refill equipment
        // TODO - Set spawn point
    }
}
