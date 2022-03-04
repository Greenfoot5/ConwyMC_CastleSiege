package me.huntifi.castlesiege.combat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.huntifi.castlesiege.teams.PlayerTeam;

public class NoHurtTeam implements Listener {

	@EventHandler
	public void onhurt(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {

			Player damaged = (Player) e.getEntity();

			Player damager = (Player) e.getDamager();

			if (PlayerTeam.playerIsInTeam(damaged, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

				e.setCancelled(true);
			}

			if (PlayerTeam.playerIsInTeam(damaged, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

				e.setCancelled(true);
			}


		} 

		if (e.getDamager() instanceof Arrow) {

			Arrow arrow = (Arrow) e.getDamager();

			if (arrow.getShooter() instanceof Player) {

				if (e.getEntity() instanceof Player) {

					Player damaged = (Player) e.getEntity();

					Player damager = (Player) arrow.getShooter();


					if (PlayerTeam.playerIsInTeam(damaged, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

						e.setCancelled(true);
					}

					if (PlayerTeam.playerIsInTeam(damaged, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

						e.setCancelled(true);
					}

				}

			}


		}
	}



	@SuppressWarnings("deprecation")
	@EventHandler
	public void onhurtHorse(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Horse && e.getDamager() instanceof Player) {

			Horse damaged = (Horse) e.getEntity();

			Player damager = (Player) e.getDamager();

			if (damaged.getPassenger() != null) {

				if (damaged.getPassenger() instanceof Player) {

					Player rider = (Player) damaged.getPassenger();

					if (PlayerTeam.playerIsInTeam(rider, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

						e.setCancelled(true);
					}

					if (PlayerTeam.playerIsInTeam(rider, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

						e.setCancelled(true);
					}


				}

			}

		}


		if (e.getDamager() instanceof Arrow) {

			Arrow arrow = (Arrow) e.getDamager();

			if (arrow.getShooter() instanceof Player) {

				if (e.getEntity() instanceof Horse) {

					Horse damaged = (Horse) e.getEntity();

					Player damager = (Player) arrow.getShooter();


					if (damaged.getPassenger() != null) {

						if (damaged.getPassenger() instanceof Player) {

							Player rider = (Player) damaged.getPassenger();

							if (PlayerTeam.playerIsInTeam(rider, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

								e.setCancelled(true);
							}

							if (PlayerTeam.playerIsInTeam(rider, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

								e.setCancelled(true);
							}


						}

					}

				}
			}
		}

	}
	
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onhurtBoat(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Boat && e.getDamager() instanceof Player) {

			Boat damaged = (Boat) e.getEntity();

			Player damager = (Player) e.getDamager();

			if (damaged.getPassenger() != null) {

				if (damaged.getPassenger() instanceof Player) {

					Player rider = (Player) damaged.getPassenger();

					if (PlayerTeam.playerIsInTeam(rider, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

						e.setCancelled(true);
					}

					if (PlayerTeam.playerIsInTeam(rider, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

						e.setCancelled(true);
					}


				}

			}

		}


		if (e.getDamager() instanceof Arrow) {

			Arrow arrow = (Arrow) e.getDamager();

			if (arrow.getShooter() instanceof Player) {

				if (e.getEntity() instanceof Boat) {

					Boat damaged = (Boat) e.getEntity();

					Player damager = (Player) arrow.getShooter();


					if (damaged.getPassenger() != null) {

						if (damaged.getPassenger() instanceof Player) {

							Player rider = (Player) damaged.getPassenger();

							if (PlayerTeam.playerIsInTeam(rider, 1) && PlayerTeam.playerIsInTeam(damager, 1)) {

								e.setCancelled(true);
							}

							if (PlayerTeam.playerIsInTeam(rider, 2) && PlayerTeam.playerIsInTeam(damager, 2)) {

								e.setCancelled(true);
							}


						}

					}

				}
			}
		}

	}

}


