package me.huntifi.castlesiege.Helmsdeep.Gates;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.WorldEditException;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.stats.MVP.MVPstats;
import me.huntifi.castlesiege.structures.MakeStructure;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class HelmsdeepMainGateDestroyEvent implements Listener {

	public static int GateHealth = 400; 

	public static ArrayList<Player> hitters = new ArrayList<Player>();

	public static boolean isBreached;

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		Player p = event.getPlayer();

		if(MapController.currentMapIs("HelmsDeep")) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if(event.getAction() == Action.LEFT_CLICK_BLOCK) {

					Location soundloc = event.getClickedBlock().getLocation();

					if (HelmsdeepMainGateBlocks.GateBlocks.contains(event.getClickedBlock())) {

						if (p.isSprinting()) {

							if (!(GateHealth < 2)) {

								if (!hitters.contains(p)) {

									hitters.add(p);

									GateHealth = GateHealth - 2;

									Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> { MVPstats.addSupports(p.getUniqueId(), 0.5);  });

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + GateHealth));

									Bukkit.getWorld("HelmsDeep").playSound(soundloc, Sound.ENTITY_GENERIC_EXPLODE , 2, 1 );

									new BukkitRunnable() {

										@Override
										public void run() {

											hitters.remove(p);
										}

									}.runTaskLater(plugin, 20);

								}


							} else if (GateHealth < 2) {

								if (isBreached != true) {


									Location loc = new Location(plugin.getServer().getWorld("HelmsDeep"), 1038, 52, 1000); 

									if (isBreached == false) {

										for (Player all : Bukkit.getOnlinePlayers()) {

											all.sendMessage(ChatColor.RED + "The main gate has been breached!");

										}

										try {
											MakeStructure.createSchematicStructure(loc, "HelmsdeepMainGateBroken", "HelmsDeep");
										} catch (WorldEditException e) {
											e.printStackTrace();
										}

										Bukkit.getWorld("HelmsDeep").playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 5, 1 );

										isBreached = true;

									}

								}


							}

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You need to sprint in order to bash the gate!"));

						}
					}
				}


			}

		}

	}

}
