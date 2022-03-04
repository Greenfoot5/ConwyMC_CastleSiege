package me.huntifi.castlesiege.kits.Spearman;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.huntifi.castlesiege.joinevents.stats.StatsChanging;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class SpearmanAbility implements Listener {

	Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	public static int getAmount(Player arg0, ItemStack arg1) {
		if (arg1 == null)
			return 0;
		int amount = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack slot = arg0.getInventory().getItem(i);
			if (slot == null || !slot.isSimilar(arg1))
				continue;
			amount += slot.getAmount();
		}
		return amount;
	}

	@EventHandler
	public void Charge(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		SpearmanKit SpearmangiveItems = new SpearmanKit(); 
		ItemStack SpearmanSpears = SpearmangiveItems.getSpearmanSpears();

		int cooldown = p.getCooldown(SpearmanSpears.getType());

		if (StatsChanging.getKit(p.getUniqueId()).equalsIgnoreCase("Spearman")) {
			if (!LobbyPlayer.containsPlayer(p)) {
				if (!p.getInventory().getItemInMainHand().getType().equals(null)) {
					if (p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
						if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {


							if (cooldown == 0) {

								p.setCooldown(SpearmanSpears.getType(), 160);

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You threw your spear!"));

								p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.0));

							} else {

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your spear yet."));

							}


						}
					}
				}

			} else {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't use that here!"));
			}
		} 


	}
	
	@EventHandler
	public void changeSpearDamage(ProjectileHitEvent e) {
		
		if (e.getEntity() instanceof Arrow) {
			
			Arrow arrow = (Arrow) e.getEntity();
			if(arrow.getShooter() instanceof Player){
				Player damager = (Player) arrow.getShooter();
				if (StatsChanging.getKit(damager.getUniqueId()).equalsIgnoreCase("Spearman")) {
					arrow.setDamage(32);
				} 

			}
			
		}
		
	}

}
