package me.huntifi.castlesiege.kits.Ranger;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ranger {

public static void setItems(Player p) {
		
		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(105);
		p.setHealthScaled(true);
		
		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(105); }
		
		WoolHat.setHead(p);
		
		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
		}
		
		p.setHealthScaled(true);

		RangerKit giveItems1 = new RangerKit(); 
		RangerVotedKit giveVotedItems = new RangerVotedKit(); 
		
		ItemStack ChestPlate2 = giveItems1.getRangerChestplate();
		ItemStack Leggings2 = giveItems1.getRangerLeggings();
		ItemStack Arrow1 = giveItems1.getRangerArrow();
		ItemStack sword = giveItems1.getRangerSword();
		ItemStack ArcherBow = giveItems1.getRangerBow();
		ItemStack ArcherBow2 = giveItems1.getRangerVolleyBow();
		ItemStack ArcherBow3 = giveItems1.getRangerBurstBow();
		ItemStack ladders = giveItems1.getRangerLadders();
		ItemStack boots = giveItems1.getRangerBoots();
		
		ItemStack laddersVoted = giveVotedItems.getRangerVotedLadders();
		ItemStack bootsVoted = giveVotedItems.getRangerVotedBoots();
		ItemStack swordVoted = giveVotedItems.getRangerVotedSword();
		
		p.getInventory().setChestplate(ChestPlate2);
		p.getInventory().setLeggings(Leggings2);
		p.getInventory().setItem(7, Arrow1);
		p.getInventory().setItem(1, ArcherBow);
		p.getInventory().setItem(2, ArcherBow2);
		p.getInventory().setItem(3, ArcherBow3);
		
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
		p.getInventory().setItem(0, sword);
		} else {
			p.getInventory().setItem(0, swordVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
		p.getInventory().setItem(4, ladders);
		} else {
			p.getInventory().setItem(4, laddersVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
		p.getInventory().setBoots(boots);
		} else {
			p.getInventory().setBoots(bootsVoted);
		}
		
		
		
	}

}
