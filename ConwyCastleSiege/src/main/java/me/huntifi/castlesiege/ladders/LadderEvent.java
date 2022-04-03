package me.huntifi.castlesiege.ladders;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class LadderEvent implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static ArrayList<Block> Ladders = new ArrayList<Block>();

	//public static HashMap<Player, List<Block>> LadderPlace = new HashMap<Player, List<Block>>();

	//public static ArrayList<Block> ladderBreak = new ArrayList<Block>();

	@EventHandler (priority = EventPriority.MONITOR)
	public void onLadderPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if (e.getBlockPlaced().getType() == Material.LADDER) {

					e.setCancelled(false);

					Block ladder = e.getBlockPlaced();

					if(!Ladders.contains(ladder)){ Ladders.add(ladder);}


				} 
			} 
		} 
	}


	//public static List <Block> returnPlacedLadder() {

	//	List<Block> highest1 = null;

	//	for (Entry<Player, List<Block>> entry : LadderPlace.entrySet()) {
	//
	//		highest1 = entry.getValue();
	//
	//	}

	//	return highest1;
	//	}


	//public static Player returnLadderPlacer() {


	//	Player placer = null;

	//	for (Entry<Player, List<Block>> entry : LadderPlace.entrySet()) {

	//		placer = entry.getKey();

	//	}

	//	return placer;
	//	}




	@EventHandler (priority = EventPriority.MONITOR)
	public void onLadderBreak(BlockBreakEvent e) {

		Player p = e.getPlayer();

		Block ladder = e.getBlock();

		if (!(p.getGameMode() == GameMode.CREATIVE)) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if (e.getBlock().getType() == Material.LADDER) {

					e.getBlock().setType(Material.AIR);
					
					if(Ladders.contains(ladder)){ Ladders.remove(ladder);}

				}
			} 
		} 
	}
}
