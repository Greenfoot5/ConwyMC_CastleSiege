package me.huntifi.castlesiege.maps;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class Woolmap implements Listener {

	WoolMapBlock[] woolMapBlocks;

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player player = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

		if (e.getClickedBlock().getState() instanceof Sign) {

			Sign s = (Sign) e.getClickedBlock().getState();
			for (WoolMapBlock block : woolMapBlocks) {
				if (s.getLine(1).equalsIgnoreCase(block.flag.name)) {
					block.SpawnPlayer(player);
				}
			}
		}
	}
}