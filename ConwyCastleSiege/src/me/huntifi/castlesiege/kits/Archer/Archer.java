package me.huntifi.castlesiege.kits.Archer;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.huntifi.castlesiege.kits.Woolheads;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Archer {

	public static void setItems(Player p) {

		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(105);
		p.setHealthScaled(true);

		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(105); }

		Woolheads.setHead(p);

		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		p.setHealthScaled(true);

		ArcherKit giveItems = new ArcherKit(); 
		ArcherVotedKit giveVotedItems = new ArcherVotedKit();

		ItemStack ChestPlate2 = giveItems.getArcherChestplate();
		p.getInventory().setChestplate(ChestPlate2);

		ItemStack Leggings2 = giveItems.getArcherLeggings();
		p.getInventory().setLeggings(Leggings2);

		ItemStack Arrow1 = giveItems.getArcherArrow();
		p.getInventory().setItem(7, Arrow1);

		ItemStack ArcherBow = giveItems.getArcherBow();
		p.getInventory().setItem(1, ArcherBow);

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {

			ItemStack sword =giveItems.getArcherSword();
			p.getInventory().setItem(0, sword);

		} else {

			ItemStack sword = giveVotedItems.getArcherVotedSword();
			p.getInventory().setItem(0, sword);

		}

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {

			ItemStack boots = giveItems.getArcherBoots();
			p.getInventory().setBoots(boots);

		}  else {

			ItemStack boots = giveVotedItems.getArcherVotedBoots();
			p.getInventory().setBoots(boots);
			
		}

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {

			ItemStack ladders = giveItems.getArcherLadders();
			p.getInventory().setItem(2, ladders);

		} else {

			ItemStack ladders = giveVotedItems.getArcherVotedLadders();
			p.getInventory().setItem(2, ladders);

		}



	}

}
