package me.huntifi.castlesiege.combat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;

public class CustomRegeneration implements Listener {

	
	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	static ArrayList<Player> inCombat = new ArrayList<Player>();

	@EventHandler
	public void naturalHealing(EntityDamageByEntityEvent ed) {


		if (ed.getEntity() instanceof Player && ed.getDamager() instanceof Player) {
			Player whoWasHit = (Player) ed.getEntity();
			Player whoHit = (Player) ed.getDamager();

			if(!LobbyPlayer.containsPlayer(whoWasHit) && !LobbyPlayer.containsPlayer(whoHit)) {

				if (PlayerTeam.playerIsInTeam(whoWasHit, 1) && PlayerTeam.playerIsInTeam(whoWasHit, 2)) {
					
					if(!inCombat.contains(whoHit)) {inCombat.add(whoHit);}
					if(!inCombat.contains(whoWasHit)) {inCombat.add(whoWasHit);}


					new BukkitRunnable() {

						@Override
						public void run() {


							if(inCombat.contains(whoHit)) {inCombat.remove(whoHit);}
							if(inCombat.contains(whoWasHit)) {inCombat.remove(whoWasHit);}




						}

					}.runTaskLater(plugin, 160);



				}
			} 

		}
	}
	

}
