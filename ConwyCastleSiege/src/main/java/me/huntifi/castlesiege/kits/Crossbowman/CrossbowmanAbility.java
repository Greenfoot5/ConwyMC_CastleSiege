package me.huntifi.castlesiege.kits.Crossbowman;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;

public class CrossbowmanAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	public static ArrayList<Arrow> arrows = new ArrayList<Arrow>();

	@EventHandler
	public void shootCrossbow(EntityShootBowEvent e) {

		Player p = (Player) e.getEntity();

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Crossbowman")) {

			int cooldown = p.getCooldown(Material.CROSSBOW);

			if (cooldown == 0) {

				e.setCancelled(true);

				p.setCooldown(Material.CROSSBOW, 75);

				Arrow arrow= p.launchProjectile(Arrow.class);
				arrow.setShooter(p);
				arrow.setVelocity(new Vector(p.getLocation().getDirection().getX(), p.getLocation().getDirection().getY(), p.getLocation().getDirection().getZ()).normalize().multiply(35));

			}
		}
	}
}
