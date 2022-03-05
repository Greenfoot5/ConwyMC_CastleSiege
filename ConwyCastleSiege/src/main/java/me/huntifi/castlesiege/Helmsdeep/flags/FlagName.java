package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.entity.Player;

import me.huntifi.castlesiege.maps.MapController;

public class FlagName {

	//Returns the flag name if the player is in the radius of that flag

	public static String returnPlayerFlagName(Player p, String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {

			if (FlagRadius.isPlayerInRadius(p, flag)) {

				if (flag.equalsIgnoreCase("SupplyCamp")) {

					return "Supply Camp";
				}

				if (flag.equalsIgnoreCase("Courtyard")) {

					return "Courtyard";
				}

				if (flag.equalsIgnoreCase("Horn")) {

					return "Horn";
				}

				if (flag.equalsIgnoreCase("MainGate")) {

					return "Main Gate";
				}

				if (flag.equalsIgnoreCase("GreatHalls")) {

					return "Great Halls";
				}

				if (flag.equalsIgnoreCase("Caves")) {

					return "The Glittering Caves";
				}



			}

		}
		return "";

	}




	public static String returnFlagName(String flag) {

		if (MapController.currentMapIs("HelmsDeep")) {



			if (flag.equalsIgnoreCase("SupplyCamp")) {

				return "The Supply Camp";
			}

			if (flag.equalsIgnoreCase("Courtyard")) {

				return "The Courtyard";
			}

			if (flag.equalsIgnoreCase("Horn")) {

				return "The Horn of Helm Hammerhand";
			}

			if (flag.equalsIgnoreCase("MainGate")) {

				return "The Main Gate";
			}

			if (flag.equalsIgnoreCase("GreatHalls")) {

				return "The Great Halls";
			}

			if (flag.equalsIgnoreCase("Caves")) {

				return "The Glittering Caves";
			}



		}


		return "";

	}

}
