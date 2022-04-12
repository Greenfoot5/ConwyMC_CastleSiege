package me.huntifi.castlesiege.combat;

//import me.huntifi.castlesiege.teams.PlayerTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class HitMessage implements Listener {
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		
		if (e.getEntity() instanceof Arrow) {
			
			Arrow arrow = (Arrow) e.getEntity();
			
			if (arrow.getShooter() instanceof Player) {
				
				Player p = (Player) arrow.getShooter();
				
				if (e.getHitEntity() instanceof Player) {
					
					Player hit = (Player) e.getHitEntity();
					
					/*if (PlayerTeam.getPlayerTeam(p) != PlayerTeam.getPlayerTeam(hit)) {
					
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_AQUA + "Hit (" + hit.getName() + ")"));
					playSound(p);
					
					}*/
					
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

	public static void playSound(Player player) {

		Location location = player.getLocation();

		Sound effect = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

		float volume = 1f; //1 = 100%
		float pitch = 0.5f; //Float between 0.5 and 2.0

		player.playSound(location, effect, volume, pitch);
	}
}
