package me.huntifi.castlesiege.Thunderstone.Gate;

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

public class ThunderstoneGateDestroyEvent implements Listener {

	public static int GateHealth = 300; 

	public static ArrayList<Player> hitters = new ArrayList<Player>();

	public static boolean isBreached = false;

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		Player p = event.getPlayer();

		if(MapController.currentMapIs("Thunderstone")) {

			if (!LobbyPlayer.containsPlayer(p)) {

				if(event.getAction() == Action.LEFT_CLICK_BLOCK) {

					Location soundloc = event.getClickedBlock().getLocation();

					if (ThunderstoneGateBlocks.GateBlocks.contains(event.getClickedBlock())) {

						if (p.isSprinting()) {

							if (!(GateHealth < 2)) {

								if (!hitters.contains(p)) {

									hitters.add(p);

									GateHealth = GateHealth - 2;

									MVPstats.addSupports(p.getUniqueId(), 0.5);  

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "" + ChatColor.BOLD + "Gate Health: " + GateHealth));

									Bukkit.getWorld("Thunderstone").playSound(soundloc, Sound.ENTITY_GENERIC_EXPLODE , 2, 1 );

									new BukkitRunnable() {

										@Override
										public void run() {

											hitters.remove(p);
										}

									}.runTaskLater(plugin, 20);

								}


							} else if (GateHealth < 2) {

								if (isBreached != true) {

									Location loc = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 65, 67); 



									for (Player all : Bukkit.getOnlinePlayers()) {

										all.sendMessage(ChatColor.RED + "The entrance has been Breached!");

									}

									try {
										MakeStructure.createSchematicStructure(loc, "ThunderstoneGateBreached", "Thunderstone");
									} catch (WorldEditException e) {
										e.printStackTrace();
									}

									Bukkit.getWorld("Thunderstone").playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR , 1, 1);

									isBreached = true;


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
