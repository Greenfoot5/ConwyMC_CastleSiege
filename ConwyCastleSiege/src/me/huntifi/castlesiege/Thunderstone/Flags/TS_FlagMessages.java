package me.huntifi.castlesiege.Thunderstone.Flags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


import me.huntifi.castlesiege.maps.currentMaps;

public class TS_FlagMessages {
	
	public static void onNeutralisation(String flag, int team) {
		
		if (currentMaps.currentMapIs("Thunderstone")) {
			
			if(TS_FlagTeam.isFlagTeam(flag, team) && TS_FlagRadius.FlagIsBeingCaptured(flag)) {
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "~~~ " + TS_FlagName.returnFlagName(flag) + " has been neutralised! ~~~");
			}
			
		}
		
	}
	
	public static void onCaptured(String flag, int team) {
		
		if (currentMaps.currentMapIs("Thunderstone")) {
			
			if(TS_FlagTeam.isFlagTeam(flag, team) && TS_FlagRadius.FlagIsBeingCaptured(flag)) {
				
				if (team == 2) {
					
					Bukkit.broadcastMessage(ChatColor.GOLD + "~~~ The Thunderstone Guard has captured " + TS_FlagName.returnFlagName(flag) + "! ~~~");
				}
				
				if (team == 1) {
					
					Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "~~~ The Cloudcrawlers have captured " + TS_FlagName.returnFlagName(flag) + "! ~~~");
				}
				
				
			}
			
		}
		
	}

}
