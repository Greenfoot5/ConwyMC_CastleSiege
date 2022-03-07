package me.huntifi.castlesiege.kits.FireArcher;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Firearcher {

	public static void setItems(Player p) {

		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(105);
		p.setHealthScaled(true);

		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(105); }

		WoolHat.setHead(p);

		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 9999999, 0)));
		}

		FirearcherKit giveItems = new FirearcherKit(); 
		FirearcherVotedKit giveVotedItems = new FirearcherVotedKit(); 

		ItemStack FireArcherChestPlate = giveItems.getFirearcherChestplate();
		p.getInventory().setChestplate(FireArcherChestPlate);
		ItemStack FireArcherLeggings = giveItems.getFirearcherLeggings();
		p.getInventory().setLeggings(FireArcherLeggings);
		ItemStack FireArcherArrow = giveItems.getFirearcherArrow();
		p.getInventory().setItem(6, FireArcherArrow);
		ItemStack FireArcherBow = giveItems.getFirearcherBow();
		p.getInventory().setItem(0, FireArcherBow);

		ItemStack firepit = giveItems.getFirearcherFirepit();
		ItemStack ladders = giveItems.getFirearcherLadders();
		ItemStack boots = giveItems.getFirearcherBoots();

		ItemStack firepitVoted = giveVotedItems.getFirearcherVotedFirepit();
		ItemStack laddersVoted = giveVotedItems.getFirearcherVotedLadders();
		ItemStack bootsVoted = giveVotedItems.getFirearcherVotedBoots();



		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			p.getInventory().setItem(3, firepit);
			
		} else {
			
			p.getInventory().setItem(3, firepitVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
			p.getInventory().setItem(2, ladders);
		} else {
			
			p.getInventory().setItem(2, laddersVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
			p.getInventory().setBoots(boots);
			
		} else {
			
			p.getInventory().setBoots(bootsVoted);
		}

	}

}
