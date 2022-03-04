package me.huntifi.castlesiege.combat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Cake;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.huntifi.castlesiege.kits.medic.MedicAbilities;
import me.huntifi.castlesiege.woolmap.LobbyPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class EatCake implements Listener {

	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ConwyCastleSiege");

	static ArrayList<Player> hasEaten = new ArrayList<Player>();

	@EventHandler
	public void eatCake(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if (!LobbyPlayer.containsPlayer(p)) {

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

				if(e.getClickedBlock().getType().equals(Material.CAKE)) {

					Block block = e.getClickedBlock();
					BlockState state = block.getState();
					MaterialData data = state.getData();

					Cake cake = (Cake) data;

					if (!MedicAbilities.cakes.containsValue(block)) {

						if (!hasEaten.contains(p) && p.getHealth() != p.getMaxHealth()) {

							p.setFoodLevel(17);

							if (cake.getSlicesRemaining() <= 7) {

								p.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 40, 4)));

								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "The cake is healing you!"));

								hasEaten.add(p);

								new BukkitRunnable() {

									@Override
									public void run() {

										if (hasEaten.contains(p)) { hasEaten.remove(p);  }

									}
								}.runTaskLater(plugin, 40);

							} else {

								block.breakNaturally();
								state.update();

							}
						}

					} else {
						
						return;
					}
				}
			}
		}

	}

}
