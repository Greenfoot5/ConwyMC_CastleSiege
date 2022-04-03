package me.huntifi.castlesiege.Thunderstone.Rams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.Thunderstone.Gate.ThunderstoneGateDestroyEvent;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.structures.MakeStructure;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ThunderstoneRamAnimation implements Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	boolean hasRammed = false;

	@Override
	public void run() {

		if(MapController.currentMapIs("Thunderstone")) {

			Location ram = new Location(Bukkit.getServer().getWorld("Thunderstone"), 150, 67, 67); //location of schematic for when ramming (touching door)
 
			Location startRam = new Location(Bukkit.getServer().getWorld("Thunderstone"), 149, 67, 67); //location of schematic for when rammed (not touching door)

			if (ThunderstoneGateReadyRam.isRamReady) {

				if (!ThunderstoneGateDestroyEvent.isBreached && ThunderstoneGateDestroyEvent.GateHealth > 0) {

					if (!hasRammed) {

						try {
							MakeStructure.createSchematicStructure(startRam, "clear_ram_east", "Thunderstone");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						try {
							MakeStructure.createSchematicStructure(ram, "ram_east", "Thunderstone");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						Bukkit.getWorld("Thunderstone").playSound(ram, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR , 2, 1 );
						
						if ((ThunderstoneGateDestroyEvent.GateHealth - 50) <= 0) { //check if gate dies after this and kill gate
							
							Location loc = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 65, 67);  //sound location and schematic location, must be the same as in ThunderstoneGateDestroyEvent.

							if (ThunderstoneGateDestroyEvent.isBreached == false) {

								for (Player all : Bukkit.getOnlinePlayers()) {

									all.sendMessage(ChatColor.RED + "The entrance has been Breached!");

								}

								try {
									MakeStructure.createSchematicStructure(loc, "ThunderstoneGateBreached", "Thunderstone");
								} catch (WorldEditException e) {
									e.printStackTrace();
								}
								
								//clear the ram (otherwise it would just kinda be there in the air)
								
								try {
									MakeStructure.createSchematicStructure(startRam, "clear_ram_east", "Thunderstone");
								} catch (WorldEditException e) {
									e.printStackTrace();
								}

								Bukkit.getWorld("Thunderstone").playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 1, 1);

								ThunderstoneGateDestroyEvent.isBreached = true;

							}
							
						} else {
							
							ThunderstoneGateDestroyEvent.GateHealth = ThunderstoneGateDestroyEvent.GateHealth - 50;
							
							for (Player p : ThunderstoneRam.rammingPlayers) {
							
							MVPstats.addSupports(p.getUniqueId(), 8.5);
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + ThunderstoneGateDestroyEvent.GateHealth));
							
							}
				
						}

						hasRammed = true;

						new BukkitRunnable() {

							@Override
							public void run() {

								if (hasRammed) {

									try {
										MakeStructure.createSchematicStructure(ram, "clear_ram_east", "Thunderstone");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}

									try {
										MakeStructure.createSchematicStructure(startRam, "ram_east", "Thunderstone");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}

									Bukkit.getWorld("Thunderstone").playSound(ram, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF , 1, 1);

									hasRammed = false;

								}
							}
						}.runTaskLater(plugin, 60);

					} 
				} 

			}

		}
	}

}
