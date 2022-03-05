package me.huntifi.castlesiege.Helmsdeep.doors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GreatHallExtraDoor implements Listener {

	public Boolean open = false;
	
	public Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onPressure(PlayerMoveEvent e) {

		Player p = e.getPlayer();

		Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 67, 964); //soundlocation

		if (MapController.currentMapIs("HelmsDeep")) {

			double LeverDistance = p.getLocation().distance(loc);

			if (p.getLocation().getBlock().getType().equals(Material.STONE_PRESSURE_PLATE) && LeverDistance <= 3) {
				
				if (open == false) {

				if (FlagTeam.getFlagTeam("GreatHalls") == PlayerTeam.getPlayerTeam(p) || FlagTeam.isFlagTeam("GreatHalls", 0)) {
					
					open = true;

					Block O1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 67, 964).getBlock();
					O1.setType(Material.AIR);

					Block O12 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 68, 964).getBlock();
					O12.setType(Material.AIR);

					Block O13 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 69, 964).getBlock();
					O13.setType(Material.AIR);

					Block O14 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 67, 963).getBlock();
					O14.setType(Material.AIR);

					Block O1b = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 68, 963).getBlock();
					O1b.setType(Material.AIR);

					Block O12b = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 69, 963).getBlock();
					O12b.setType(Material.AIR);

					Bukkit.getWorld("HelmsDeep").playSound(loc, Sound.BLOCK_WOODEN_DOOR_OPEN , 3, 1 );

					new BukkitRunnable() {

						@Override
						public void run() {

							Block O1 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 67, 964).getBlock();
							O1.setType(Material.SPRUCE_LOG);

							Block O12 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 68, 964).getBlock();
							O12.setType(Material.SPRUCE_LOG);

							Block O13 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 69, 964).getBlock();
							O13.setType(Material.SPRUCE_LOG);

							Block O14 = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 67, 963).getBlock();
							O14.setType(Material.SPRUCE_LOG);

							Block O1b = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 68, 963).getBlock();
							O1b.setType(Material.SPRUCE_LOG);

							Block O12b = new Location(plugin.getServer().getWorld("HelmsDeep"), 996, 69, 963).getBlock();
							O12b.setType(Material.SPRUCE_LOG);
							
							Bukkit.getWorld("HelmsDeep").playSound(loc, Sound.BLOCK_WOODEN_DOOR_CLOSE , 3, 1 );

							open = false;
						}

					}.runTaskLater(plugin, 40);

				} else {

					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your team does not control this door."));


				}

			}

			}


		}
	}
}
