package me.huntifi.castlesiege.database;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.database.AsyncGetters.StringCallback;

public class StatsStrings {
	
	public static void returnKit(Player p) {
		
		UUID uuid = p.getUniqueId();
		
		StringCallback callback = new StringCallback() {

			@Override
			public void gotString(String string) {
				
				p.sendMessage(ChatColor.YELLOW + "Kit: " + string);

			}


		};
		
		AsyncGetters.performLookupKit(callback, p, SQLStats.getKit(uuid), true);
		
	}
	
	
	public static String returnRank(Player p) {
		
		UUID uuid = p.getUniqueId();
		
		StringCallback callback = new StringCallback() {

			@Override
			public void gotString(String string) {
				
				p.sendMessage(ChatColor.YELLOW + "Kit: " + string);
				
				return;

			}


		};
		
		AsyncGetters.performLookupKit(callback, p, SQLStats.getKit(uuid), true);
		return "";
		
	}

}
