package me.huntifi.castlesiege.events;

import me.huntifi.castlesiege.kits.kits.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Movement implements Listener {

    /**
     * Prevent animals from climbing ladders
     * @param e The event called when moving on a ladder
     */
    @EventHandler
    public void onClimb(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!Kit.equippedKits.get(p.getUniqueId()).canClimb &&
                p.getLocation().getBlock().getType() == Material.LADDER &&
                e.getTo() != null && e.getTo().getY() - e.getFrom().getY() > 0) {
            e.setCancelled(true);
        }
    }
}
