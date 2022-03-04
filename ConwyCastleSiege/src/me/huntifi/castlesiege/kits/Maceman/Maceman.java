package me.huntifi.castlesiege.kits.Maceman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Maceman {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }
		
		Woolheads.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		MacemanKit giveItems = new MacemanKit();
		MacemanVotedKit giveVotedItems = new MacemanVotedKit();
		
		ItemStack legs = giveItems.getMacemanLeggings();
		ItemStack plate = giveItems.getMacemanChestplate();
		
		ItemStack boots = giveItems.getMacemanBoots();
		ItemStack mace = giveItems.getMacemanSword();
		ItemStack ladders = giveItems.getMacemanLadders();
		
		ItemStack bootsVoted = giveVotedItems.getMacemanVotedBoots();
		ItemStack maceVoted = giveVotedItems.getMacemanVotedSword();
		ItemStack laddersVoted = giveVotedItems.getMacemanVotedLadders();
		
		p.getInventory().setLeggings(legs);
		p.getInventory().setChestplate(plate);
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			
		p.getInventory().setItem(0, mace);
		
		} else {
			
			p.getInventory().setItem(0, maceVoted);
		}
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
			
		p.getInventory().setItem(1, ladders);
		
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
			
		p.getInventory().setBoots(boots);
		
		} else {
			
			p.getInventory().setBoots(bootsVoted);
			
		}
		
	}
}
