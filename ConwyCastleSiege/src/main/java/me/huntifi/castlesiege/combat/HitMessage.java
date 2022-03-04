package me.huntifi.castlesiege.combat;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffects;

public class HitMessage implements Listener {
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		
		if (e.getEntity() instanceof Arrow) {
			
			Arrow arrow = (Arrow) e.getEntity();
			
			if (arrow.getShooter() instanceof Player) {
				
				Player p = (Player) arrow.getShooter();
				
				if (e.getHitEntity() instanceof Player) {
					
					Player hit = (Player) e.getHitEntity();
					
					if (PlayerTeam.getPlayerTeam(p) != PlayerTeam.getPlayerTeam(hit)) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + hit.getName() + ")"));
					playSound(p);
					
					}
					
				} else if (e.getHitEntity() instanceof Horse) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} else if (e.getHitEntity() instanceof Chicken) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} else if (e.getHitEntity() instanceof Cow) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} else if (e.getHitEntity() instanceof Wolf) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} else if (e.getHitEntity() instanceof Sheep) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} else if (e.getHitEntity() instanceof Bat) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + e.getHitEntity().getType() + ")"));
					playSound(p);
					
				} 
				
				
			}
			
		}
		
		
	}
	
	
	public void playSound(Player p) {
		
		CraftPlayer craftPlayer = (CraftPlayer) p;
		
		Location l = p.getLocation();
		
		SoundEffect effect = SoundEffects.ENTITY_EXPERIENCE_ORB_PICKUP;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0
		
		PacketPlayOutNamedSoundEffect packet;
		packet  = new PacketPlayOutNamedSoundEffect(effect, SoundCategory.PLAYERS, l.getX(), l.getY(), l.getZ(),  volume, pitch);
		craftPlayer.getHandle().playerConnection.sendPacket(packet);
	}

}
