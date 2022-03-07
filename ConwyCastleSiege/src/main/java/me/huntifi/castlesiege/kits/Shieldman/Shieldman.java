package me.huntifi.castlesiege.kits.Shieldman;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Shieldman {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(120);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(120); }
		
		WoolHat.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.addPotionEffect((new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 0)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 9999999, 0)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 9999999, 0)));
		
		ShieldmanKit giveItems = new ShieldmanKit(); 
		ShieldmanVotedKit giveVotedItems = new ShieldmanVotedKit(); 
		
		ItemStack ChestPlate = giveItems.getShieldmanChestplate();
		p.getInventory().setChestplate(ChestPlate);
		ItemStack Leggings = giveItems.getShieldmanLeggings();
		p.getInventory().setLeggings(Leggings);
		ItemStack Shield = giveItems.getShieldmanShield();
		p.getInventory().setItemInOffHand(Shield);

		ItemStack sword = giveItems.getShieldmanSword();
		ItemStack swordVoted = giveVotedItems.getShieldmanVotedSword();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, sword);
		} else {
			
			p.getInventory().setItem(0, swordVoted);
		}

		ItemStack ladders = giveItems.getShieldmanLadders();
		ItemStack laddersVoted = giveVotedItems.getShieldmanVotedLadders();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(1, ladders);
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}
		
		ItemStack boots = giveItems.getShieldmanBoots();
		ItemStack bootsVoted = giveVotedItems.getShieldmanVotedBoots();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}
	}
	
}
