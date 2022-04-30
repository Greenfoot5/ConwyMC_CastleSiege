package me.huntifi.castlesiege.Thunderstone;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class TS_Woolmap implements Listener {
	
	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {

		/*Player p = e.getPlayer();

		UUID uuid = p.getUniqueId();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

		if (e.getClickedBlock().getState() instanceof Sign) {

			Sign s = (Sign) e.getClickedBlock().getState();


			if(MapController.currentMapIs("Thunderstone")) {

				if (s.getLine(1).equalsIgnoreCase("West Tower")) {

					if (TS_FlagTeam.isFlagTeam("westtower", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "westtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 97, 82, -91, 5);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The West Tower"));

								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "westtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("westtower", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("westtower", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "westtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 156, 97, 82, -91, 5);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The West Tower"));

								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "westtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}



				if (s.getLine(1).equalsIgnoreCase("East Tower")) {

					if (TS_FlagTeam.isFlagTeam("easttower", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "easttower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 182, 92, 83, 180, -1);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The East Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "easttower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("easttower", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("easttower", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "easttower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 182, 92, 83, 180, -1);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The East Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "easttower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}


				if (s.getLine(1).equalsIgnoreCase("Skyview Tower")) {

					if (TS_FlagTeam.isFlagTeam("skyviewtower", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "skyviewtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 166, 154, 68, -54, 1);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Skyview Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "skyviewtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("skyviewtower", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("skyviewtower", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "skyviewtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 166, 154, 68, -54, 1);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Skyview Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "skyviewtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}


				if (s.getLine(1).equalsIgnoreCase("Stair Hall")) {

					if (TS_FlagTeam.isFlagTeam("stairhall", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "stairhall")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 182, 66, 67, 90, 2);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " stairhall"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "stairhall")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("stairhall", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("stairhall", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "stairhall")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 182, 66, 67, 90, 2);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The stairhall"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "stairhall")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}


				if (s.getLine(1).equalsIgnoreCase("Lonely Tower")) {

					if (TS_FlagTeam.isFlagTeam("lonelytower", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "lonelytower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 123, 97, 55, -12, 2);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Lonely Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "lonelytower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("lonelytower", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("lonelytower", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "lonelytower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 123, 97, 55, -12, 2);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Lonely Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "lonelytower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}



				if (s.getLine(1).equalsIgnoreCase("Shifted Tower")) {

					if (TS_FlagTeam.isFlagTeam("shiftedtower", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "shiftedtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 230, 116, 82, -163, 0);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Shifted Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "shiftedtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("shiftedtower", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("shiftedtower", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "shiftedtower")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 230, 116, 82, -163, 0);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Shifted Tower"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "shiftedtower")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}
				
				
				if (s.getLine(1).equalsIgnoreCase("Twin Bridge")) {

					if (TS_FlagTeam.isFlagTeam("Twinbridge", 2)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(1, "Twinbridge")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 2)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 292, 74, 83, -112, 0);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Twinbridge"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}

							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(1, "twinbridge")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} 

					} else if (TS_FlagTeam.isFlagTeam("Twinbridge", 0)) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 2) || PlayerTeam.playerIsInTeam(p, 1)) {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn at neutral flags."));

							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}

					} else if (TS_FlagTeam.isFlagTeam("twinbridge", 1)) {

						if (!TS_FlagRadius.ThisTeamIsCapturing(2, "Twinbridge")) {

							if (StatsChanging.getKit(uuid) != null) {

								if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

									Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 292, 74, 83, -112, 0);
									p.teleport(Village);
									//Enderchest.refill(p);
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " The Twinbridge"));


								} else {
									p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
								}
							} else {
								p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
								p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
							}

						} else if (TS_FlagRadius.ThisTeamIsCapturing(2, "Twinbridge")) {


							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "You can't spawn here. This flag is under attack!"));

						} else {

							p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "This is an error!"));
						}
					}
				}




				if (s.getLine(1).equalsIgnoreCase("The Camp")) {

						if (StatsChanging.getKit(uuid) != null) {

							if(true) {//PlayerTeam.playerIsInTeam(p, 1)) {

								Location Village = new Location(plugin.getServer().getWorld("Thunderstone"), 90, 86, 106, -112, -5);
								p.teleport(Village);
								//Enderchest.refill(p);
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_GREEN + "Spawning at:" + ChatColor.GREEN + " Cloudcrawler Camp"));


							} else {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[!] " + ChatColor.DARK_RED + "Your team does not own this flag at the moment."));
							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You can't join the battlefield without a kit/class!");
							p.sendMessage(ChatColor.DARK_RED + "Choose a kit/class with the command " + ChatColor.RED + "/class <Classname>" + ChatColor.DARK_RED + "!");
						}
				}
			}
		}*/
	}

}
