package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * A MythicMob's listener
 */
public class MythicListener implements Listener {

    /**
     * @param e Called when a condition is loaded
     */
    @EventHandler
    public void onLoadConditions(MythicConditionLoadEvent e) {
        if (e.getConditionName().equalsIgnoreCase("sameteam")) {
            e.register(new SameTeam());
        }
    }

    @EventHandler
    public void onLoadMechanics(MythicMechanicLoadEvent e) {
        if (e.getMechanicName().equalsIgnoreCase("gotoenemyflag")) {
            e.register(new GoToEnemyFlag());
        }
    }
}
