package me.huntifi.castlesiege.Thunderstone.Flags;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.teams.PlayerTeam;

public class TS_FlagRadius implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");

	//This class detects if the player is in the radius of the flag and on what team they are!
	//Requires the FlagDistance class

	public static ArrayList<Player> stairhall2 = new ArrayList<Player>();
	public static ArrayList<Player> skyviewtower2 = new ArrayList<Player>();
	public static ArrayList<Player> easttower2 = new ArrayList<Player>();
	public static ArrayList<Player> westtower2 = new ArrayList<Player>();
	public static ArrayList<Player> shiftedtower2 = new ArrayList<Player>();
	public static ArrayList<Player> lonelytower2 = new ArrayList<Player>();
	public static ArrayList<Player> twinbridge2 = new ArrayList<Player>();

	public static ArrayList<Player> stairhall1 = new ArrayList<Player>();
	public static ArrayList<Player> skyviewtower1 = new ArrayList<Player>();
	public static ArrayList<Player> easttower1 = new ArrayList<Player>();
	public static ArrayList<Player> westtower1 = new ArrayList<Player>();
	public static ArrayList<Player> shiftedtower1 = new ArrayList<Player>();
	public static ArrayList<Player> lonelytower1 = new ArrayList<Player>();
	public static ArrayList<Player> twinbridge1 = new ArrayList<Player>();
	
	
	public static void removePlayerFromAll(Player p) {
		
		if (MapController.currentMapIs("Thunderstone")) {
		
		if (stairhall1.contains(p)) { stairhall1.remove(p); }
		if (stairhall2.contains(p)) { stairhall2.remove(p); }
		
		if (skyviewtower1.contains(p)) { skyviewtower1.remove(p); }
		if (skyviewtower2.contains(p)) { skyviewtower2.remove(p); }
		
		if (easttower1.contains(p)) { easttower1.remove(p); }
		if (easttower2.contains(p)) { easttower2.remove(p); }
		
		if (westtower1.contains(p)) { westtower1.remove(p); }
		if (westtower2.contains(p)) { westtower2.remove(p); }
		
		if (shiftedtower1.contains(p)) { shiftedtower1.remove(p); }
		if (shiftedtower2.contains(p)) { shiftedtower2.remove(p); }
		
		if (lonelytower1.contains(p)) { lonelytower1.remove(p); }
		if (lonelytower2.contains(p)) { lonelytower2.remove(p); }
		
		if (twinbridge1.contains(p)) { twinbridge1.remove(p); }
		if (twinbridge2.contains(p)) { twinbridge2.remove(p); }
		
		}
	}

	public static void setPlayerInRadius(Player p, String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (p.getGameMode() != GameMode.SPECTATOR) {

				if (flag.equalsIgnoreCase("stairhall")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {

							if (!stairhall1.contains(p)) {

								stairhall1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!stairhall2.contains(p)) {

								stairhall2.add(p);
							}
						}
					}
				}

				else if (flag.equalsIgnoreCase("skyviewtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!skyviewtower1.contains(p)) {
								skyviewtower1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!skyviewtower2.contains(p)) {
								skyviewtower2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("easttower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!easttower1.contains(p)) {
								easttower1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!easttower2.contains(p)) {
								easttower2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("westtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!westtower1.contains(p)) {
								westtower1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!westtower2.contains(p)) {
								westtower2.add(p);
							}
						}

					}

				}

				else if (flag.equalsIgnoreCase("shiftedtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 20) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!shiftedtower1.contains(p)) {
								shiftedtower1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!shiftedtower2.contains(p)) {
								shiftedtower2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("lonelytower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!lonelytower1.contains(p)) {
								lonelytower1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!lonelytower2.contains(p)) {
								lonelytower2.add(p);
							}
						}
					}
				}

				else if (flag.equalsIgnoreCase("twinbridge")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!twinbridge1.contains(p)) {
								twinbridge1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!twinbridge2.contains(p)) {
								twinbridge2.add(p);
							}
						}
					}
				}
			}
		}
	}

	public static Boolean isPlayerInRadius(Player p, String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (p.getGameMode() != GameMode.SPECTATOR) {

				if (flag.equalsIgnoreCase("stairhall")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("skyviewtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("easttower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("westtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						return true;

					}

				}

				else if (flag.equalsIgnoreCase("shiftedtower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("lonelytower")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}
				}

				else if (flag.equalsIgnoreCase("twinbridge")) {

					if (TS_FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}
				}
			}
		}
		return false;
	}


	public static void onWalkAway(Player p) {

		if (MapController.currentMapIs("Thunderstone")) {


			if (!(TS_FlagDistance.distancePlayerToFlag(p, "stairhall", p.getLocation()) <= 7)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					stairhall1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					stairhall2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "stairhall")) {


				setPlayerInRadius(p, "stairhall");

			}

			if (!(TS_FlagDistance.distancePlayerToFlag(p, "skyviewtower", p.getLocation()) <= 7)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					skyviewtower1.remove(p);

				}

				if (PlayerTeam.playerIsInTeam(p, 2)) {
					skyviewtower2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "skyviewtower")) {

				setPlayerInRadius(p, "skyviewtower");


			}

			if (!(TS_FlagDistance.distancePlayerToFlag(p, "easttower", p.getLocation()) <= 20)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					easttower1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					easttower2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "easttower")) {

				setPlayerInRadius(p, "easttower");

			}

			if (!(TS_FlagDistance.distancePlayerToFlag(p, "westtower", p.getLocation()) <= 7)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					westtower1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					westtower2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "westtower")) {

				setPlayerInRadius(p, "westtower");


			}
			if (!(TS_FlagDistance.distancePlayerToFlag(p, "shiftedtower", p.getLocation()) <= 6)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					shiftedtower1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					shiftedtower2.remove(p);

				}

			}

			if (isPlayerInRadius(p, "shiftedtower")) {

				setPlayerInRadius(p, "shiftedtower");


			}
			if (!(TS_FlagDistance.distancePlayerToFlag(p, "lonelytower", p.getLocation()) <= 7)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					lonelytower1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					lonelytower2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "lonelytower")) {

				setPlayerInRadius(p, "lonelytower");


			}

			if (!(TS_FlagDistance.distancePlayerToFlag(p, "twinbridge", p.getLocation()) <= 7)) {

				if (PlayerTeam.playerIsInTeam(p, 1)) {
					twinbridge1.remove(p);

				}
				if (PlayerTeam.playerIsInTeam(p, 2)) {
					twinbridge2.remove(p);

				}

			} 

			if (isPlayerInRadius(p, "twinbridge")) {

				setPlayerInRadius(p, "twinbridge");


			}

		}

	}


	public static void onWalkIn(Player p) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (isPlayerInRadius(p, "stairhall")) {

				setPlayerInRadius(p, "stairhall");

			}

			if (isPlayerInRadius(p, "skyviewtower")) {

				setPlayerInRadius(p, "skyviewtower");

			}


			if (isPlayerInRadius(p, "westtower")) {

				setPlayerInRadius(p, "westtower");

			}


			if (isPlayerInRadius(p, "easttower")) {

				setPlayerInRadius(p, "easttower");

			}


			if (isPlayerInRadius(p, "shiftedtower")) {

				setPlayerInRadius(p, "shiftedtower");

			}

			if (isPlayerInRadius(p, "twinbridge")) {

				setPlayerInRadius(p, "twinbridge");


			}

			if (isPlayerInRadius(p, "lonelytower")) {

				setPlayerInRadius(p, "lonelytower");


			}

		}

	}

	@EventHandler
	public void onDie(PlayerDeathEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			Player p = (Player) e.getEntity();

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (stairhall1.contains(p)) { stairhall1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (stairhall1.contains(p)) { stairhall2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (skyviewtower1.contains(p)) { skyviewtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (skyviewtower2.contains(p)) { skyviewtower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (lonelytower1.contains(p)) { lonelytower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (lonelytower2.contains(p)) { lonelytower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (twinbridge1.contains(p)) { twinbridge1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (twinbridge2.contains(p)) { twinbridge2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (easttower1.contains(p)) { easttower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (easttower2.contains(p)) { easttower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (westtower1.contains(p)) { westtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (westtower2.contains(p)) { westtower2.remove(p); }
			}

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (shiftedtower1.contains(p)) { shiftedtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (shiftedtower2.contains(p)) { shiftedtower2.remove(p); }
			}

		}

	}


	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		if (MapController.currentMapIs("Thunderstone")) {

			Player p = e.getPlayer();

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (stairhall1.contains(p)) { stairhall1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (stairhall1.contains(p)) { stairhall2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (skyviewtower1.contains(p)) { skyviewtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (skyviewtower2.contains(p)) { skyviewtower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (lonelytower1.contains(p)) { lonelytower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (lonelytower2.contains(p)) { lonelytower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (twinbridge1.contains(p)) { twinbridge1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (twinbridge2.contains(p)) { twinbridge2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (easttower1.contains(p)) { easttower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (easttower2.contains(p)) { easttower2.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (westtower1.contains(p)) { westtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (westtower2.contains(p)) { westtower2.remove(p); }
			}

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				if (shiftedtower1.contains(p)) { shiftedtower1.remove(p); }
			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				if (shiftedtower2.contains(p)) { shiftedtower2.remove(p); }
			}

		}
	}


	public static Boolean ThisTeamIsCapturing(int team, String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				if (team == 1) {

					if (stairhall1.size() > stairhall2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (stairhall1.size() < stairhall2.size()) {

						return true;
					}

				}

			} else if (flag.equalsIgnoreCase("skyviewtower")) {

				if (team == 1) {

					if (skyviewtower1.size() > skyviewtower2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (skyviewtower1.size() < skyviewtower2.size()) {

						return true;
					}

				}

			} else if (flag.equalsIgnoreCase("easttower")) {

				if (team == 1) {

					if (easttower1.size() > easttower2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (easttower1.size() < easttower2.size()) {

						return true;
					}

				}


			} else if (flag.equalsIgnoreCase("westtower")) {

				if (team == 1) {

					if (westtower1.size() > westtower2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (westtower1.size() < westtower2.size()) {

						return true;
					}

				}


			} else	if (flag.equalsIgnoreCase("lonelytower")) {

				if (team == 1) {

					if (lonelytower1.size() > lonelytower2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (lonelytower1.size() < lonelytower2.size()) {

						return true;
					}

				}


			} else	if (flag.equalsIgnoreCase("shiftedtower")) {

				if (team == 1) {

					if (shiftedtower1.size() > shiftedtower2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (shiftedtower1.size() < shiftedtower2.size()) {

						return true;
					}

				}

			} else	if (flag.equalsIgnoreCase("twinbridge")) {

				if (team == 1) {

					if (twinbridge1.size() > twinbridge2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (twinbridge1.size() < twinbridge2.size()) {

						return true;
					}

				}

			}
		}

		return false;

	}

	public static Boolean isFlagContested(String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				if (stairhall1.size() == stairhall2.size()) {

					if (!(stairhall2.size() == 0 && stairhall1.size() == 0)) {

						return true;

					}
				}
			}


			else if (flag.equalsIgnoreCase("skyviewtower")) {

				if (skyviewtower1.size() == skyviewtower2.size()) {

					if (!(skyviewtower2.size() == 0 && skyviewtower1.size() == 0)) {

						return true;
					}

				}

			}

			else if (flag.equalsIgnoreCase("easttower")) {

				if (easttower1.size() == easttower2.size()) {

					if (!(easttower2.size() == 0 && easttower1.size() == 0))

						return true;

				}

			}

			else if (flag.equalsIgnoreCase("westtower")) {

				if (westtower1.size() == westtower2.size()) {

					if (!(westtower2.size() == 0 && westtower1.size() == 0)) {

						return true;

					}
				}

			}

			else if (flag.equalsIgnoreCase("lonelytower")) {

				if (lonelytower1.size() == lonelytower2.size()) {

					if (!(lonelytower1.size() == 0 && lonelytower2.size() == 0)) {

						return true;

					}
				}

			}

			else if (flag.equalsIgnoreCase("shiftedtower")) {

				if (shiftedtower1.size() == shiftedtower2.size()) {

					if (!(shiftedtower1.size() == 0 && shiftedtower2.size() == 0)) {

						return true;

					}
				}
			}

			else if (flag.equalsIgnoreCase("twinbridge")) {

				if (twinbridge1.size() == twinbridge2.size()) {

					if (!(twinbridge1.size() == 0 && twinbridge2.size() == 0)) {

						return true;

					}
				}
			}

		}
		return false;
	}


	public static Boolean FlagIsBeingCaptured(String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				if (stairhall1.size() != 0 || stairhall2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("skyviewtower")) {

				if (skyviewtower1.size() != 0 || skyviewtower2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("easttower")) {

				if (easttower1.size() != 0 || easttower2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("westtower")) {

				if (westtower1.size() != 0 || westtower2.size() != 0) {
					return true;
				}

			}

			else if (flag.equalsIgnoreCase("lonelytower")) {

				if (lonelytower1.size() != 0 || lonelytower2.size() != 0) {
					return true;
				}

			}

			else if (flag.equalsIgnoreCase("shiftedtower")) {

				if (shiftedtower1.size() != 0 || shiftedtower2.size() != 0) {
					return true;
				}

			}

			else if (flag.equalsIgnoreCase("twinbridge")) {

				if (twinbridge1.size() != 0 || twinbridge2.size() != 0) {
					return true;
				}

			}
		}

		return false;
	}
}
