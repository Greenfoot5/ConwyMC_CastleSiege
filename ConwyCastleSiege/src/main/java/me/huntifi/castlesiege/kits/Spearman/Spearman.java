package me.huntifi.castlesiege.kits.Spearman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Spearman {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(115);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(115); }
		
		WoolHat.setHead(p);

		SpearmanKit giveItems = new SpearmanKit(); 
		SpearmanVotedKit giveVotedItems = new SpearmanVotedKit();
	
		ItemStack SpearmanChestPlate = giveItems.getSpearmanChestplate();
		ItemStack SpearmanLeggings = giveItems.getSpearmanLeggings();
		ItemStack ladders = giveItems.getSpearmanLadders();
		ItemStack boots = giveItems.getSpearmanBoots();
		ItemStack spears = giveItems.getSpearmanSpears();
		
		ItemStack laddersVoted = giveVotedItems.getSpearmanVotedLadders();
		ItemStack bootsVoted = giveVotedItems.getSpearmanVotedBoots();
		ItemStack spearsVoted = giveVotedItems.getSpearmanVotedSpears();
		
		
		p.getInventory().setChestplate(SpearmanChestPlate);
		p.getInventory().setLeggings(SpearmanLeggings);
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		
		p.getInventory().setItem(0, spears);
		
		} else {
			
			p.getInventory().setItem(0, spearsVoted);
			
		}
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		
		p.getInventory().setBoots(boots);
		
		} else {
			
			p.getInventory().setBoots(bootsVoted);
			
		}
		
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		
		p.getInventory().setItem(1, ladders);
		
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
			
		}

	}

}
