package me.huntifi.castlesiege.Helmsdeep.flags;

import me.huntifi.castlesiege.maps.MapController;

public class FlagTeam {

	public static int SupplyCamp = 2;
	public static int Horn = 2;
	public static int MainGate = 2;
	public static int Courtyard = 2;
	public static int GreatHalls = 2;
	public static int Caves = 2;

	public static int neutral = 0;
	public static int attackingTeam = 1;
	public static int defendingTeam = 2;

	public static void setFlagTeam(String flag, int team) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (team == 0) {
					SupplyCamp = 0;
				}
				if (team == 1) {
					SupplyCamp = 1;
				}
				if (team == 2) {
					SupplyCamp = 2;
				}

			}

			if (flag.equalsIgnoreCase("Horn")) {

				if (team == 0) {
					Horn = 0;
				}
				if (team == 1) {
					Horn = 1;
				}
				if (team == 2) {
					Horn = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("MainGate")) {

				if (team == 0) {
					MainGate = 0;
				}
				if (team == 1) {
					MainGate = 1;
				}
				if (team == 2) {
					MainGate = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("Courtyard")) {

				if (team == 0) {
					Courtyard = 0;
				}
				if (team == 1) {
					Courtyard = 1;
				}
				if (team == 2) {
					Courtyard = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("GreatHalls")) {

				if (team == 0) {
					
					GreatHalls = 0;
				}
				if (team == 1) {
					GreatHalls = 1;
				}
				if (team == 2) {
					GreatHalls = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("Caves")) {

				if (team == 0) {
					Caves = 0;
				}
				if (team == 1) {
					Caves = 1;
				}
				if (team == 2) {
					Caves = 2;
				}
				
			}


		}

	}

	public static Integer getFlagTeam(String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {


			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (SupplyCamp == 0) {
					return 0;
				}
				if (SupplyCamp == 1) {
					return 1;
				}
				if (SupplyCamp == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("Horn")) {

				if (Horn == 0) {
					return 0;
				}
				if (Horn == 1) {
					return 1;
				}
				if (Horn == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("MainGate")) {

				if (MainGate == 0) {
					return 0;
				}
				if (MainGate == 1) {
					return 1;
				}
				if (MainGate == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("Courtyard")) {

				if (Courtyard == 0) {
					return 0;
				}
				if (Courtyard == 1) {
					return 1;
				}
				if (Courtyard == 2) {
					return 2;
				}
			}

			if (flag.equalsIgnoreCase("GreatHalls")) {

				if (GreatHalls == 0) {
					return 0;
				}
				if (GreatHalls == 1) {
					return 1;
				}
				if (GreatHalls == 2) {
					return 2;
				}
			}

			if (flag.equalsIgnoreCase("Caves")) {

				if (Caves == 0) {
					return 0;
				}
				if (Caves == 1) {
					return 1;
				}
				if (Caves == 2) {
					return 2;
				}
			}


		}

		return 3;
	}
	
	
	public static Boolean isFlagTeam(String flag, int team) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				if (SupplyCamp == 0 && team == 0) {
					return true;
				}
				if (SupplyCamp == 1 && team == 1) {
					return true;
				}
				if (SupplyCamp == 2 && team == 2) {
					return true;
				}

			}

			if (flag.equalsIgnoreCase("Horn")) {

				if (Horn == 0 && team == 0) {
					return true;
				}
				if (Horn == 1 && team == 1) {
					return true;
				}
				if (Horn == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("MainGate")) {

				if (MainGate == 0 && team == 0) {
					return true;
				}
				if (MainGate == 1 && team == 1) {
					return true;
				}
				if (MainGate == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("Courtyard")) {

				if (Courtyard == 0 && team == 0) {
					return true;
				}
				if (Courtyard == 1 && team == 1) {
					return true;
				}
				if (Courtyard == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("GreatHalls")) {

				if (GreatHalls == 0 && team == 0) {
					return true;
				}
				if (GreatHalls == 1 && team == 1) {
					return true;
				}
				if (GreatHalls == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("Caves")) {

				if (Caves == 0 && team == 0) {
					return true;
				}
				if (Caves == 1 && team == 1) {
					return true;
				}
				if (Caves == 2 && team == 2) {
					return true;
				}
				
			}


		}
		return false;

	}

}
