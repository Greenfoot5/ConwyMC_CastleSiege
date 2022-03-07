package me.huntifi.castlesiege.kits.VotedKits.Ladderman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Ladderman {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }
		
		WoolHat.setHead(p);
		
		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 9999999, 1)));
		}

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		LaddermanKit giveItems = new LaddermanKit();
		LaddermanVotedKit giveVotedItems = new LaddermanVotedKit();
		
		ItemStack legs = giveItems.getLaddermanLeggings();
		p.getInventory().setLeggings(legs);
		
		ItemStack plate = giveItems.getLaddermanChestplate();
		p.getInventory().setChestplate(plate);
		
		ItemStack ladders = giveItems.getLaddermanLadders();
		ItemStack laddersVoted = giveVotedItems.getLaddermanVotedLadders();
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(1, ladders);
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}
		
		ItemStack axe = giveItems.getLaddermanSword();
		ItemStack axeVoted = giveVotedItems.getLaddermanVotedSword();
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, axe);
		} else {
			
			p.getInventory().setItem(0, axeVoted);
		}
		
		
		ItemStack boots = giveItems.getLaddermanBoots();
		ItemStack bootsVoted = giveVotedItems.getLaddermanVotedBoots();
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}
		
		
	}
	
}
