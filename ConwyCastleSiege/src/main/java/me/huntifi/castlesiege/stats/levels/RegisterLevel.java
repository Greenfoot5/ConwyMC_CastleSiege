package me.huntifi.castlesiege.stats.levels;

import java.util.UUID;

import me.huntifi.castlesiege.maps.MapController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.tags.NametagsEvent;

public class RegisterLevel implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onJoinLevel(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		new BukkitRunnable() {

			@Override
			public void run() {

				if (StatsChanging.getScore(uuid) <= 0) {

					StatsChanging.setLevel(uuid, 0);

				}

				else if (StatsChanging.getScore(uuid) >= 8 && StatsChanging.getScore(uuid) < 24) {

					StatsChanging.setLevel(uuid, 1);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 24 && StatsChanging.getScore(uuid) < 56) {

					StatsChanging.setLevel(uuid, 2);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 56 && StatsChanging.getScore(uuid) < 104) {
					
					StatsChanging.setLevel(uuid, 3);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));
				}

				else if (StatsChanging.getScore(uuid) >= 104 && StatsChanging.getScore(uuid) < 168) {

					StatsChanging.setLevel(uuid, 4);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 168 && StatsChanging.getScore(uuid) < 296) {

					StatsChanging.setLevel(uuid, 5);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 296 && StatsChanging.getScore(uuid) < 424) {

					StatsChanging.setLevel(uuid, 6);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 424 && StatsChanging.getScore(uuid) < 604) {

					StatsChanging.setLevel(uuid, 7);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 604 && StatsChanging.getScore(uuid) < 784) {

					StatsChanging.setLevel(uuid, 8);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 784 && StatsChanging.getScore(uuid) < 1084) {

					StatsChanging.setLevel(uuid, 9);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 1084 && StatsChanging.getScore(uuid) < 1340) {

					StatsChanging.setLevel(uuid, 10);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 1340 && StatsChanging.getScore(uuid) < 1852) {

					StatsChanging.setLevel(uuid, 11);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 1852 && StatsChanging.getScore(uuid) < 2364) {

					StatsChanging.setLevel(uuid, 12);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 2364 && StatsChanging.getScore(uuid) < 3089) {

					StatsChanging.setLevel(uuid, 13);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 3089 && StatsChanging.getScore(uuid) < 3814) {

					StatsChanging.setLevel(uuid, 14);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 3814 && StatsChanging.getScore(uuid) < 4838) {

					StatsChanging.setLevel(uuid, 15);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 4838 && StatsChanging.getScore(uuid) < 5862) {

					StatsChanging.setLevel(uuid, 16);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 5862 && StatsChanging.getScore(uuid) < 7062) {

					StatsChanging.setLevel(uuid, 17);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 7062 && StatsChanging.getScore(uuid) < 8262) {

					StatsChanging.setLevel(uuid, 18);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 8262 && StatsChanging.getScore(uuid) < 9862) {

					StatsChanging.setLevel(uuid, 19);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 9862 && StatsChanging.getScore(uuid) < 11262) {

					StatsChanging.setLevel(uuid, 20);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 11262 && StatsChanging.getScore(uuid) < 12662) {

					StatsChanging.setLevel(uuid, 21);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 12662 && StatsChanging.getScore(uuid) < 14312) {

					StatsChanging.setLevel(uuid, 22);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 14312 && StatsChanging.getScore(uuid) < 16162) {

					StatsChanging.setLevel(uuid, 23);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 16162 && StatsChanging.getScore(uuid) < 18212) {

					StatsChanging.setLevel(uuid, 24);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 18212 && StatsChanging.getScore(uuid) < 20662) {

					StatsChanging.setLevel(uuid, 25);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 20662 && StatsChanging.getScore(uuid) < 23312) {

					StatsChanging.setLevel(uuid, 26);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 23312 && StatsChanging.getScore(uuid) < 26212) {

					StatsChanging.setLevel(uuid, 27);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 26212 && StatsChanging.getScore(uuid) < 29162) {

					StatsChanging.setLevel(uuid, 28);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 29162 && StatsChanging.getScore(uuid) < 32662) {

					StatsChanging.setLevel(uuid, 29);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 32662 && StatsChanging.getScore(uuid) < 35812) {

					StatsChanging.setLevel(uuid, 30);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 35812 && StatsChanging.getScore(uuid) < 39042) {

					StatsChanging.setLevel(uuid, 31);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 39042 && StatsChanging.getScore(uuid) < 42322) {

					StatsChanging.setLevel(uuid, 32);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 42322 && StatsChanging.getScore(uuid) < 45742) {

					StatsChanging.setLevel(uuid, 33);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 45742 && StatsChanging.getScore(uuid) < 49382) {

					StatsChanging.setLevel(uuid, 34);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 49382 && StatsChanging.getScore(uuid) < 53102) {

					StatsChanging.setLevel(uuid, 35);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 53102 && StatsChanging.getScore(uuid) < 56962) {

					StatsChanging.setLevel(uuid, 36);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 56962 && StatsChanging.getScore(uuid) < 60937) {

					StatsChanging.setLevel(uuid, 37);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 60937 && StatsChanging.getScore(uuid) < 64952) {

					StatsChanging.setLevel(uuid, 38);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 64952 && StatsChanging.getScore(uuid) < 69202) {


					StatsChanging.setLevel(uuid, 39);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

				else if (StatsChanging.getScore(uuid) >= 69202 && StatsChanging.getScore(uuid) < 73272) {

					StatsChanging.setLevel(uuid, 40);
					NametagsEvent.GiveNametag(p, MapController.getCurrentMap().getTeam(p));

				}

			}
		}.runTaskLater(plugin, 40);
	}

}
