package me.huntifi.castlesiege.Thunderstone.Flags;

import me.huntifi.castlesiege.maps.currentMaps;

public class TS_FlagTeam {

	public static int stairhall = 2;
	public static int lonelytower = 2;
	public static int skyviewtower = 2;
	public static int easttower = 2;
	public static int westtower = 2;
	public static int twinbridge = 2;
	public static int shiftedtower = 2;

	public static int neutral = 0;
	public static int attackingTeam = 1;
	public static int defendingTeam = 2;

	public static void setFlagTeam(String flag, int team) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				if (team == 0) {
					stairhall = 0;
				}
				if (team == 1) {
					stairhall = 1;
				}
				if (team == 2) {
					stairhall = 2;
				}

			}

			if (flag.equalsIgnoreCase("skyviewtower")) {

				if (team == 0) {
					skyviewtower = 0;
				}
				if (team == 1) {
					skyviewtower = 1;
				}
				if (team == 2) {
					skyviewtower = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("easttower")) {

				if (team == 0) {
					easttower = 0;
				}
				if (team == 1) {
					easttower = 1;
				}
				if (team == 2) {
					easttower = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("westtower")) {

				if (team == 0) {
					westtower = 0;
				}
				if (team == 1) {
					westtower = 1;
				}
				if (team == 2) {
					westtower = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("lonelytower")) {

				if (team == 0) {
					
					lonelytower = 0;
				}
				if (team == 1) {
					lonelytower = 1;
				}
				if (team == 2) {
					lonelytower = 2;
				}
				
			}

			if (flag.equalsIgnoreCase("shiftedtower")) {

				if (team == 0) {
					shiftedtower = 0;
				}
				if (team == 1) {
					shiftedtower = 1;
				}
				if (team == 2) {
					shiftedtower = 2;
				}
				
			}
			
			if (flag.equalsIgnoreCase("twinbridge")) {

				if (team == 0) {
					twinbridge = 0;
				}
				if (team == 1) {
					twinbridge = 1;
				}
				if (team == 2) {
					twinbridge = 2;
				}
				
			}


		}

	}

	public static Integer getFlagTeam(String flag) {

		if (currentMaps.currentMapIs("Thunderstone")) {


			if (flag.equalsIgnoreCase("stairhall")) {

				if (stairhall == 0) {
					return 0;
				}
				if (stairhall == 1) {
					return 1;
				}
				if (stairhall == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("skyviewtower")) {

				if (skyviewtower == 0) {
					return 0;
				}
				if (skyviewtower == 1) {
					return 1;
				}
				if (skyviewtower == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("easttower")) {

				if (easttower == 0) {
					return 0;
				}
				if (easttower == 1) {
					return 1;
				}
				if (easttower == 2) {
					return 2;
				}
				
			}

			if (flag.equalsIgnoreCase("westtower")) {

				if (westtower == 0) {
					return 0;
				}
				if (westtower == 1) {
					return 1;
				}
				if (westtower == 2) {
					return 2;
				}
			}

			if (flag.equalsIgnoreCase("lonelytower")) {

				if (lonelytower == 0) {
					return 0;
				}
				if (lonelytower == 1) {
					return 1;
				}
				if (lonelytower == 2) {
					return 2;
				}
			}

			if (flag.equalsIgnoreCase("shiftedtower")) {

				if (shiftedtower == 0) {
					return 0;
				}
				if (shiftedtower == 1) {
					return 1;
				}
				if (shiftedtower == 2) {
					return 2;
				}
			}
			
			if (flag.equalsIgnoreCase("twinbridge")) {

				if (twinbridge == 0) {
					return 0;
				}
				if (twinbridge == 1) {
					return 1;
				}
				if (twinbridge == 2) {
					return 2;
				}
			}


		}

		return 3;
	}
	
	
	public static Boolean isFlagTeam(String flag, int team) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				if (stairhall == 0 && team == 0) {
					return true;
				}
				if (stairhall == 1 && team == 1) {
					return true;
				}
				if (stairhall == 2 && team == 2) {
					return true;
				}

			}

			if (flag.equalsIgnoreCase("skyviewtower")) {

				if (skyviewtower == 0 && team == 0) {
					return true;
				}
				if (skyviewtower == 1 && team == 1) {
					return true;
				}
				if (skyviewtower == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("easttower")) {

				if (easttower == 0 && team == 0) {
					return true;
				}
				if (easttower == 1 && team == 1) {
					return true;
				}
				if (easttower == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("westtower")) {

				if (westtower == 0 && team == 0) {
					return true;
				}
				if (westtower == 1 && team == 1) {
					return true;
				}
				if (westtower == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("lonelytower")) {

				if (lonelytower == 0 && team == 0) {
					return true;
				}
				if (lonelytower == 1 && team == 1) {
					return true;
				}
				if (lonelytower == 2 && team == 2) {
					return true;
				}
				
			}

			if (flag.equalsIgnoreCase("shiftedtower")) {

				if (shiftedtower == 0 && team == 0) {
					return true;
				}
				if (shiftedtower == 1 && team == 1) {
					return true;
				}
				if (shiftedtower == 2 && team == 2) {
					return true;
				}
				
			}
			
			if (flag.equalsIgnoreCase("twinbridge")) {

				if (twinbridge == 0 && team == 0) {
					return true;
				}
				if (twinbridge == 1 && team == 1) {
					return true;
				}
				if (twinbridge == 2 && team == 2) {
					return true;
				}
				
			}


		}
		return false;

	}

}
