package me.huntifi.castlesiege.events.combat;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class EnterBed implements Listener {

    public void OnEnterBed(PlayerBedEnterEvent event) {
        event.useBed();
    }

    public void OnLeaveBed(PlayerBedLeaveEvent event) {
        event.setSpawnLocation(false);
    }
}
