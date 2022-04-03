package me.huntifi.castlesiege.kits.Berserker;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.voting.VotesChanging;

public class BerserkerAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	public static HashMap<UUID, Boolean> Berserker = new HashMap<UUID, Boolean>();

	@EventHandler
	public void Charge(PlayerInteractEvent e) {
		Player p = e.getPlayer();


		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Berserker")) {
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (e.getItem().getType() == Material.POTION) {

					BerserkerKit giveItems = new BerserkerKit(); 
					ItemStack swordN = giveItems.getBerserkerSword();
					ItemStack swordD = giveItems.getBerserkerSword2();
					
					BerserkerVotedKit giveVotedItems = new BerserkerVotedKit(); 
					ItemStack swordVotedN = giveVotedItems.getBerserkerVotedSword();
					ItemStack swordVotedD = giveVotedItems.getBerserkerVotedDamageSword();

					

					new BukkitRunnable() {

						@Override
						public void run() {

							p.addPotionEffect((new PotionEffect(PotionEffectType.CONFUSION, 320, 0)));
						}
					}.runTaskLater(plugin, 80);

					p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 400, 1)));
					p.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 0)));
					
					if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {
					
					p.getInventory().setItem(0, swordD);
					
					} else {
						
						p.getInventory().setItem(0, swordVotedD);
						
					}

					p.getInventory().getItemInMainHand().setType(Material.GLASS_BOTTLE);


					new BukkitRunnable() {

						@Override
						public void run() {

							if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Berserker")) {
								
								if (!VotesChanging.getVotes(p.getUniqueId()).contains("V#1")) {

								p.getInventory().setItem(0, swordN);
								
								} else {
									
									p.getInventory().setItem(0, swordVotedN);
									
								}

							}

						}
					}.runTaskLater(plugin, 400);



				} 
			}
		}



	}
}
