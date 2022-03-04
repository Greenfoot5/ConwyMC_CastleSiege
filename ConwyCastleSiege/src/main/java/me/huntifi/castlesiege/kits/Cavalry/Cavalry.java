package me.huntifi.castlesiege.kits.Cavalry;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Cavalry {

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

		CavalryKit giveItems = new CavalryKit();
		CavalryVotedKit giveVotedItems = new CavalryVotedKit();


		ItemStack ladders = giveItems.getCavalryLadders();
		ItemStack sword = giveItems.getCavalrySword();
		ItemStack horse = giveItems.getCavalryHorse();
		ItemStack legs = giveItems.getCavalryLeggings();
		ItemStack boots = giveItems.getCavalryBoots();
		ItemStack plate = giveItems.getCavalryChestplate();
		
		ItemStack laddersVoted = giveVotedItems.getCavalryVotedLadders();
		ItemStack swordVoted = giveVotedItems.getCavalryVotedSword();
		ItemStack bootsVoted = giveVotedItems.getCavalryVotedBoots();
		
		p.getInventory().setItem(2, horse);
		p.getInventory().setLeggings(legs);
		p.getInventory().setChestplate(plate);
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(1, ladders);
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, sword);
		} else {
			
			p.getInventory().setItem(0, swordVoted);	
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}

	}
}
