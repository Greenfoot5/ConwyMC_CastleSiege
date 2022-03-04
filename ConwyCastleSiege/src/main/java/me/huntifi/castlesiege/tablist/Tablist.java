package me.huntifi.castlesiege.tablist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.huntifi.castlesiege.Main;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;

public class Tablist {
	
	private List<ChatComponentText> headers = new ArrayList<>();
	private List<ChatComponentText> footers = new ArrayList<>();
	
	private Main plugin;
	
	public Tablist(Main plugin) {
		this.plugin = plugin;
		
	}
	
	public void showTab() {
		
		if (headers.isEmpty() && footers.isEmpty()) return;
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			int count1 = 0;   //headers (Cause the code wouldn't know how many headers or footers there would be)
			int count2 = 0;   //footers

			@Override
			public void run() {
				
				try {
					
					Field a = packet.getClass().getDeclaredField("header");   //in versions below 1.13, header would be a & footer would be b.
					a.setAccessible(true);
					
					Field b = packet.getClass().getDeclaredField("footer");
					b.setAccessible(true);
					
					if (count1 >= headers.size()) count1 = 0;
					
					if (count2 >= footers.size()) count2 = 0;
					
					a.set(packet, headers.get(count1));
					b.set(packet, footers.get(count2));
					
					if (Bukkit.getOnlinePlayers().size() != 0) {
						
						for (Player player : Bukkit.getOnlinePlayers()) ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
							
					}
					
					//^^^^ send the tab packet to all players online
					
					count1++;
					count2++;
					
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			} }, 10, 40);
		
	}
	
	public void addHeader(String header) {
		
		headers.add(new ChatComponentText(format(header)));
		
	}
	
	public void addFooter(String footer) {
		
		footers.add(new ChatComponentText(format(footer)));
		
	}
	
	private String format(String msg) {
		
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
