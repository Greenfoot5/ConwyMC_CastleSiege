package me.huntifi.castlesiege.kits.Halberdier;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Halberdier {

public static void setItems(Player p) {
	
	AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
	healthAttribute.setBaseValue(160);
	p.setHealthScaled(true);
	
	p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(2);
	
	if (LobbyPlayer.containsPlayer(p)) { p.setHealth(160); }
		
		Woolheads.setHead(p);
		
		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 999999, 2)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, 3)));
		p.addPotionEffect((new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 0)));

		p.setHealthScaled(true);
		
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		
		HalberdierKit giveItems = new HalberdierKit();
		HalberdierVotedKit giveVotedItems = new HalberdierVotedKit();
		
		ItemStack axe = giveItems.getHalberdierSword();
		ItemStack legs = giveItems.getHalberdierLeggings();
		ItemStack boots = giveItems.getHalberdierBoots();
		ItemStack plate = giveItems.getHalberdierChestplate();
		
		ItemStack bootsVoted = giveVotedItems.getHalberdierVotedBoots();
		ItemStack axeVoted = giveVotedItems.getHalberdierVotedAxe();
		
		p.getInventory().setChestplate(plate);
		p.getInventory().setLeggings(legs);
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			
			p.getInventory().setItem(0, axe);
			
		} else {
			
			p.getInventory().setItem(0, axeVoted);
		}
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
			
			p.getInventory().setBoots(boots);
			
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}
		
		
	}
}
