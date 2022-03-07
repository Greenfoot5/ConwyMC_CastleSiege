package me.huntifi.castlesiege.kits.Executioner;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Executioner {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(115);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(115); }
		
		WoolHat.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		ExecutionerKit giveItems = new ExecutionerKit();
		ExecutionerVotedKit giveVotedItems = new ExecutionerVotedKit();
		
		ItemStack ladders = giveItems.getExecutionerLadders();
		ItemStack axe = giveItems.getExecutionerSword();
		ItemStack boots = giveItems.getExecutionerBoots();
		ItemStack plate = giveItems.getExecutionerChestplate();
		ItemStack legs = giveItems.getExecutionerLeggings();
		
		
		ItemStack laddersVoted = giveVotedItems.getExecutionerVotedLadders();
		ItemStack axeVoted = giveVotedItems.getExecutionerVotedAxe();
		ItemStack bootsVoted = giveVotedItems.getExecutionerVotedBoots();
		
		p.getInventory().setLeggings(legs);
		p.getInventory().setChestplate(plate);
		
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, axe);
		} else {
			
			p.getInventory().setItem(0, axeVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(1, ladders);
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}
		
	}
}
