package me.huntifi.castlesiege.security;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class CustomFallDamage implements Listener {

	@EventHandler
	public void onFallDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();
			
			Location Loc = p.getLocation();
			
			Location BlockLoc = new Location(p.getWorld(), Loc.getX(), Loc.getY() - 1, Loc.getZ());

			if (!LobbyPlayer.containsPlayer(p)) {

				if (e.getCause() == DamageCause.FALL) {
					
					if (BlockLoc.getBlock().getType().equals(Material.HAY_BLOCK)) {
						
						e.setCancelled(true);
						
					}

					Double damage = e.getDamage();
					
					if (damage == 1) {
						e.setDamage(5);
					}
					
					else if (damage == 2) {
						e.setDamage(10);
					}
					
					else if (damage == 3) {
						e.setDamage(15);
					}
					
					else if (damage == 4) {
						e.setDamage(20);
					}
					
					else if (damage == 5) {
						e.setDamage(25);
					}
					
					else if (damage == 6) {
						e.setDamage(30);
					}
					
					else if (damage == 7) {
						e.setDamage(35);
					}
					
					else if (damage == 8) {
						e.setDamage(40);
					}
					
					else if (damage == 9) {
						e.setDamage(45);
					}
					
					else if (damage == 10) {
						e.setDamage(50);
					}
					
					else if (damage == 11) {
						e.setDamage(55);
					}
					
					else if (damage == 12) {
						e.setDamage(60);
					}
					
					else if (damage == 13) {
						e.setDamage(65);
					}
					
					else if (damage == 14) {
						e.setDamage(70);
					}
					
					else if (damage == 15) {
						e.setDamage(75);
					}
					
					else if (damage == 16) {
						e.setDamage(80);
					}
					
					else if (damage == 17) {
						e.setDamage(85);
					}
					
					else if (damage == 18) {
						e.setDamage(90);
					}
					
					else if (damage == 19) {
						e.setDamage(95);
					}
					
					else if (damage == 20) {
						e.setDamage(100);
					}
					
					else if (damage >= 21) {
						e.setDamage(105);
					}
					
					else if (damage >= 22) {
						e.setDamage(105);
					}
					
					else if (damage >= 23) {
						e.setDamage(110);
					}
					
					else if (damage >= 24) {
						e.setDamage(115);
					}
					
					else if (damage >= 25 || damage >= 26 || damage >= 27) {
						e.setDamage(118);
					}
					
					else if (damage >= 28 || damage >= 29 || damage >= 30) {
						e.setDamage(125);
					}
					
					else if (damage >= 31 || damage >= 32 || damage >= 33) {
						e.setDamage(150);
					}
					
					else if (damage >= 34 || damage >= 35 || damage >= 36) {
						e.setDamage(155);
					} 
					
					else if (damage > 37) {
						e.setDamage(170);
					}
					

				}
				
			} else if (LobbyPlayer.containsPlayer(p)) {
				
				if (e.getCause() == DamageCause.FALL) {

					e.setCancelled(true);

				}
				
				
			}

		}

	}
	
	
	

}

