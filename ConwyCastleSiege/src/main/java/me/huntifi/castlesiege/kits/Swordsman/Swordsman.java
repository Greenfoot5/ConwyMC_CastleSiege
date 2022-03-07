package me.huntifi.castlesiege.kits.Swordsman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Swordsman {

	public static void setItems(Player p) {
		

		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(120);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(120); }
		
		WoolHat.setHead(p);

		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		SwordsmanKit giveItems = new SwordsmanKit();
		SwordsmanVotedKit giveVotedItems = new SwordsmanVotedKit(); 

		ItemStack chestPlate = giveItems.getSwordsmanChestplate();
		p.getInventory().setChestplate(chestPlate);
		
		ItemStack leggings = giveItems.getSwordsmanLeggings();
		p.getInventory().setLeggings(leggings);
		
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {

		ItemStack IronSword = giveItems.getSwordsmanSword();
		p.getInventory().setItem(0, IronSword);
		
		} else {
			
			ItemStack IronSword = giveVotedItems.getSwordsmanVotedSword();
			p.getInventory().setItem(0, IronSword);
			
		}

		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		
		ItemStack ladders = giveItems.getSwordsmanLadders();
		p.getInventory().setItem(1, ladders);

		} else {
			
			ItemStack ladders = giveVotedItems.getSwordsmanVotedLadders();
			p.getInventory().setItem(1, ladders);
			
		}
		
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		
		ItemStack boots = giveItems.getSwordsmanBoots();
		p.getInventory().setBoots(boots);

		} else {
			
			ItemStack boots = giveVotedItems.getSwordsmanVotedBoots();
			p.getInventory().setBoots(boots);
			
			
		}
	}

}

