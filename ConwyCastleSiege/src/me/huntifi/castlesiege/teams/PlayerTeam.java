package me.huntifi.castlesiege.teams;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.huntifi.castlesiege.Helmsdeep.flags.FlagRadius;
import me.huntifi.castlesiege.Thunderstone.Flags.TS_FlagRadius;
import me.huntifi.castlesiege.maps.currentMaps;

public class PlayerTeam {
	
	//Helm's Deep
	public static ArrayList<Player> Rohan = new ArrayList<Player>();
	public static ArrayList<Player> Urukhai = new ArrayList<Player>();
	
	//Thunderstone
	public static ArrayList<Player> Cloudcrawlers = new ArrayList<Player>();
	public static ArrayList<Player> ThunderstoneGuards = new ArrayList<Player>();
	
	//Everything to do with adding, removing and checking player teams
	
	//team 1 == attacking, team 2 == defending
	
	public static void setPlayerTeam(Player p, int team) {
		
		if (currentMaps.currentMapIs("HelmsDeep")) {
			
			if (team == 1) {
				
				Urukhai.add(p);
			}
			
			if (team == 2) {
				
				Rohan.add(p);
			}
			
		} else
		
		if (currentMaps.currentMapIs("Thunderstone")) {
			
			if (team == 1) {
				
				Cloudcrawlers.add(p);
			}
			
			if (team == 2) {
				
				ThunderstoneGuards.add(p);
			}
			
		}
	}
	
	
	public static void switchPlayerTeam(Player p, int currentTeam) {
		
		if (currentMaps.currentMapIs("HelmsDeep")) {
			
			FlagRadius.removePlayerFromAll(p);
			
			if (currentTeam == 2) {
				
				if (Rohan.contains(p))  { Rohan.remove(p); }
				if (!Urukhai.contains(p))  { Urukhai.add(p); }
			}
			
			if (currentTeam == 1) {
				
				if (Urukhai.contains(p))  {  Urukhai.remove(p); }
				if (!Rohan.contains(p))  { Rohan.add(p); }
			}
			
		} else if (currentMaps.currentMapIs("Thunderstone")) {
			
			TS_FlagRadius.removePlayerFromAll(p);
			
			if (currentTeam == 2) {
				
				if (ThunderstoneGuards.contains(p))  { ThunderstoneGuards.remove(p); }
				if (!Cloudcrawlers.contains(p))  { Cloudcrawlers.add(p); }
			}
			
			if (currentTeam == 1) {
				
				if (Cloudcrawlers.contains(p))  {  Cloudcrawlers.remove(p); }
				if (!ThunderstoneGuards.contains(p))  { ThunderstoneGuards.add(p); }
			}
			
		}
	}
	
	public static int getPlayerTeam(Player p) {
		
		if (currentMaps.currentMapIs("HelmsDeep")) {
			
			if (Urukhai.contains(p)) {
				
				return 1;
			}
			
			if (Rohan.contains(p)) {
				
				return 2;
			}
			
		} else if (currentMaps.currentMapIs("Thunderstone")) {
			
			if (Cloudcrawlers.contains(p)) {
				
				return 1;
			}
			
			if (ThunderstoneGuards.contains(p)) {
				
				return 2;
			}
			
		}
		
		return 0;
	}
	
	public static void removePlayerFromTeam(Player p, int team) {
		
		if (currentMaps.currentMapIs("HelmsDeep")) {
			
			if (team == 1) {
				
				Urukhai.remove(p);
			}
			
			if (team == 2) {
				
				Rohan.remove(p);
			}
			
		} else if (currentMaps.currentMapIs("Thunderstone")) {
			
			if (team == 1) {
				
				Cloudcrawlers.remove(p);
			}
			
			if (team == 2) {
				
				ThunderstoneGuards.remove(p);
			}
			
		}
	}
	
	public static Boolean playerIsInTeam(Player p, int team) {
		
		if (currentMaps.currentMapIs("HelmsDeep")) {
			
			if (Urukhai.contains(p) && team == 1) {
				
				return true;
			}
			
			if (Rohan.contains(p) && team == 2) {
				
				return true;
			}
			
		} else if (currentMaps.currentMapIs("Thunderstone")) {
			
			if (Cloudcrawlers.contains(p) && team == 1) {
				
				return true;
			}
			
			if (ThunderstoneGuards.contains(p) && team == 2) {
				
				return true;
			}
			
		}
		
		return false;
		
		
	}
	
	public static int rohanTeamSize() {
		return Rohan.size();

	}
	
	public static int urukhaiTeamSize() {
		return Urukhai.size();

	}
	
	public static int cloudcrawlersTeamSize() {
		return Cloudcrawlers.size();

	}
	
	public static int thunderstoneGuardsTeamSize() {
		return ThunderstoneGuards.size();

	}

}
