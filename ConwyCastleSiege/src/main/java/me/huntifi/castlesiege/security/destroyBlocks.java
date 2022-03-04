package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class destroyBlocks implements Listener {

	@EventHandler
	public void onBlockPlaced(BlockBreakEvent e) {

		Player p = e.getPlayer();

		if (p.getGameMode() == GameMode.CREATIVE) {

			e.setCancelled(false);

		} else if (p.getGameMode() != GameMode.CREATIVE) {
			
			e.setCancelled(true);
			
		}


	}

}
