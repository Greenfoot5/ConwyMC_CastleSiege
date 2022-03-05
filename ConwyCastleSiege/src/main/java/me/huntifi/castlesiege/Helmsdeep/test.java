package me.huntifi.castlesiege.Helmsdeep;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.huntifi.castlesiege.maps.MapController;

public class test implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

				Block target = p.getTargetBlockExact(50);

				if (target.getState() instanceof Sign) {
					
					p.sendMessage("almost");
					
					Sign s = (Sign) target.getState();
					
					if (s.getLine(1).equalsIgnoreCase("Camp")) {
						
						p.sendMessage("found fix finally");
						
					}

				}
		}

	}

}
