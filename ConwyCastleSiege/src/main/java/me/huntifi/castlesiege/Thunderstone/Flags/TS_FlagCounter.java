package me.huntifi.castlesiege.Thunderstone.Flags;

import me.huntifi.castlesiege.maps.MapController;

public class TS_FlagCounter {

	public static int LonelyTower = 0;
	public static int StairHall = 0;
	public static int WestTower = 0;
	public static int EastTower = 0;
	public static int SkyviewTower = 0;
	public static int ShiftedTower = 0;
	public static int TwinBridge = 0;

	public static void setFlagCounter(String flag, int counter) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("StairHall")) {

				StairHall = counter;

			}

			if (flag.equalsIgnoreCase("LonelyTower")) {

				LonelyTower = counter;

			}

			if (flag.equalsIgnoreCase("WestTower")) {

				WestTower = counter;
			}

			if (flag.equalsIgnoreCase("EastTower")) {


				EastTower = counter;


			}

			if (flag.equalsIgnoreCase("SkyviewTower")) {
				SkyviewTower = counter;

			}

			if (flag.equalsIgnoreCase("ShiftedTower")) {
				
				ShiftedTower = counter;
				
			}
			
			if (flag.equalsIgnoreCase("TwinBridge")) {
				
				TwinBridge = counter;
				
			}
		}
	}
	
	
	public static int getFlagCounter(String flag) {

		if (MapController.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("StairHall")) {

				return StairHall;

			}

			if (flag.equalsIgnoreCase("LonelyTower")) {

				return LonelyTower;

			}

			if (flag.equalsIgnoreCase("WestTower")) {

				return WestTower;
			}

			if (flag.equalsIgnoreCase("EastTower")) {


				return EastTower;


			}

			if (flag.equalsIgnoreCase("SkyviewTower")) {
				return SkyviewTower;

			}

			if (flag.equalsIgnoreCase("ShiftedTower")) {
				return ShiftedTower;
			}
			
			if (flag.equalsIgnoreCase("Twinbridge")) {
				return TwinBridge;
			}
		}
		return 0;
	}
	
	
	public static Boolean isFlagCounter(String flag, int counter) {

		if (MapController.currentMapIs("Thunderstone")) {


			if (flag.equalsIgnoreCase("stairhall") && StairHall == counter) {

				return true;

			}

			if (flag.equalsIgnoreCase("lonelytower") && LonelyTower == counter) {

				return true;

			}

			if (flag.equalsIgnoreCase("EastTower") && EastTower == counter) {

				return true;
			}

			if (flag.equalsIgnoreCase("WestTower") && WestTower == counter) {


				return true;


			}

			if (flag.equalsIgnoreCase("ShiftedTower") && ShiftedTower == counter) {
				return true;

			}

			if (flag.equalsIgnoreCase("twinbridge") && TwinBridge == counter) {
				
				return true;
				
			}
			
			if (flag.equalsIgnoreCase("skyviewtower") && SkyviewTower == counter) {
				
				return true;
				
			}
		}
		return false;
	}

}
