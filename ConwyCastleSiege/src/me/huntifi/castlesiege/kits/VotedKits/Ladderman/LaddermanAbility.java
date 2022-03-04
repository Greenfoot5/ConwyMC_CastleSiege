package me.huntifi.castlesiege.kits.VotedKits.Ladderman;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.ladders.LadderEvent;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class LaddermanAbility implements Listener {

	@EventHandler 
	public void onLadderBreak(BlockBreakEvent e) {

		Player p = e.getPlayer();

		Block ladder = e.getBlock();
		
		if (!LobbyPlayer.containsPlayer(p)) {

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Ladderman")) {

				if (e.getBlock().getType() == Material.LADDER) {
					
					e.getBlock().setType(Material.AIR);
					
					p.getInventory().addItem(new ItemStack(Material.LADDER));

					if(LadderEvent.Ladders.contains(ladder)){ LadderEvent.Ladders.remove(ladder);}

				}

			} 

		} 

	}

}
