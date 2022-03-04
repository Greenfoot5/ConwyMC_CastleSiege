package me.huntifi.castlesiege.Helmsdeep.rams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.Helmsdeep.Gates.HelmsdeepGreatHallDestroyEvent;
import me.huntifi.castlesiege.maps.currentMaps;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.structures.MakeStructure;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GreatHallRamAnimation implements Runnable {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	boolean hasRammed = false;

	@Override
	public void run() {

		if(currentMaps.currentMapIs("HelmsDeep")) {

			Location ram = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 977, 74, 1000); //location of schematic for when ramming (touching door)
 
			Location startRam = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 978, 74, 1000); //location of schematic for when rammed (not touching door)

			if (GreatHallGateReadyRam.isRamReady) {

				if (!HelmsdeepGreatHallDestroyEvent.isBreached && HelmsdeepGreatHallDestroyEvent.GateHealth > 0) {

					if (!hasRammed) {

						try {
							MakeStructure.createSchematicStructure(startRam, "removeRam_Maingate", "HelmsDeep");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						try {
							MakeStructure.createSchematicStructure(ram, "Classic_Ram", "HelmsDeep");
						} catch (WorldEditException e) {
							e.printStackTrace();
						}

						Bukkit.getWorld("HelmsDeep").playSound(ram, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR , 2, 1 );
						
						if ((HelmsdeepGreatHallDestroyEvent.GateHealth - 50) <= 0) { //check if gate dies after this and kill gate
							
							Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 969, 72, 1000); //sound location and schematic location

							if (HelmsdeepGreatHallDestroyEvent.isBreached == false) {

								for (Player all : Bukkit.getOnlinePlayers()) {

									all.sendMessage(ChatColor.RED + "The door to the great halls has been breached!");

								}

								try {
									MakeStructure.createSchematicStructure(loc, "HelmsdeepGreatHallBroken", "HelmsDeep");
								} catch (WorldEditException e) {
									e.printStackTrace();
								}

								Bukkit.getWorld("HelmsDeep").playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );

								HelmsdeepGreatHallDestroyEvent.isBreached = true;

							}
							
						} else {
							
							HelmsdeepGreatHallDestroyEvent.GateHealth = HelmsdeepGreatHallDestroyEvent.GateHealth - 50;
							
							for (Player p : GreatHallGateRam.rammingPlayers) {
							
							MVPstats.addSupports(p.getUniqueId(), 8.5);
							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + HelmsdeepGreatHallDestroyEvent.GateHealth));
							
							}
				
						}

						hasRammed = true;

						new BukkitRunnable() {

							@Override
							public void run() {

								if (hasRammed) {

									try {
										MakeStructure.createSchematicStructure(ram, "removeRam_Maingate", "HelmsDeep");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}

									try {
										MakeStructure.createSchematicStructure(startRam, "Classic_Ram", "HelmsDeep");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}

									Bukkit.getWorld("HelmsDeep").playSound(ram, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF , 1, 1);

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


