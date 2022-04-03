package me.huntifi.castlesiege.kits.Halberdier;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class HalberdierAbility implements Listener, Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void antiCav(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if (StatsChanging.getKit(whoHit.getUniqueId()).equalsIgnoreCase("Halberdier")) {
				if (!LobbyPlayer.containsPlayer(whoHit)) {

					if (whoWasHit.isInsideVehicle()) {

						if (whoWasHit.getHealth() > 10) {

							whoWasHit.damage(10);

						}
					}

				}
			}
		}

	}

	public static ArrayList<Player> jumpers = new ArrayList<Player>();

	@Override
	public void run() {

		for (Player halbs : Bukkit.getOnlinePlayers()) {
			
			if (!jumpers.contains(halbs)) {
				
				if (StatsChanging.getKit(halbs.getUniqueId()).equalsIgnoreCase("Halberdier")) {
					
					jumpers.add(halbs);
					
				}
				

			} else if (jumpers.contains(halbs)) {
				
				if (!StatsChanging.getKit(halbs.getUniqueId()).equalsIgnoreCase("Halberdier")) {
					
					jumpers.remove(halbs);
					
				}
				
			}
			
			if (jumpers.contains(halbs)) {
				
				halbs.setFoodLevel(4);
				
			}
		}
	}


	@EventHandler
	public void takeMoreDamage(EntityDamageByEntityEvent ed) {

		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();
			if (!LobbyPlayer.containsPlayer(whoHit)) {

				if (StatsChanging.getKit(whoWasHit.getUniqueId()).equalsIgnoreCase("Halberdier")) {

					if (whoWasHit.getLastDamageCause().getCause() == DamageCause.PROJECTILE) {

						if (whoWasHit.getHealth() > 0) {

							whoWasHit.damage(5);


						}
					}
				}


			}
		}

	}

}
