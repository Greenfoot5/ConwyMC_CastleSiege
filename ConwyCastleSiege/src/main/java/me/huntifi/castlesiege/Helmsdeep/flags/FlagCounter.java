package me.huntifi.castlesiege.Helmsdeep.flags;


import me.huntifi.castlesiege.maps.MapController;


public class FlagCounter {

	public static int SupplyCamp = 0;
	public static int Horn = 0;
	public static int MainGate = 0;
	public static int Courtyard = 0;
	public static int GreatHalls = 0;
	public static int Caves = 0;

	public static void setFlagCounter(String flag, int counter) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				SupplyCamp = counter;

			}

			if (flag.equalsIgnoreCase("Horn")) {

				Horn = counter;

			}

			if (flag.equalsIgnoreCase("MainGate")) {

				MainGate = counter;
			}

			if (flag.equalsIgnoreCase("Courtyard")) {


				Courtyard = counter;


			}

			if (flag.equalsIgnoreCase("GreatHalls")) {
				GreatHalls = counter;

			}

			if (flag.equalsIgnoreCase("Caves")) {
				Caves = counter;
			}
		}
	}
	
	
	public static int getFlagCounter(String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (flag.equalsIgnoreCase("SupplyCamp")) {

				return SupplyCamp;

			}

			if (flag.equalsIgnoreCase("Horn")) {

				return Horn;

			}

			if (flag.equalsIgnoreCase("MainGate")) {

				return MainGate;
			}

			if (flag.equalsIgnoreCase("Courtyard")) {


				return Courtyard;


			}

			if (flag.equalsIgnoreCase("GreatHalls")) {
				return GreatHalls;

			}

			if (flag.equalsIgnoreCase("Caves")) {
				return Caves;
			}
		}
		return 0;
	}
	
	
	public static Boolean isFlagCounter(String flag, int counter) {

		if (MapController.currentMapIs("HelmsDeep")) {


			if (flag.equalsIgnoreCase("SupplyCamp") && SupplyCamp == counter) {

				return true;

			}

			if (flag.equalsIgnoreCase("Horn") && Horn == counter) {

				return true;

			}

			if (flag.equalsIgnoreCase("MainGate") && MainGate == counter) {

				return true;
			}

			if (flag.equalsIgnoreCase("Courtyard") && Courtyard == counter) {


				return true;


			}

			if (flag.equalsIgnoreCase("GreatHalls") && GreatHalls == counter) {
				return true;

			}

			if (flag.equalsIgnoreCase("Caves") && Caves == counter) {
				return true;
			}
		}
		return false;
	}

}
