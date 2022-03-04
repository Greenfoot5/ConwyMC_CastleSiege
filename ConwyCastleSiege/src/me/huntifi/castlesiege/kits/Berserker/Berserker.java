package me.huntifi.castlesiege.kits.Berserker;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Berserker {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }
		
		Woolheads.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		
		p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 999999, 0)));

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		BerserkerKit giveItems = new BerserkerKit();
		BerserkerVotedKit giveVotedItems = new BerserkerVotedKit();
		ItemStack ladders = giveItems.getBerserkerLadders();
		ItemStack potionB = giveItems.getBerserkerPotion();
		ItemStack sword = giveItems.getBerserkerSword();
		
		ItemStack laddersVoted = giveVotedItems.getBerserkerVotedLadders();
		ItemStack swordVoted = giveVotedItems.getBerserkerVotedSword();
		
		p.getInventory().setItem(1, potionB);
		
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		
		p.getInventory().setItem(2, ladders);
		
		} else {
			
			p.getInventory().setItem(2, laddersVoted);
			
		}
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		
		p.getInventory().setItem(0, sword);
		
		} else {
			
			p.getInventory().setItem(0, swordVoted);
			
		}
		
	}
}