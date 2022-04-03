package me.huntifi.castlesiege.kits.Maceman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MacemanAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");



	@SuppressWarnings("deprecation")
	@EventHandler
	public void Stunn(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Maceman")) {
				if (!LobbyPlayer.containsPlayer(whoHit)) {
					if (whoHit.getItemInHand().getType() == Material.IRON_SHOVEL) {

						if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {

							Location loc = whoWasHit.getLocation();

							int cooldown = whoHit.getCooldown(Material.IRON_SHOVEL);

							if (cooldown == 0) {

								whoHit.setCooldown(Material.IRON_SHOVEL, 200);
								whoHit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You have stunned " + ChatColor.DARK_AQUA + whoWasHit.getName() + ChatColor.AQUA +  "."));
								whoWasHit.sendMessage(ChatColor.DARK_RED + "You have been stunned by " + ChatColor.RED + whoHit.getName() + ChatColor.DARK_RED + "!");

								if (!whoWasHit.isBlocking()) {
									
									whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 50, 1)));
									whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 30, 1)));
									whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 50, 1)));
									if (whoWasHit.getHealth() - 16 >= 16) {
										
										whoWasHit.damage(16);
										
									}
									whoWasHit.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR , 1, 1 );
									
								} else {
									
									whoHit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + whoWasHit.getName() + " Blocked your stunn."));
									whoWasHit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You blocked" + ChatColor.DARK_AQUA + whoHit.getName() + ChatColor.AQUA + "'s Stunn"));
								}
							}

						} 
					}
				}
			}
		}

	}
}
