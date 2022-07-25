//package me.huntifi.castlesiege.deprecated.Helmsdeep.rams;
//
//import me.huntifi.castlesiege.database.UpdateStats;
//import me.huntifi.castlesiege.structures.SchematicSpawner;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import com.sk89q.worldedit.WorldEditException;
//
//import me.huntifi.castlesiege.maps.MapController;
//import net.md_5.bungee.api.ChatMessageType;
//import net.md_5.bungee.api.chat.TextComponent;
//
//public class MainGateRamAnimation implements Runnable {
//
//	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
//
//	boolean hasRammed = false;
//
//	@Override
//	public void run() {
//
//		if(MapController.currentMapIs("HelmsDeep")) {
//
//			Location ram = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1044, 54, 1000); //location of schematic for when ramming (touching door)
//
//			Location startRam = new Location(Bukkit.getServer().getWorld("HelmsDeep"), 1045, 54, 1000); //location of schematic for when rammed (not touching door)
//
//			if (MainGateReadyRam.isRamReady) {
//
//				if (!HelmsdeepMainGateDestroyEvent.isBreached && HelmsdeepMainGateDestroyEvent.GateHealth > 0) {
//
//					if (!hasRammed) {
//
//						try {
//							SchematicSpawner.spawnSchematic(startRam, "removeRam_Maingate", "HelmsDeep");
//						} catch (WorldEditException e) {
//							e.printStackTrace();
//						}
//
//						try {
//							SchematicSpawner.spawnSchematic(ram, "Classic_Ram", "HelmsDeep");
//						} catch (WorldEditException e) {
//							e.printStackTrace();
//						}
//
//						Bukkit.getWorld("HelmsDeep").playSound(ram, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR , 2, 1 );
//
//						if ((HelmsdeepMainGateDestroyEvent.GateHealth - 50) <= 0) { //check if gate dies after this and kill gate
//
//							Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1038, 52, 1000); //sound location and schematic location
//
//							if (HelmsdeepMainGateDestroyEvent.isBreached == false) {
//
//								for (Player all : Bukkit.getOnlinePlayers()) {
//
//									all.sendMessage(ChatColor.RED + "The main gate has been breached!");
//
//								}
//
//								try {
//									SchematicSpawner.spawnSchematic(loc, "HelmsdeepMainGateBroken", "HelmsDeep");
//								} catch (WorldEditException e) {
//									e.printStackTrace();
//								}
//
//								//clear the ram in the air, otherwise it would stay there... �-�
//
//								try {
//									SchematicSpawner.spawnSchematic(startRam, "removeRam_Maingate", "HelmsDeep");
//								} catch (WorldEditException e) {
//									e.printStackTrace();
//								}
//
//								Bukkit.getWorld("HelmsDeep").playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );
//
//								HelmsdeepMainGateDestroyEvent.isBreached = true;
//
//							}
//
//						} else {
//
//							HelmsdeepMainGateDestroyEvent.GateHealth = HelmsdeepMainGateDestroyEvent.GateHealth - 50;
//
//							for (Player p : MainGateRam.rammingPlayers) {
//
//							UpdateStats.addSupports(p.getUniqueId(), 8.5);
//							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + HelmsdeepMainGateDestroyEvent.GateHealth));
//
//							}
//
//						}
//
//						hasRammed = true;
//
//						new BukkitRunnable() {
//
//							@Override
//							public void run() {
//
//								if (hasRammed) {
//
//									try {
//										SchematicSpawner.spawnSchematic(ram, "removeRam_Maingate", "HelmsDeep");
//									} catch (WorldEditException e) {
//										e.printStackTrace();
//									}
//
//									try {
//										SchematicSpawner.spawnSchematic(startRam, "Classic_Ram", "HelmsDeep");
//									} catch (WorldEditException e) {
//										e.printStackTrace();
//									}
//
//									Bukkit.getWorld("HelmsDeep").playSound(ram, Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF , 1, 1);
//
//									hasRammed = false;
//
//								}
//							}
//						}.runTaskLater(plugin, 60);
//
//					}
//				}
//
//			}
//
//		}
//	}
//
//}
