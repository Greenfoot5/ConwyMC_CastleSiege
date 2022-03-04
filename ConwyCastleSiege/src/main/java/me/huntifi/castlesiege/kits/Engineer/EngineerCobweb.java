package me.huntifi.castlesiege.kits.Engineer;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class EngineerCobweb implements Listener {

	public static ArrayList<Block> Cobwebs = new ArrayList<Block>();

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

			if (StatsChanging.getKit(uuid).equalsIgnoreCase("Engineer")) {

				if (e.getBlockPlaced().getType() == Material.COBWEB) {
					
					e.setCancelled(false);
					
					Block web = e.getBlockPlaced();
					
					if (!Cobwebs.contains(web)) {  Cobwebs.add(web);  }
					
				}
			}

		}

	}
	
	
	@EventHandler
	public void onDestroy(BlockBreakEvent e) {

		Player p = e.getPlayer();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

				if (e.getBlock().getType() == Material.COBWEB) {
					
					Block web = e.getBlock();
					
					if (Cobwebs.contains(web)) {  
		
					web.setType(Material.AIR);
					Cobwebs.remove(web); 
					
					}
					
				}

		}

	}

}
