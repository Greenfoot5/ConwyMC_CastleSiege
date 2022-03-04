package me.huntifi.castlesiege.kits.VotedKits.Scout;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Scout {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(100);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(100); }
		
		Woolheads.setHead(p);
		
		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 9999999, 1)));
		}

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		ScoutKit giveItems = new ScoutKit();
		ScoutVotedKit giveVotedItems = new ScoutVotedKit();
		
		ItemStack ladders = giveItems.getScoutLadders();
		ItemStack laddersVoted = giveVotedItems.getScoutVotedLadders();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
			
		p.getInventory().setItem(1, ladders);
		
		} else {
			p.getInventory().setItem(1, laddersVoted);
		}
		
		ItemStack axe = giveItems.getScoutSword();
		ItemStack axeVoted = giveVotedItems.getScoutVotedSword();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			
		p.getInventory().setItem(0, axe);
		
		} else {
			p.getInventory().setItem(0, axeVoted);
		}
		
		ItemStack legs = giveItems.getScoutLeggings();
		p.getInventory().setLeggings(legs);
		
		ItemStack boots = giveItems.getScoutBoots();
		ItemStack bootsVoted = giveVotedItems.getScoutVotedBoots();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
			
		p.getInventory().setBoots(boots);
		
		} else {
			p.getInventory().setBoots(bootsVoted);
		}
		
		ItemStack plate = giveItems.getScoutChestplate();
		p.getInventory().setChestplate(plate);
		
	}
	
}
