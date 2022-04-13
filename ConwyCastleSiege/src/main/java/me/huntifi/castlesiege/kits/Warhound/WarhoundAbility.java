package me.huntifi.castlesiege.kits.Warhound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.huntifi.castlesiege.events.join.stats.StatsChanging;
//import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WarhoundAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");



	@SuppressWarnings("deprecation")
	@EventHandler
	public void Stunn(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Warhound")) {
				if (!LobbyPlayer.containsPlayer(whoHit)) {
					if (whoHit.getItemInHand().getType() == Material.GHAST_TEAR) {

						//if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {

							Location loc = whoWasHit.getLocation();

							int cooldown = whoHit.getCooldown(Material.GHAST_TEAR);

							float walkspeed = whoWasHit.getWalkSpeed();

							if (cooldown == 0) {
								
								AttributeInstance knockAttribute = whoWasHit.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
								

								whoHit.setCooldown(Material.GHAST_TEAR, 280);
								whoHit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You immobilised " + ChatColor.DARK_AQUA + whoWasHit.getName() + ChatColor.AQUA +  "."));
								whoWasHit.sendMessage(ChatColor.DARK_RED + "You have been immobilised by " + ChatColor.RED + whoHit.getName() + ChatColor.DARK_RED + "!");

								whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
								whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 0)));
								whoWasHit.getWorld().playSound(loc, Sound.ENTITY_WOLF_GROWL , 1, 1 );
								whoHit.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 80, 1)));
								whoWasHit.setWalkSpeed(0);
								knockAttribute.setBaseValue(2);

								new BukkitRunnable() {

									@Override
									public void run() {
										
										knockAttribute.setBaseValue(0);
										whoWasHit.setWalkSpeed(walkspeed);

									}
								}.runTaskLater(plugin, 80);
							}

						//}
					}
				}
			}
		}

	}
	
	
	@EventHandler
	public void WarhoundAbility1(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Warhound")) {

				//if (PlayerTeam.getPlayerTeam(whoHit) != PlayerTeam.getPlayerTeam(whoWasHit)) {

					whoWasHit.addPotionEffect((new PotionEffect(PotionEffectType.WITHER, 25, 5)));

				/*} else {

					ed.setCancelled(true);
				}*/

			}
		}

	}
	
	@EventHandler
	public void cantclimb(PlayerMoveEvent e) {
		
		Player p = e.getPlayer();
		
		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Warhound")) {
		
			if (p.getLocation().getBlock().getType() == Material.LADDER) {
				
				e.setCancelled(true);
				
				
			}
			
		}
	}

}
