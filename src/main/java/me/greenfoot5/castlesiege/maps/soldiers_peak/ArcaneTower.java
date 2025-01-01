package me.greenfoot5.castlesiege.maps.soldiers_peak;

import me.greenfoot5.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Attempts to reduce lag on Soldier's Peak
 */
public class ArcaneTower implements Listener {

    /**
     * @param e The ChunkLoadEvent for Soldier's Peak
     */
    @EventHandler
    public void onChunk(ChunkLoadEvent e) {

        if (MapController.getCurrentMap().worldName.equals("SoldiersPeak")) {

            if (e.getChunk().getWorld().equals(Bukkit.getWorld("SoldiersPeak"))) {

                // player headroom
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(0,-3, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(-1,-3, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(-2,-3, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(-2,-4, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(-1,-4, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(0,-4, true);

                // arcane tower
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(5,-4, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(5,-5, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(3,-4, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(3,-5, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(4,-5, true);
                Bukkit.getWorld("SoldiersPeak").setChunkForceLoaded(4,-4, true);
            }

        }

    }

}
