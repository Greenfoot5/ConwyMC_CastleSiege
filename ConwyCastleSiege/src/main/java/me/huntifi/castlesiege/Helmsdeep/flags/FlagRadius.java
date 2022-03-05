package me.huntifi.castlesiege.Helmsdeep.flags;

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

public class FlagRadius implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	//This class detects if the player is in the radius of the flag and on what team they are!
	//Requires the FlagDistance class

	public static ArrayList<Player> SupplyCamp2 = new ArrayList<Player>();
	public static ArrayList<Player> Horn2 = new ArrayList<Player>();
	public static ArrayList<Player> MainGate2 = new ArrayList<Player>();
	public static ArrayList<Player> GreatHalls2 = new ArrayList<Player>();
	public static ArrayList<Player> Caves2 = new ArrayList<Player>();
	public static ArrayList<Player> Courtyard2 = new ArrayList<Player>();

	public static ArrayList<Player> SupplyCamp1 = new ArrayList<Player>();
	public static ArrayList<Player> Horn1 = new ArrayList<Player>();
	public static ArrayList<Player> MainGate1 = new ArrayList<Player>();
	public static ArrayList<Player> GreatHalls1 = new ArrayList<Player>();
	public static ArrayList<Player> Caves1 = new ArrayList<Player>();
	public static ArrayList<Player> Courtyard1 = new ArrayList<Player>();
	
	public static void removePlayerFromAll(Player p) {
		
		if (MapController.currentMapIs("Helmsdeep")) {
		
		if (SupplyCamp1.contains(p)) { SupplyCamp1.remove(p); }
		if (SupplyCamp2.contains(p)) { SupplyCamp2.remove(p); }
		
		if (Horn1.contains(p)) { Horn1.remove(p); }
		if (Horn2.contains(p)) { Horn2.remove(p); }
		
		if (MainGate1.contains(p)) { MainGate1.remove(p); }
		if (MainGate2.contains(p)) { MainGate2.remove(p); }
		
		if (GreatHalls1.contains(p)) { GreatHalls1.remove(p); }
		if (GreatHalls2.contains(p)) { GreatHalls2.remove(p); }
		
		if (Caves1.contains(p)) { Caves1.remove(p); }
		if (Caves2.contains(p)) { Caves2.remove(p); }
		
		if (Courtyard1.contains(p)) { Courtyard1.remove(p); }
		if (Courtyard2.contains(p)) { Courtyard2.remove(p); }
		
		}
	}

	public static void setPlayerInRadius(Player p, String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (p.getGameMode() != GameMode.SPECTATOR) {

				if (flag.equalsIgnoreCase("SupplyCamp")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {

							if (!SupplyCamp1.contains(p)) {

								SupplyCamp1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							
							if (!SupplyCamp2.contains(p)) {

								SupplyCamp2.add(p);
							}
						}
					}
				}

				else if (flag.equalsIgnoreCase("Horn")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!Horn1.contains(p)) {
								Horn1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!Horn2.contains(p)) {
								Horn2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("MainGate")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!MainGate1.contains(p)) {
								MainGate1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!MainGate2.contains(p)) {
								MainGate2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("Courtyard")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!Courtyard1.contains(p)) {
								Courtyard1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!Courtyard2.contains(p)) {
								Courtyard2.add(p);
							}
						}

					}

				}

				else if (flag.equalsIgnoreCase("GreatHalls")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 20) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!GreatHalls1.contains(p)) {
								GreatHalls1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!GreatHalls2.contains(p)) {
								GreatHalls2.add(p);
							}
						}
					}

				}

				else if (flag.equalsIgnoreCase("Caves")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						if (PlayerTeam.playerIsInTeam(p, 1)) {
							if (!Caves1.contains(p)) {
								Caves1.add(p);
							}
						}
						if (PlayerTeam.playerIsInTeam(p, 2)) {
							if (!Caves2.contains(p)) {
								Caves2.add(p);
							}
						}
					}
				}
			}
		}
	}

	public static Boolean isPlayerInRadius(Player p, String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (p.getGameMode() != GameMode.SPECTATOR) {

				if (flag.equalsIgnoreCase("SupplyCamp")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("Horn")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("MainGate")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("Courtyard")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 6) {

						return true;

					}

				}

				else if (flag.equalsIgnoreCase("GreatHalls")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 20) {

						return true;
					}

				}

				else if (flag.equalsIgnoreCase("Caves")) {

					if (FlagDistance.distancePlayerToFlag(p, flag, p.getLocation()) <= 7) {

						return true;
					}
				}
			}
		}
		return false;
	}


	public static void onWalkAway(Player p) {


		if (!(FlagDistance.distancePlayerToFlag(p, "SupplyCamp", p.getLocation()) <= 7)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				SupplyCamp1.remove(p);

			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				SupplyCamp2.remove(p);

			}

		} 

		else if (isPlayerInRadius(p, "SupplyCamp")) {


			setPlayerInRadius(p, "SupplyCamp");

		}

		if (!(FlagDistance.distancePlayerToFlag(p, "Horn", p.getLocation()) <= 7)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				Horn1.remove(p);

			}

			if (PlayerTeam.playerIsInTeam(p, 2)) {
				Horn2.remove(p);

			}

		} 

		else if (isPlayerInRadius(p, "Horn")) {

			setPlayerInRadius(p, "Horn");


		}

		if (!(FlagDistance.distancePlayerToFlag(p, "GreatHalls", p.getLocation()) <= 20)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				GreatHalls1.remove(p);

			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				GreatHalls2.remove(p);

			}

		} 

		else if (isPlayerInRadius(p, "GreatHalls")) {

			setPlayerInRadius(p, "GreatHalls");

		}

		if (!(FlagDistance.distancePlayerToFlag(p, "Caves", p.getLocation()) <= 7)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				Caves1.remove(p);

			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				Caves2.remove(p);

			}

		} 

		else if (isPlayerInRadius(p, "Caves")) {

			setPlayerInRadius(p, "Caves");


		}
		if (!(FlagDistance.distancePlayerToFlag(p, "Courtyard", p.getLocation()) <= 6)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				Courtyard1.remove(p);

			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				Courtyard2.remove(p);

			}

		}

		else if (isPlayerInRadius(p, "Courtyard")) {

			setPlayerInRadius(p, "Courtyard");


		}
		if (!(FlagDistance.distancePlayerToFlag(p, "MainGate", p.getLocation()) <= 7)) {

			if (PlayerTeam.playerIsInTeam(p, 1)) {
				MainGate1.remove(p);

			}
			if (PlayerTeam.playerIsInTeam(p, 2)) {
				MainGate2.remove(p);

			}

		} 

		else if (isPlayerInRadius(p, "MainGate")) {

			setPlayerInRadius(p, "MainGate");


		}

	}
	
	public static void onWalkInHorn(Player p) {
		
		if (isPlayerInRadius(p, "horn")) {
			
			setPlayerInRadius(p, "horn");
		}
		
	}
	
	public static void onWalkInCamp(Player p) {
		
		if (isPlayerInRadius(p, "supplycamp")) {
			
			setPlayerInRadius(p, "supplycamp");
		}
		
	}
	
	public static void onWalkInGreatHall(Player p) {
		
		if (isPlayerInRadius(p, "GreatHalls")) {
			
			setPlayerInRadius(p, "GreatHalls");
		}
		
	}
	
	public static void onWalkInCaves(Player p) {
		
		if (isPlayerInRadius(p, "Caves")) {
			
			setPlayerInRadius(p, "Caves");
		}
		
	}
	
	public static void onWalkInMaingate(Player p) {
		
		if (isPlayerInRadius(p, "MainGate")) {
			
			setPlayerInRadius(p, "MainGate");
		}
		
	}
	
	public static void onWalkInCourtyard(Player p) {
		
		if (isPlayerInRadius(p, "Courtyard")) {
			
			setPlayerInRadius(p, "Courtyard");
		}
		
	}


	@EventHandler
	public void onDie(PlayerDeathEvent e) {

		Player p = (Player) e.getEntity();

		if (PlayerTeam.playerIsInTeam(p, 1)) {
			SupplyCamp1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			SupplyCamp2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Horn1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Horn2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			GreatHalls1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			GreatHalls2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Caves1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Caves2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Courtyard1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Courtyard2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			MainGate1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			MainGate2.remove(p);
		}
	}


	@EventHandler
	public void onQuit(PlayerQuitEvent e) {

		Player p = e.getPlayer();

		if (PlayerTeam.playerIsInTeam(p, 1)) {
			SupplyCamp1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			SupplyCamp2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Horn1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Horn2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			GreatHalls1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			GreatHalls2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Caves1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Caves2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			Courtyard1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			Courtyard2.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 1)) {
			MainGate1.remove(p);
		}
		if (PlayerTeam.playerIsInTeam(p, 2)) {
			MainGate2.remove(p);
		}
	}

	public static Boolean ThisTeamIsCapturing(int team, String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (team == 1) {

					if (SupplyCamp1.size() > SupplyCamp2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (SupplyCamp1.size() < SupplyCamp2.size()) {

						return true;
					}

				}

			} else if (flag.equalsIgnoreCase("Horn")) {

				if (team == 1) {

					if (Horn1.size() > Horn2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (Horn1.size() < Horn2.size()) {

						return true;
					}

				}

			} else if (flag.equalsIgnoreCase("MainGate")) {

				if (team == 1) {

					if (MainGate1.size() > MainGate2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (MainGate1.size() < MainGate2.size()) {

						return true;
					}

				}


			} else if (flag.equalsIgnoreCase("Courtyard")) {

				if (team == 1) {

					if (Courtyard1.size() > Courtyard2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (Courtyard1.size() < Courtyard2.size()) {

						return true;
					}

				}


			} else	if (flag.equalsIgnoreCase("GreatHalls")) {

				if (team == 1) {

					if (GreatHalls1.size() > GreatHalls2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (GreatHalls1.size() < GreatHalls2.size()) {

						return true;
					}

				}


			} else	if (flag.equalsIgnoreCase("Caves")) {

				if (team == 1) {

					if (Caves1.size() > Caves2.size()) {

						return true;
					}
				}

				if (team == 2) {

					if (Caves1.size() < Caves2.size()) {

						return true;
					}

				}

			}
		}

		return false;

	}

	public static Boolean isFlagContested(String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (SupplyCamp1.size() == SupplyCamp2.size()) {

					if (!(SupplyCamp2.size() == 0 && SupplyCamp1.size() == 0)) {

						return true;

					}
				}
			}


			else if (flag.equalsIgnoreCase("Horn")) {

				if (Horn1.size() == Horn2.size()) {

					if (!(Horn2.size() == 0 && Horn1.size() == 0)) {

						return true;
					}

				}

			}

			else if (flag.equalsIgnoreCase("MainGate")) {

				if (MainGate1.size() == MainGate2.size()) {

					if (!(MainGate2.size() == 0 && MainGate1.size() == 0))

						return true;

				}

			}

			else if (flag.equalsIgnoreCase("Courtyard")) {

				if (Courtyard1.size() == Courtyard2.size()) {

					if (!(Courtyard2.size() == 0 && Courtyard1.size() == 0)) {

						return true;

					}
				}

			}

			else if (flag.equalsIgnoreCase("GreatHalls")) {

				if (GreatHalls1.size() == GreatHalls2.size()) {

					if (!(GreatHalls1.size() == 0 && GreatHalls2.size() == 0)) {

						return true;

					}
				}

			}

			else if (flag.equalsIgnoreCase("Caves")) {

				if (Caves1.size() == Caves2.size()) {

					if (!(Caves1.size() == 0 && Caves2.size() == 0)) {

						return true;

					}
				}
			}

		}
		return false;
	}


	public static Boolean FlagIsBeingCaptured(String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {


			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (SupplyCamp1.size() != 0 || SupplyCamp2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("Horn")) {

				if (Horn1.size() != 0 || Horn2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("MainGate")) {

				if (MainGate1.size() != 0 || MainGate2.size() != 0) {

					return true;
				}

			}

			else if (flag.equalsIgnoreCase("Courtyard")) {

				if (Courtyard1.size() != 0 || Courtyard2.size() != 0) {
					return true;
				}

			}

			else if (flag.equalsIgnoreCase("GreatHalls")) {

				if (GreatHalls1.size() != 0 || GreatHalls2.size() != 0) {
					return true;
				}

			}

			else if (flag.equalsIgnoreCase("Caves")) {

				if (Caves1.size() != 0 || Caves2.size() != 0) {
					return true;
				}

			}
		}

		return false;
	}
}



