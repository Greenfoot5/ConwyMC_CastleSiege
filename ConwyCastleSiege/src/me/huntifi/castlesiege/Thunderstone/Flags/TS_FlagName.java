package me.huntifi.castlesiege.Thunderstone.Flags;

import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.currentMaps;

public class TS_FlagName {
	
	//Returns the flag name if the player is in the radius of that flag

	public static String returnPlayerFlagName(Player p, String flag) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			if (TS_FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("stairhall")) {

					return "The Stairhall";
				}

				if (flag.equalsIgnoreCase("skyviewtower")) {

					return "Skyview Tower";
				}

				if (flag.equalsIgnoreCase("easttower")) {

					return "The East Tower";
				}

				if (flag.equalsIgnoreCase("westtower")) {

					return "The West Tower";
				}

				if (flag.equalsIgnoreCase("shiftedtower")) {

					return "The Shifted Tower";
				}

				if (flag.equalsIgnoreCase("twinbridge")) {

					return "The Twinbridge";
				}
				
				if (flag.equalsIgnoreCase("lonelytower")) {

					return "The Lonely Tower";
				}



			}

		}
		return "";

	}




	public static String returnFlagName(String flag) {

		if (currentMaps.currentMapIs("Thunderstone")) {

			if (flag.equalsIgnoreCase("stairhall")) {

				return "The Stairhall";
			}

			if (flag.equalsIgnoreCase("skyviewtower")) {

				return "Skyview Tower";
			}

			if (flag.equalsIgnoreCase("easttower")) {

				return "The East Tower";
			}

			if (flag.equalsIgnoreCase("westtower")) {

				return "The West Tower";
			}

			if (flag.equalsIgnoreCase("shiftedtower")) {

				return "The Shifted Tower";
			}

			if (flag.equalsIgnoreCase("twinbridge")) {

				return "The Twinbridge";
			}
			
			if (flag.equalsIgnoreCase("lonelytower")) {

				return "The Lonely Tower";
			}

		}


		return "";

	}

}
