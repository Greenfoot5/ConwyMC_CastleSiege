package me.huntifi.castlesiege.kits.Crossbowman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Crossbowman {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(105);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(105); }
		
		WoolHat.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 999999, 1)));

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		CrossbowmanKit giveItems = new CrossbowmanKit();
		CrossbowmanVotedKit giveVotedItems = new CrossbowmanVotedKit();
		
		ItemStack plate = giveItems.getXbowmanChestplate();
		ItemStack xbow = giveItems.getXbowmanBow();
		ItemStack Arrow1 = giveItems.getXbowmanArrow();
		ItemStack legs = giveItems.getXbowmanLeggings();
		
		ItemStack ladders = giveItems.getXbowmanLadders();
		ItemStack boots = giveItems.getXbowmanBoots();
		
		ItemStack laddersVoted = giveVotedItems.getXbowmanVotedLadders();
		ItemStack bootsVoted = giveVotedItems.getXbowmanVotedBoots();
		
		
		p.getInventory().setItem(0, xbow);
		p.getInventory().setItem(7, Arrow1);
		p.getInventory().setLeggings(legs);
		p.getInventory().setChestplate(plate);
		
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
