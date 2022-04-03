package me.huntifi.castlesiege.Thunderstone.Flags;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TS_FlagCaptureSpeed {
	
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CastleSiege");
	
	static int captureSpeed1 = 100; //Capture-speed in ticks, this is 5 seconds
	static int captureSpeed2 = 100; //For every flag
	static int captureSpeed3 = 100; 
	static int captureSpeed4 = 100; 
	static int captureSpeed5 = 100; 
	static int captureSpeed6 = 100; 
	
	public static int flagCaptureSpeed(String flag, int team) {
		
		if (flag.equalsIgnoreCase("stairhall")) {
			
			if (team == 1) {
				
				if (TS_FlagRadius.stairhall1.size() == 1) {
					captureSpeed1 = 100;
				}
				if (TS_FlagRadius.stairhall1.size() == 2) {
					captureSpeed1 = 85;
				}
				if (TS_FlagRadius.stairhall1.size() == 3) {
					captureSpeed1 = 75;
				}
				if (TS_FlagRadius.stairhall1.size() == 4) {
					captureSpeed1 = 60;
				}
				if (TS_FlagRadius.stairhall1.size() >= 5) {
					captureSpeed1 = 50;
				}
				
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.stairhall2.size() == 1) {
					captureSpeed1 = 100;
				}
				if (TS_FlagRadius.stairhall2.size() == 2) {
					captureSpeed1 = 85;
				}
				if (TS_FlagRadius.stairhall2.size() == 3) {
					captureSpeed1 = 75;
				}
				if (TS_FlagRadius.stairhall2.size() == 4) {
					captureSpeed1 = 60;
				}
				if (TS_FlagRadius.stairhall2.size() >= 5) {
					captureSpeed1 = 50;
				}
				
			}

		}

		if (flag.equalsIgnoreCase("skyviewtower")) {

			if (team == 1) {
				
				if (TS_FlagRadius.skyviewtower1.size() == 1) {
					captureSpeed2 = 100;
				}
				if (TS_FlagRadius.skyviewtower1.size() == 2) {
					captureSpeed2 = 85;
				}
				if (TS_FlagRadius.skyviewtower1.size() == 3) {
					captureSpeed2 = 75;
				}
				if (TS_FlagRadius.skyviewtower1.size() == 4) {
					captureSpeed2 = 60;
				}
				if (TS_FlagRadius.skyviewtower1.size() >= 5) {
					captureSpeed2 = 50;
				}
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.skyviewtower2.size() == 1) {
					captureSpeed2 = 100;
				}
				if (TS_FlagRadius.skyviewtower2.size() == 2) {
					captureSpeed2 = 85;
				}
				if (TS_FlagRadius.skyviewtower2.size() == 3) {
					captureSpeed2 = 75;
				}
				if (TS_FlagRadius.skyviewtower2.size() == 4) {
					captureSpeed2 = 60;
				}
				if (TS_FlagRadius.skyviewtower2.size() >= 5) {
					captureSpeed2 = 50;
				}
			}
			
		}

		if (flag.equalsIgnoreCase("westtower")) {

			if (team == 1) {
				
				if (TS_FlagRadius.westtower1.size() == 1) {
					captureSpeed3 = 100;
				}
				if (TS_FlagRadius.westtower1.size() == 2) {
					captureSpeed3 = 85;
				}
				if (TS_FlagRadius.westtower1.size() == 3) {
					captureSpeed3 = 75;
				}
				if (TS_FlagRadius.westtower1.size() == 4) {
					captureSpeed3 = 60;
				}
				if (TS_FlagRadius.westtower1.size() >= 5) {
					captureSpeed3 = 50;
				}
				
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.westtower2.size() == 1) {
					captureSpeed3 = 100;
				}
				if (TS_FlagRadius.westtower2.size() == 2) {
					captureSpeed3 = 85;
				}
				if (TS_FlagRadius.westtower2.size() == 3) {
					captureSpeed3 = 75;
				}
				if (TS_FlagRadius.westtower2.size() == 4) {
					captureSpeed3 = 60;
				}
				if (TS_FlagRadius.westtower2.size() >= 5) {
					captureSpeed3 = 50;
				}
			}

		}

		if (flag.equalsIgnoreCase("easttower")) {

			
			if (team == 1) {
				
				if (TS_FlagRadius.easttower1.size() == 1) {
					captureSpeed4 = 100;
				}
				if (TS_FlagRadius.easttower1.size() == 2) {
					captureSpeed4 = 85;
				}
				if (TS_FlagRadius.easttower1.size() == 3) {
					captureSpeed4 = 75;
				}
				if (TS_FlagRadius.easttower1.size() == 4) {
					captureSpeed4 = 60;
				}
				if (TS_FlagRadius.easttower1.size() >= 5) {
					captureSpeed4 = 50;
				}
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.easttower2.size() == 1) {
					captureSpeed4 = 100;
				}
				if (TS_FlagRadius.easttower2.size() == 2) {
					captureSpeed4 = 85;
				}
				if (TS_FlagRadius.easttower2.size() == 3) {
					captureSpeed4 = 75;
				}
				if (TS_FlagRadius.easttower2.size() == 4) {
					captureSpeed4 = 60;
				}
				if (TS_FlagRadius.easttower2.size() >= 5) {
					captureSpeed4 = 50;
				}
			}
			
		}

		if (flag.equalsIgnoreCase("lonelytower")) {

			
			if (team == 1) {
				
				if (TS_FlagRadius.lonelytower1.size() == 1) {
					captureSpeed5 = 100;
				}
				if (TS_FlagRadius.lonelytower1.size() == 2) {
					captureSpeed5 = 85;
				}
				if (TS_FlagRadius.lonelytower1.size() == 3) {
					captureSpeed5 = 75;
				}
				if (TS_FlagRadius.lonelytower1.size() == 4) {
					captureSpeed5 = 60;
				}
				if (TS_FlagRadius.lonelytower1.size() >= 5) {
					captureSpeed5 = 50;
				}
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.lonelytower2.size() == 1) {
					captureSpeed5 = 100;
				}
				if (TS_FlagRadius.lonelytower2.size() == 2) {
					captureSpeed5 = 85;
				}
				if (TS_FlagRadius.lonelytower2.size() == 3) {
					captureSpeed5 = 75;
				}
				if (TS_FlagRadius.lonelytower2.size() == 4) {
					captureSpeed5 = 60;
				}
				if (TS_FlagRadius.lonelytower2.size() >= 5) {
					captureSpeed5 = 50;
				}
			}

		}

		if (flag.equalsIgnoreCase("shiftedtower")) {

			if (team == 1) {
				
				if (TS_FlagRadius.shiftedtower1.size() == 1) {
					captureSpeed6 = 100;
				}
				if (TS_FlagRadius.shiftedtower1.size() == 2) {
					captureSpeed6 = 85;
				}
				if (TS_FlagRadius.shiftedtower1.size() == 3) {
					captureSpeed6 = 75;
				}
				if (TS_FlagRadius.shiftedtower1.size() == 4) {
					captureSpeed6 = 60;
				}
				if (TS_FlagRadius.shiftedtower1.size() >= 5) {
					captureSpeed6 = 50;
				}
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.shiftedtower2.size() == 1) {
					captureSpeed6 = 100;
				}
				if (TS_FlagRadius.shiftedtower2.size() == 2) {
					captureSpeed6 = 85;
				}
				if (TS_FlagRadius.shiftedtower2.size() == 3) {
					captureSpeed6 = 75;
				}
				if (TS_FlagRadius.shiftedtower2.size() == 4) {
					captureSpeed6 = 60;
				}
				if (TS_FlagRadius.shiftedtower2.size() >= 5) {
					captureSpeed6 = 50;
				}
			}

			
		}
		
		if (flag.equalsIgnoreCase("twinbridge")) {

			if (team == 1) {
				
				if (TS_FlagRadius.twinbridge1.size() == 1) {
					captureSpeed6 = 100;
				}
				if (TS_FlagRadius.twinbridge1.size() == 2) {
					captureSpeed6 = 85;
				}
				if (TS_FlagRadius.twinbridge1.size() == 3) {
					captureSpeed6 = 75;
				}
				if (TS_FlagRadius.twinbridge1.size() == 4) {
					captureSpeed6 = 60;
				}
				if (TS_FlagRadius.twinbridge1.size() >= 5) {
					captureSpeed6 = 50;
				}
			}
			
			if (team == 2) {
				
				if (TS_FlagRadius.twinbridge2.size() == 1) {
					captureSpeed6 = 100;
				}
				if (TS_FlagRadius.twinbridge2.size() == 2) {
					captureSpeed6 = 85;
				}
				if (TS_FlagRadius.twinbridge2.size() == 3) {
					captureSpeed6 = 75;
				}
				if (TS_FlagRadius.twinbridge2.size() == 4) {
					captureSpeed6 = 60;
				}
				if (TS_FlagRadius.twinbridge2.size() >= 5) {
					captureSpeed6 = 50;
				}
			}

			
		}
		
		return 100;
	}

}
