package me.huntifi.castlesiege.security;

import org.bukkit.GameMode;
import org.bukkit.Material;
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

			if (e.getBlock().getType() == Material.LADDER) {
				e.getBlock().setType(Material.AIR);
			} else {
				e.setCancelled(true);
			}
			
		}


	}

}
