package me.huntifi.castlesiege.maps;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagTeam;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagTeam;

public class currentMaps {

	public static boolean HelmsDeep;
	public static boolean Lakeborough;
	public static boolean Abrakhan;
	public static boolean Conwy;
	public static boolean Thunderstone;
	public static boolean Skyhold;
	public static boolean Moria;
	public static boolean SoldiersPeak;
	public static boolean Firelands;
	public static boolean Contario;

	//Set the current map

	public static void setMap(String mapName) {

		if (mapName.equalsIgnoreCase("HelmsDeep")) {

			HelmsDeep = true;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;

		}

		if (mapName.equalsIgnoreCase("Lakeborough")) {

			HelmsDeep = false;
			Lakeborough = true;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;

		}

		if (mapName.equalsIgnoreCase("Abrakhan")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = true;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;

		}

		if (mapName.equalsIgnoreCase("Conwy")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = true;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;
		}

		if (mapName.equalsIgnoreCase("Thunderstone")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = true;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;
		}

		if (mapName.equalsIgnoreCase("Skyhold")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = true;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;
		} 

		if (mapName.equalsIgnoreCase("Moria")) {

			HelmsDeep = true;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = true;
			SoldiersPeak = false;
			Firelands = false;
			Contario = false;
		} 

		if (mapName.equalsIgnoreCase("SoldiersPeak")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = true;
			Firelands = false;
			Contario = false;
		} 

		if (mapName.equalsIgnoreCase("Firelands")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = true;
			Contario = false;
		} 

		if (mapName.equalsIgnoreCase("Contario")) {

			HelmsDeep = false;
			Lakeborough = false;
			Abrakhan = false;
			Conwy = false;
			Thunderstone = false;
			Skyhold = false;
			Moria = false;
			SoldiersPeak = false;
			Firelands = false;
			Contario = true;

		} 

	}


	//Check if that current map is currently being played

	public static Boolean currentMapIs(String mapName) {

		if (mapName.equalsIgnoreCase("HelmsDeep")) {

			if (HelmsDeep) {

				return true;

			}

		}

		if (mapName.equalsIgnoreCase("Lakeborough")) {

			if (Lakeborough) {

				return true;

			}

		}

		if (mapName.equalsIgnoreCase("Abrakhan")) {

			if (Abrakhan) {

				return true;

			}

		}

		if (mapName.equalsIgnoreCase("Conwy")) {

			if (Conwy) {

				return true;

			}
		}

		if (mapName.equalsIgnoreCase("Thunderstone")) {

			if (Thunderstone) {

				return true;

			}
		}

		if (mapName.equalsIgnoreCase("Skyhold")) {

			if (Skyhold) {

				return true;

			}
		} 

		if (mapName.equalsIgnoreCase("Moria")) {

			if (Moria) {

				return true;

			}
		} 

		if (mapName.equalsIgnoreCase("SoldiersPeak")) {

			if (SoldiersPeak) {

				return true;

			}
		} 

		if (mapName.equalsIgnoreCase("Firelands")) {

			if (Firelands) {

				return true;

			}
		} 

		if (mapName.equalsIgnoreCase("Contario")) {

			if (Contario) {

				return true;

			}

		}
		return false; 

	}



	//returns the proper map name

	public static String getCurrentMap() {

		if (HelmsDeep) {

			return "Helm's Deep";

		}


		else if (Lakeborough) {

			return "Lakeborough";
		}


		else if (Abrakhan) {

			return "Abrakhan";

		}


		else if (Conwy) {

			return "Conwy";

		}


		else if (Thunderstone) {

			return "Thunderstone";

		}



		else if (Skyhold) {

			return "Skyhold";

		} 



		else if (Moria) {

			return "Moria";

		} 



		else if (SoldiersPeak) {

			return "Soldier's Peak";

		} 


		else if (Firelands) {

			return "Firelands";

		} 


		else if (Contario) {

			return "Contario";

		}
		return "";


	}


	public static Boolean isMapEnded() {
		
		boolean bool1 = FlagTeam.isFlagTeam("MainGate", 1) || FlagTeam.isFlagTeam("MainGate", 0);
		boolean bool2 = FlagTeam.isFlagTeam("Courtyard", 1) || FlagTeam.isFlagTeam("Courtyard", 0);
		boolean bool3 = FlagTeam.isFlagTeam("Caves", 1) || FlagTeam.isFlagTeam("Caves", 0);
		boolean bool4 = FlagTeam.isFlagTeam("Horn", 1) || FlagTeam.isFlagTeam("Horn", 0);
		boolean bool5 = FlagTeam.isFlagTeam("GreatHalls", 1) || FlagTeam.isFlagTeam("GreatHalls", 0);
		boolean bool6 = FlagTeam.isFlagTeam("SupplyCamp", 1) || FlagTeam.isFlagTeam("SupplyCamp", 0);
		
		boolean TS_bool1 = TS_FlagTeam.isFlagTeam("skyviewtower", 1) || TS_FlagTeam.isFlagTeam("skyviewtower", 0);
		boolean TS_bool2 = TS_FlagTeam.isFlagTeam("stairhall", 1) || TS_FlagTeam.isFlagTeam("stairhall", 0);
		boolean TS_bool3 = TS_FlagTeam.isFlagTeam("westtower", 1) || TS_FlagTeam.isFlagTeam("westtower", 0);
		boolean TS_bool4 = TS_FlagTeam.isFlagTeam("easttower", 1) || TS_FlagTeam.isFlagTeam("easttower", 0);
		boolean TS_bool5 = TS_FlagTeam.isFlagTeam("shiftedtower", 1) || TS_FlagTeam.isFlagTeam("shiftedtower", 0);
		boolean TS_bool6 = TS_FlagTeam.isFlagTeam("twinbridge", 1) || TS_FlagTeam.isFlagTeam("twinbridge", 0);
		boolean TS_bool7 = TS_FlagTeam.isFlagTeam("lonelytower", 1) || TS_FlagTeam.isFlagTeam("lonelytower", 0);
		
		if (currentMapIs("HelmsDeep")) {

			if (bool1 && bool2 && bool3 &&
					bool4 && bool5 && bool6) {

				return true;
			}

		}
		
		if (currentMapIs("Thunderstone")) {

			if (TS_bool1 && TS_bool2 && TS_bool3 &&
					TS_bool4 && TS_bool5 && TS_bool6 && TS_bool7) {

				return true;
			}

		}



		return false;


	}



}
