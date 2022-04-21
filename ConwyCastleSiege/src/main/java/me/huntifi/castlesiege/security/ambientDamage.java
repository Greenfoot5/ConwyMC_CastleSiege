package me.huntifi.castlesiege.security;

import me.huntifi.castlesiege.events.combat.InCombat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ambientDamage implements Listener {


	@EventHandler
	public void onLavaDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (InCombat.isPlayerInLobby(p.getUniqueId())) {

				if (e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.CONTACT || e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.LIGHTNING) {

					e.setCancelled(true);

				}

			}
		}

	}



	@EventHandler
	public void onLavaDamage2(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (InCombat.isPlayerInLobby(p.getUniqueId())) {

				if (e.getCause() == DamageCause.CRAMMING || e.getCause() == DamageCause.MAGIC || e.getCause() == DamageCause.POISON || e.getCause() == DamageCause.PROJECTILE) {

					e.setCancelled(true);

				}

			}
		}

	}

	@EventHandler
	public void onLavaDamage3(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (InCombat.isPlayerInLobby(p.getUniqueId())) {

				if (e.getCause() == DamageCause.STARVATION || e.getCause() == DamageCause.FALLING_BLOCK || e.getCause() == DamageCause.SUFFOCATION || e.getCause() == DamageCause.VOID) {

					e.setCancelled(true);

				}

			}
		}

	}
}