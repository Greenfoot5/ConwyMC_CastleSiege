package me.huntifi.castlesiege.kits.Ranger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class RangerAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	RangerKit rangerBow = new RangerKit();
	ItemStack theVolleyBow = rangerBow.getRangerVolleyBow();
	ItemMeta getBowMeta1 = theVolleyBow.getItemMeta(); 

	ItemStack theBurstBow = rangerBow.getRangerBurstBow();
	ItemMeta getBowMeta2 = theBurstBow.getItemMeta(); 

	@EventHandler
	public void changeSpearDamage(ProjectileHitEvent e) {

		if (e.getEntity() instanceof Arrow) {

			Arrow arrow = (Arrow) e.getEntity();
			if(arrow.getShooter() instanceof Player){
				Player damager = (Player) arrow.getShooter();
				if (StatsChanging.getKit(damager.getUniqueId()).equalsIgnoreCase("Ranger")) {
					arrow.setDamage(13);
				} 

			}

		}

	}

	@EventHandler
	public void VolleyBow(EntityShootBowEvent e) {

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();

			if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Ranger")) {

				ItemStack bow = p.getInventory().getItemInMainHand();

				ItemMeta bowMeta = bow.getItemMeta();

				int cooldownVolley = p.getCooldown(theVolleyBow.getType());

				int cooldownBurst = p.getCooldown(theBurstBow.getType());

				if (e.getProjectile() instanceof Arrow) {
					
					final Arrow arrow = (Arrow) e.getProjectile();
					
					Vector vel = arrow.getVelocity();
					
					Location arrowLoc = new Location(p.getWorld(), p.getEyeLocation().getX() + getX(p) , p.getEyeLocation().getY() , p.getEyeLocation().getZ() + getZ(p));

					if (bowMeta.equals(getBowMeta1)) {

						if (cooldownVolley == 0) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "You shot your volley bow!"));

							Arrow arrow1 = e.getEntity().getWorld().spawn(arrowLoc, Arrow.class);
							arrow1.setDamage(arrow.getDamage());
							arrow1.setShooter(p);
							arrow1.setVelocity(arrow.getVelocity().rotateAroundY(Math.toRadians(9)));
							p.getInventory().removeItem(new ItemStack(Material.ARROW));
							
							Arrow arrow2 = e.getEntity().getWorld().spawn(arrowLoc, Arrow.class);
							arrow2.setDamage(arrow.getDamage());
							arrow2.setShooter(p);
							arrow2.setVelocity(arrow.getVelocity().rotateAroundY(Math.toRadians(-9)));
							p.getInventory().removeItem(new ItemStack(Material.ARROW));

							p.setCooldown(theVolleyBow.getType(), 100);

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't shoot your volley bow yet."));

						}

					} else if (bowMeta.equals(getBowMeta2)) {

						if (cooldownBurst == 0) {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "You shot your burst bow!"));

							new BukkitRunnable() {

								@Override
								public void run() {

									Arrow arrow1 = e.getEntity().getWorld().spawn(arrowLoc, Arrow.class);
									arrow1.setDamage(arrow.getDamage());
									arrow1.setShooter(p);
									arrow1.setVelocity(vel.clone());
									arrow1.getVelocity().multiply(8);
									p.getInventory().removeItem(new ItemStack(Material.ARROW));

									new BukkitRunnable() {

										@Override
										public void run() {

											Arrow arrow1 = e.getEntity().getWorld().spawn(arrowLoc, Arrow.class);
											arrow1.setDamage(arrow.getDamage());
											arrow1.setShooter(p);
											arrow1.setVelocity(vel.clone());
											arrow1.getVelocity().multiply(16);
											p.getInventory().removeItem(new ItemStack(Material.ARROW));

										}
									}.runTaskLater(plugin, 10);
								}
							}.runTaskLater(plugin, 10);

							p.setCooldown(theVolleyBow.getType(), 100);

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't shoot your burst bow yet."));

						}
					}


				}
			}
		}

	}
	
	public double getX(Player p) {

		double rotation = (p.getLocation().getYaw() - 90.0F) % 360.0F;

		if (rotation < 0.0D) {
		rotation += 360.0D;
		}
		if ((0.0D <= rotation) && (rotation < 45.0D))
		return -1.5;
		if ((45.0D <= rotation) && (rotation < 135.0D))
		return 0;
		if ((135.0D <= rotation) && (rotation < 225.0D))
		return 1.5;
		if ((225.0D <= rotation) && (rotation < 315.0D))
		return 0;
		if ((315.0D <= rotation) && (rotation < 360.0D)) {
		return -1.5;
		}
		return 0;
		}
	
	public double getZ(Player p) {

		double rotation = (p.getLocation().getYaw() - 90.0F) % 360.0F;

		if (rotation < 0.0D) {
		rotation += 360.0D;
		}
		if ((0.0D <= rotation) && (rotation < 45.0D))
		return 0;
		if ((45.0D <= rotation) && (rotation < 135.0D))
		return -1.5;
		if ((135.0D <= rotation) && (rotation < 225.0D))
		return 0;
		if ((225.0D <= rotation) && (rotation < 315.0D))
		return 1.5;
		if ((315.0D <= rotation) && (rotation < 360.0D)) {
		return 0;
		}
		return 0;
		}




	public Vector rotateVector(Vector vector, double whatAngle) {
		double sin = Math.sin(whatAngle);
		double cos = Math.cos(whatAngle);
		double x = vector.getX() * cos + vector.getZ() * sin;
		double z = vector.getX() * -sin + vector.getZ() * cos;

		return vector.setX(x).setZ(z);
	}

}
