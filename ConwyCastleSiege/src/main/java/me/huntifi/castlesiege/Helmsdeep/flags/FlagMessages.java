package me.huntifi.castlesiege.Helmsdeep.flags;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.huntifi.castlesiege.maps.MapController;

public class FlagMessages {
	
	public static void onNeutralisation(String flag, int team) {
		
		if (MapController.currentMapIs("HelmsDeep")) {
			
			if(FlagTeam.isFlagTeam(flag, team) && FlagRadius.FlagIsBeingCaptured(flag)) {
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "~~~ " + FlagName.returnFlagName(flag) + " has been neutralised! ~~~");
			}
			
		}
		
	}
	
	public static void onCaptured(String flag, int team) {
		
		if (MapController.currentMapIs("HelmsDeep")) {
			
			if(FlagTeam.isFlagTeam(flag, team) && FlagRadius.FlagIsBeingCaptured(flag)) {
				
				if (team == 2) {
					
					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "~~~ Rohan has captured " + FlagName.returnFlagName(flag) + "! ~~~");
				}
				
				if (team == 1) {
					
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "~~~ The Uruk-hai have captured " + FlagName.returnFlagName(flag) + "! ~~~");
				}
				
				
			}
			
		}
		
	}

}
