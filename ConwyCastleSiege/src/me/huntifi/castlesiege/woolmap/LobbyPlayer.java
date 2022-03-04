package me.huntifi.castlesiege.woolmap;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class LobbyPlayer {
	
	public static ArrayList<Player> lobby = new ArrayList<Player>();
	
	public static void addPlayer(Player p) {
		
		lobby.add(p);
	}
	
	public static void removePlayer(Player p) {
		
		lobby.remove(p);
	}
	
	public static boolean containsPlayer(Player p) {
		
		if (lobby.contains(p)) {
			
			return true;
		}
		
		return false;
	}

}
