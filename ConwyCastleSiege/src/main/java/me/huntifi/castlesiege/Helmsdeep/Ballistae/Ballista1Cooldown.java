package me.huntifi.castlesiege.Helmsdeep.Ballistae;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Ballista1Cooldown {

	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static Integer Seconds = 7;

	public static Boolean canShoot = true;

	public static void Ballista1Timer(Player p) {

		new BukkitRunnable() {

			@Override
			public void run() {

				if(MapController.currentMapIs("HelmsDeep")) {

					if (canShoot == false) {

						if (!(Seconds == 0)) {

							Seconds--;

						} else if (Seconds == 0) {

							canShoot = true;
							Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Ballista is ready to shoot!")); });

						}

					}

				} else {

					this.cancel();
				}

			}

		}.runTaskTimerAsynchronously(plugin, 20, 20);


	}

}


