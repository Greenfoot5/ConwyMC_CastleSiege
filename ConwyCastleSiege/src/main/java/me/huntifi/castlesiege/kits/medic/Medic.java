package me.huntifi.castlesiege.kits.medic;

import me.huntifi.castlesiege.kits.WoolHat;
import me.huntifi.castlesiege.voting.VotesChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Medic {

	public static void setItems(Player p) {

		AttributeInstance healthAttribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(100);
		p.setHealthScaled(true);

		if (LobbyPlayer.containsPlayer(p)) { p.setHealth(100); }

		WoolHat.setHead(p);

		if (!LobbyPlayer.containsPlayer(p)) {
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 9999999, 0)));
		}

		p.setHealthScaled(true);

		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);

		MedicKit giveItems = new MedicKit();
		MedicVotedKit giveVotedItems = new MedicVotedKit();


		ItemStack ladders = giveItems.getMedicLadders();
		ItemStack cakes = giveItems.getMedicCake();
		ItemStack bandage = giveItems.getMedicBandage();
		ItemStack sword = giveItems.getMedicSword();
		ItemStack legs = giveItems.getMedicLeggings();
		ItemStack boots = giveItems.getMedicBoots();
		ItemStack plate = giveItems.getMedicChestplate();


		ItemStack laddersVoted = giveVotedItems.getMedicVotedLadders();
		ItemStack swordVoted = giveVotedItems.getMedicVotedSword();
		ItemStack bootsVoted = giveVotedItems.getMedicVotedBoots();

		p.getInventory().setChestplate(plate);
		p.getInventory().setLeggings(legs);
		p.getInventory().setItem(1, bandage);
		p.getInventory().setItem(2, cakes);


		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {

			p.getInventory().setItem(0, sword);

		} else {

			p.getInventory().setItem(0, swordVoted);
		}

		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#3")) {

			p.getInventory().setItem(3, ladders);

		} else {

			p.getInventory().setItem(3, laddersVoted);
		}
		if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#2")) {

			p.getInventory().setBoots(boots);

		} else {


			p.getInventory().setBoots(bootsVoted);
		}
	}
}

