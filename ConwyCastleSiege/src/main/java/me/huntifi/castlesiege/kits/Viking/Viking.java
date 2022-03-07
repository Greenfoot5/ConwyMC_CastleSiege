package me.huntifi.castlesiege.kits.Viking;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Viking {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }
		
		WoolHat.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 999999, 0)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 0)));

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		VikingKit giveItems = new VikingKit();
		VikingVotedKit giveVotedItems = new VikingVotedKit();

		ItemStack ladders = giveItems.getVikingLadders();
		ItemStack axe = giveItems.getVikingAxe();
		ItemStack legs = giveItems.getVikingLeggings();
		ItemStack plate = giveItems.getVikingChestplate();
		ItemStack boots = giveItems.getVikingBoots();
		
		ItemStack bootsVoted = giveVotedItems.getVikingVotedBoots();
		ItemStack laddersVoted = giveVotedItems.getVikingVotedLadders();
		ItemStack axeVoted = giveVotedItems.getVikingVotedAxe();
		
		p.getInventory().setLeggings(legs);
		p.getInventory().setChestplate(plate);
		
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
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			
		p.getInventory().setItem(0, axe);
		
		} else {
			
			p.getInventory().setItem(0, axeVoted);
		}
		
	}
}
