package me.huntifi.castlesiege.kits.Engineer;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class Engineer {

	public static void setItems(Player p) {

		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(110);
		p.setHealthScaled(true);

		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(110); }

		WoolHat.setHead(p);

		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 9999999, 0)));
		}

		p.setHealthScaled(true);

		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);

		EngineerKit giveItems = new EngineerKit();
		EngineerVotedKit giveVotedItems = new EngineerVotedKit();


		ItemStack ladders = giveItems.getEngineerLadders();
		ItemStack traps = giveItems.getEngineerTraps();
		ItemStack cobwebs = giveItems.getEngineerCobwebs();
		ItemStack planks = giveItems.getEngineerPlanks();
		ItemStack cobble = giveItems.getEngineerStone();
		ItemStack sword = giveItems.getEngineerSword();
		ItemStack legs = giveItems.getEngineerLeggings();
		ItemStack boots = giveItems.getEngineerBoots();
		ItemStack plate = giveItems.getEngineerChestplate();


		ItemStack laddersVoted = giveVotedItems.getEngineerVotedLadders();
		ItemStack swordVoted = giveVotedItems.getEngineerVotedSword();
		ItemStack bootsVoted = giveVotedItems.getEngineerVotedBoots();

		p.getInventory().setChestplate(plate);
		p.getInventory().setLeggings(legs);
		p.getInventory().setItem(5, cobble);
		p.getInventory().setItem(4, planks);
		p.getInventory().setItem(3, cobwebs);
		p.getInventory().setItem(2, traps);

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
			
			p.getInventory().setItem(0, sword);
			
		} else {

			p.getInventory().setItem(0, swordVoted);
		}

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {
			
			p.getInventory().setItem(1, ladders);
			
		} else {
			
			p.getInventory().setItem(0, laddersVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {
			
			p.getInventory().setBoots(boots);
			
		} else {
			
			
			p.getInventory().setBoots(bootsVoted);
		}
	}
}
