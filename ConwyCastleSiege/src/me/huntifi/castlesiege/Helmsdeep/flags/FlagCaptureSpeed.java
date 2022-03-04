package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FlagCaptureSpeed {
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");
	
	static int captureSpeed1 = 100; //Capture-speed in ticks, this is 5 seconds
	static int captureSpeed2 = 100; //For every flag
	static int captureSpeed3 = 100; 
	static int captureSpeed4 = 100; 
	static int captureSpeed5 = 100; 
	static int captureSpeed6 = 100; 
	
	public static int flagCaptureSpeed(String flag, int team) {
		
		if (flag.equalsIgnoreCase("SupplyCamp")) {
			
			if (team == 1) {
				
				if (FlagRadius.SupplyCamp1.size() == 1) {
					captureSpeed1 = 100;
				}
				if (FlagRadius.SupplyCamp1.size() == 2) {
					captureSpeed1 = 80;
				}
				if (FlagRadius.SupplyCamp1.size() == 3) {
					captureSpeed1 = 70;
				}
				if (FlagRadius.SupplyCamp1.size() == 4) {
					captureSpeed1 = 60;
				}
				if (FlagRadius.SupplyCamp1.size() >= 5) {
					captureSpeed1 = 50;
				}
				
			}
			
			if (team == 2) {
				
				if (FlagRadius.SupplyCamp2.size() == 1) {
					captureSpeed1 = 100;
				}
				if (FlagRadius.SupplyCamp2.size() == 2) {
					captureSpeed1 = 80;
				}
				if (FlagRadius.SupplyCamp2.size() == 3) {
					captureSpeed1 = 70;
				}
				if (FlagRadius.SupplyCamp2.size() == 4) {
					captureSpeed1 = 60;
				}
				if (FlagRadius.SupplyCamp2.size() >= 5) {
					captureSpeed1 = 50;
				}
				
			}

		}

		if (flag.equalsIgnoreCase("Horn")) {

			if (team == 1) {
				
				if (FlagRadius.Horn1.size() == 1) {
					captureSpeed2 = 100;
				}
				if (FlagRadius.Horn1.size() == 2) {
					captureSpeed2 = 80;
				}
				if (FlagRadius.Horn1.size() == 3) {
					captureSpeed2 = 70;
				}
				if (FlagRadius.Horn1.size() == 4) {
					captureSpeed2 = 60;
				}
				if (FlagRadius.Horn1.size() >= 5) {
					captureSpeed2 = 50;
				}
			}
			
			if (team == 2) {
				
				if (FlagRadius.Horn2.size() == 1) {
					captureSpeed2 = 100;
				}
				if (FlagRadius.Horn2.size() == 2) {
					captureSpeed2 = 80;
				}
				if (FlagRadius.Horn2.size() == 3) {
					captureSpeed2 = 70;
				}
				if (FlagRadius.Horn2.size() == 4) {
					captureSpeed2 = 60;
				}
				if (FlagRadius.Horn2.size() >= 5) {
					captureSpeed2 = 50;
				}
			}
			
		}

		if (flag.equalsIgnoreCase("MainGate")) {

			if (team == 1) {
				
				if (FlagRadius.MainGate1.size() == 1) {
					captureSpeed3 = 100;
				}
				if (FlagRadius.MainGate1.size() == 2) {
					captureSpeed3 = 80;
				}
				if (FlagRadius.MainGate1.size() == 3) {
					captureSpeed3 = 70;
				}
				if (FlagRadius.MainGate1.size() == 4) {
					captureSpeed3 = 60;
				}
				if (FlagRadius.MainGate1.size() >= 5) {
					captureSpeed3 = 50;
				}
				
			}
			
			if (team == 2) {
				
				if (FlagRadius.MainGate2.size() == 1) {
					captureSpeed3 = 100;
				}
				if (FlagRadius.MainGate2.size() == 2) {
					captureSpeed3 = 80;
				}
				if (FlagRadius.MainGate2.size() == 3) {
					captureSpeed3 = 70;
				}
				if (FlagRadius.MainGate2.size() == 4) {
					captureSpeed3 = 60;
				}
				if (FlagRadius.MainGate2.size() >= 5) {
					captureSpeed3 = 50;
				}
			}

		}

		if (flag.equalsIgnoreCase("Courtyard")) {

			
			if (team == 1) {
				
				if (FlagRadius.Courtyard1.size() == 1) {
					captureSpeed4 = 100;
				}
				if (FlagRadius.Courtyard1.size() == 2) {
					captureSpeed4 = 80;
				}
				if (FlagRadius.Courtyard1.size() == 3) {
					captureSpeed4 = 70;
				}
				if (FlagRadius.Courtyard1.size() == 4) {
					captureSpeed4 = 60;
				}
				if (FlagRadius.Courtyard1.size() >= 5) {
					captureSpeed4 = 50;
				}
			}
			
			if (team == 2) {
				
				if (FlagRadius.Courtyard2.size() == 1) {
					captureSpeed4 = 100;
				}
				if (FlagRadius.Courtyard2.size() == 2) {
					captureSpeed4 = 80;
				}
				if (FlagRadius.Courtyard2.size() == 3) {
					captureSpeed4 = 70;
				}
				if (FlagRadius.Courtyard2.size() == 4) {
					captureSpeed4 = 60;
				}
				if (FlagRadius.Courtyard2.size() >= 5) {
					captureSpeed4 = 50;
				}
			}
			
		}

		if (flag.equalsIgnoreCase("GreatHalls")) {

			
			if (team == 1) {
				
				if (FlagRadius.GreatHalls1.size() == 1) {
					captureSpeed5 = 100;
				}
				if (FlagRadius.GreatHalls1.size() == 2) {
					captureSpeed5 = 80;
				}
				if (FlagRadius.GreatHalls1.size() == 3) {
					captureSpeed5 = 70;
				}
				if (FlagRadius.GreatHalls1.size() == 4) {
					captureSpeed5 = 60;
				}
				if (FlagRadius.GreatHalls1.size() >= 5) {
					captureSpeed5 = 50;
				}
			}
			
			if (team == 2) {
				
				if (FlagRadius.GreatHalls2.size() == 1) {
					captureSpeed5 = 100;
				}
				if (FlagRadius.GreatHalls2.size() == 2) {
					captureSpeed5 = 80;
				}
				if (FlagRadius.GreatHalls2.size() == 3) {
					captureSpeed5 = 70;
				}
				if (FlagRadius.GreatHalls2.size() == 4) {
					captureSpeed5 = 60;
				}
				if (FlagRadius.GreatHalls2.size() >= 5) {
					captureSpeed5 = 50;
				}
			}

		}

		if (flag.equalsIgnoreCase("Caves")) {

			if (team == 1) {
				
				if (FlagRadius.Caves1.size() == 1) {
					captureSpeed6 = 100;
				}
				if (FlagRadius.Caves1.size() == 2) {
					captureSpeed6 = 80;
				}
				if (FlagRadius.Caves1.size() == 3) {
					captureSpeed6 = 70;
				}
				if (FlagRadius.Caves1.size() == 4) {
					captureSpeed6 = 60;
				}
				if (FlagRadius.Caves1.size() >= 5) {
					captureSpeed6 = 50;
				}
			}
			
			if (team == 2) {
				
				if (FlagRadius.Caves2.size() == 1) {
					captureSpeed6 = 100;
				}
				if (FlagRadius.Caves2.size() == 2) {
					captureSpeed6 = 80;
				}
				if (FlagRadius.Caves2.size() == 3) {
					captureSpeed6 = 70;
				}
				if (FlagRadius.Caves2.size() == 4) {
					captureSpeed6 = 60;
				}
				if (FlagRadius.Caves2.size() >= 5) {
					captureSpeed6 = 50;
				}
			}

			
		}
		return 100;
	}

}
