package me.huntifi.castlesiege.Helmsdeep;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagRadius;
import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.kits.EnderchestRefill;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Woolmap_Distance implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		Block target = p.getTargetBlockExact(50);

		if (LobbyPlayer.containsPlayer(p)) {

				if (target.getState() instanceof Sign) {

					Sign s = (Sign) target.getState();

					if(MapController.currentMapIs("HelmsDeep")) {

						if (s.getLine(1).equalsIgnoreCase("Main Gate")) {

							if (FlagTeam.isFlagTeam("MainGate", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "MainGate")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 1033, 52, 1000, 91, -5);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Main Gate"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "MainGate")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("MainGate", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("MainGate", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "MainGate")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 1033, 52, 1000, 91, -5);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Main Gate"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "MainGate")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}



						if (s.getLine(1).equalsIgnoreCase("Courtyard")) {

							if (FlagTeam.isFlagTeam("Courtyard", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "Courtyard")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 1010, 67, 999, 89, -10);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Courtyard"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "MainGate")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("Courtyard", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("Courtyard", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "Courtyard")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 1010, 67, 999, 89, -10);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Courtyard"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "Courtyard")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}


						if (s.getLine(1).equalsIgnoreCase("Great Hall")) {

							if (FlagTeam.isFlagTeam("GreatHalls", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "GreatHalls")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 960, 72, 1000, 118, -1);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Great Hall"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "GreatHalls")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("GreatHalls", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("GreatHalls", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "GreatHalls")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 960, 72, 1000, 118, -1);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Great Hall"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "GreatHalls")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}


						if (s.getLine(1).equalsIgnoreCase("Horn")) {

							if (FlagTeam.isFlagTeam("Horn", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "Horn")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 980, 140, 1042, -135, 5);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Horn"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "Horn")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("Horn", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("Horn", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "Horn")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 980, 140, 1042, -135, 5);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Horn"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "Horn")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}


						if (s.getLine(1).equalsIgnoreCase("Supply Camp")) {

							if (FlagTeam.isFlagTeam("SupplyCamp", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "SupplyCamp")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 990, 36, 1150, 166, -1);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Supply Camp"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "SupplyCamp")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("SupplyCamp", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("SupplyCamp", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "SupplyCamp")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 990, 36, 1150, 166, -1);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Supply Camp"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "SupplyCamp")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}



						if (s.getLine(1).equalsIgnoreCase("Caves")) {

							if (FlagTeam.isFlagTeam("Caves", 2)) {

								if (!FlagRadius.ThisTeamIsCapturing(1, "Caves")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 2)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 878, 51, 936, -48, 4);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Glittering Caves"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}

									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(1, "Caves")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} 

							} else if (FlagTeam.isFlagTeam("Caves", 0)) {

								if (StatsChanging.getKit(uuid) != null) {

									if(PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									} else {

										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

									}
								} else {
									p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
									p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
								}

							} else if (FlagTeam.isFlagTeam("Caves", 1)) {

								if (!FlagRadius.ThisTeamIsCapturing(2, "Caves")) {

									if (StatsChanging.getKit(uuid) != null) {

										if(PlayerTeam.playerIsInTeam(p, 1)) {

											Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 878, 51, 936, -48, 4);
											p.teleport(Village);
											EnderchestRefill.refill(p);
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Glittering Caves"));
											LobbyPlayer.removePlayer(p);

										} else {
											p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
										}
									} else {
										p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
										p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
									}

								} else if (FlagRadius.ThisTeamIsCapturing(2, "Caves")) {


									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

								} else {

									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
								}
							}
						}




						if (s.getLine(1).equalsIgnoreCase("Uruk's Camp")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("HelmsDeep"), 1169, 35, 1118, 54, -2);
									p.teleport(Village);
									EnderchestRefill.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " Uruk-hai Camp"));
									LobbyPlayer.removePlayer(p);

								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}
						}
					}
			}
		} 
	}
}
