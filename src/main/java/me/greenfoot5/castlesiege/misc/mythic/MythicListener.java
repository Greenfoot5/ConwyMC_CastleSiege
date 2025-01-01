package me.greenfoot5.castlesiege.misc.mythic;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
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
    public void onLoadMechanics(MythicConditionLoadEvent e) {
        if (e.getConditionName().equalsIgnoreCase("sameteam")) {
            e.register(new SameTeam());
        }
    }
}
