package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Spearman extends Kit implements Listener, CommandExecutor {

	public Spearman() {
		super("Spearman");
		super.baseHeath = 115;


		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = createItem(new ItemStack(Material.STICK),
				ChatColor.GREEN + "Spear",
				Arrays.asList("", ChatColor.AQUA + "Right-click to throw a spear."),
				Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 48)));
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				createItem(new ItemStack(Material.STICK),
						ChatColor.GREEN + "Spear",
						Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage",
								ChatColor.AQUA + "Right-click to throw a spear."),
						Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 52))),
				0);

		// Chestplate
		es.chest = createItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
				ChatColor.GREEN + "Chainmail Chestplate", null, null);

		// Leggings
		es.legs = createItem(new ItemStack(Material.CHAINMAIL_LEGGINGS),
				ChatColor.GREEN + "Chainmail Leggings", null, null);

		// Boots
		es.feet = createItem(new ItemStack(Material.CHAINMAIL_BOOTS),
				ChatColor.GREEN + "Chainmail Boots", null, null);
		// Voted Boots
		es.votedFeet = createItem(new ItemStack(Material.CHAINMAIL_BOOTS),
				ChatColor.GREEN + "Chainmail Boots",
				Arrays.asList("", ChatColor.AQUA + "- voted: Depth Strider 2"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, 4);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

		super.equipment = es;


		// Death Messages
		super.projectileDeathMessage[0] = "You were impaled by ";
		super.projectileKillMessage[0] = "You impaled ";
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		super.addPlayer(((Player) commandSender).getUniqueId());
		return true;
	}

	@EventHandler
	public void Charge(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		int cooldown = p.getCooldown(Material.STICK);

		if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {
			if (p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) {
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

					if (cooldown == 0) {
						p.setCooldown(Material.STICK, 160);
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "You threw your spear!"));
						p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(2.0));

					} else {
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your spear yet."));
					}
				}
			}
		}
	}

	@EventHandler
	public void changeSpearDamage(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();

			if(arrow.getShooter() instanceof Player){
				Player damages = (Player) arrow.getShooter();

				if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
					arrow.setDamage(32);
				}
			}
		}
	}

	@EventHandler
	public void onBreakLadder(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (Objects.equals(Kit.equippedKits.get(p.getUniqueId()).name, name)) {

			Location l = e.getBlock().getLocation();
			while (l.getBlock().getType() == Material.LADDER) {
				l.getBlock().setType(Material.AIR);
				l.setY(l.getBlockY() - 1);
			}
		}
	}
}
