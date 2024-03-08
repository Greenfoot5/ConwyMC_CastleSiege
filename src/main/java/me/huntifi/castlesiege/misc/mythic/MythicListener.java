package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicListener implements Listener {

    @EventHandler
    public void onLoadMechanics(MythicConditionLoadEvent e) {
        if (e.getConditionName().equalsIgnoreCase("sameteam")) {
            e.register(new SameTeam());
        }
    }

}
