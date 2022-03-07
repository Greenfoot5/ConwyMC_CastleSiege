package me.huntifi.castlesiege.kits.Skirmisher;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Skirmisher {

	public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }
		
		WoolHat.setHead(p);

		if (!LobbyPlayer.containsPlayer(p)) {

			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
			p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 9999999, 0)));

		}

		SkirmisherKit giveItems = new SkirmisherKit(); 
		SkirmisherVotedKit giveVotedItems = new SkirmisherVotedKit(); 
		
		ItemStack ChestPlate = giveItems.getSkirmisherChestplate();
		p.getInventory().setChestplate(ChestPlate);
		ItemStack Leggings = giveItems.getSkirmisherLeggings();
		p.getInventory().setLeggings(Leggings);

		ItemStack sword = giveItems.getSkirmisherSword();
		ItemStack swordVoted = giveVotedItems.getSkirmisherVotedSword();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, sword);
		} else {
			
			p.getInventory().setItem(0, swordVoted);
		}

		ItemStack ladders = giveItems.getSkirmisherLadders();
		ItemStack laddersVoted = giveVotedItems.getSkirmisherVotedLadders();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(1, ladders);
		} else {
			
			p.getInventory().setItem(1, laddersVoted);
		}

		ItemStack boots = giveItems.getSkirmisherBoots();
		ItemStack bootsVoted = giveVotedItems.getSkirmisherVotedBoots();
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}


	}

}
