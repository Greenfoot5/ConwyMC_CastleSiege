package me.huntifi.castlesiege.Helmsdeep.Ballistae;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import me.huntifi.castlesiege.maps.MapController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HelmsdeepBallistaEvent implements Listener {

	public static HashMap<Player, Minecart> BallistaUser = new HashMap<Player, Minecart>();
	
	static ArrayList<Minecart> Minecarts = new ArrayList<Minecart>();

	public static Minecart minecart;

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	public static Location ballistaLoc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1018, 66, 1040, -90, 2);

	public static void spawnBallistaMinecart() {

		minecart = (Minecart) ballistaLoc.getWorld().spawnEntity(ballistaLoc, EntityType.MINECART);
		minecart.setInvulnerable(true);
		
		Minecarts.add(minecart);
		
		
	}
	
	public static void removeBallistaMinecart() {

		if (MapController.currentMapIs("HelmsDeep")) {
		
		Minecarts.get(0).remove();
		
		}

	}
	
	public static void removeAllBallistaeMinecart() {

		if (MapController.currentMapIs("HelmsDeep")) {
		
			for (Entity entity : Bukkit.getServer().getWorld("HelmsDeep").getLivingEntities()) {
				
				if (entity instanceof Minecart) {
					
					entity.remove();
					
				}
				
			}

		
		}

	}

	@EventHandler
	public void enterBallista(VehicleEnterEvent e) {

		Player p = (Player) e.getEntered();

		if (e.getVehicle() instanceof Minecart) {
			
			Minecart minecart = (Minecart) e.getVehicle();

			BallistaUser.put(p, minecart);

		}


	}

	@EventHandler
	public void Charge(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if (BallistaUser.containsKey(p)) {

			if (Ballista1Cooldown.canShoot) {

				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You shot the ballista!"));

				Location loc = p.getLocation();
				loc.setY(p.getLocation().getY() + 3.0);
				loc.setZ(p.getLocation().getZ() + 1.0);

				Vector source = loc.getDirection().multiply(5);

				p.launchProjectile(Arrow.class).setVelocity(source);
				Ballista1Cooldown.canShoot = false;
				Ballista1Cooldown.Ballista1Timer(p);

			}
		}
	}
	
	
	@EventHandler
	public void ondismount (EntityDismountEvent e) {

		Player player = (Player) e.getEntity();

		if (e.getDismounted() instanceof Minecart) {

			if (BallistaUser.containsKey(player)) {

				BallistaUser.remove(player);

			}

		}
	}
}
