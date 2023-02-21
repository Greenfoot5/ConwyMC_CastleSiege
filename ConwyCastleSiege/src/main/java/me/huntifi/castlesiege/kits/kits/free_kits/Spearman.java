package me.huntifi.castlesiege.kits.kits.free_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.FreeKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * The spearman kit
 */
public class Spearman extends FreeKit implements Listener {
	private static final int health = 260;
	private static final double regen = 12;
	private static final int spearCount = 4;
	private static final double meleeDamage = 45;
	private static final int ladderCount = 4;

	// Spear Throw
	private static final int throwCooldown = 160;
	private static final double throwVelocity = 2.0;
	private static final int throwDelay = 5;
	private static final double throwDamage = 60;

	// Damage multiplier when hitting horses
	private static final double HORSE_MULTIPLIER = 1.5;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Spearman() {
		super("Spearman", health, regen);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();
		super.heldItemSlot = 0;

		// Weapon
		es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STICK, spearCount),
				ChatColor.GREEN + "Spear",
				Collections.singletonList(ChatColor.AQUA + "Right-click to throw a spear."), null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				ItemCreator.weapon(new ItemStack(Material.STICK, 4),
						ChatColor.GREEN + "Spear",
						Arrays.asList(ChatColor.AQUA + "Right-click to throw a spear.",
								ChatColor.AQUA + "- voted: +2 damage"),
						Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), meleeDamage + 2),
				0);

		// Chestplate
		es.chest = ItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
				ChatColor.GREEN + "Chainmail Chestplate", null, null);

		// Leggings
		es.legs = ItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
				ChatColor.GREEN + "Chainmail Leggings", null, null);

		// Boots
		es.feet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
				ChatColor.GREEN + "Chainmail Boots", null, null);
		// Voted Boots
		es.votedFeet = ItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
				ChatColor.GREEN + "Chainmail Boots",
				Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
				Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

		// Ladders
		es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
		es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

		super.equipment = es;


		// Death Messages
		super.projectileDeathMessage[0] = "You were impaled by ";
		super.projectileKillMessage[0] = " impaled ";
	}

	/**
	 * Activate the spearman ability of throwing a spear
	 * @param e The event called when right-clicking with a stick
	 */
	@EventHandler
	public void throwSpear(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		ItemStack stick = p.getInventory().getItemInMainHand();
		int cooldown = p.getCooldown(Material.STICK);

		// Prevent using in lobby
		if (InCombat.isPlayerInLobby(uuid)) {
			return;
		}

		if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {
			if (stick.getType().equals(Material.STICK)) {
				if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (cooldown == 0) {
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
								ChatColor.AQUA + "Preparing to throw your spear!"));
						stick.setAmount(stick.getAmount() - 1);
						p.setCooldown(Material.STICK, throwCooldown);
						BarCooldown.add(uuid, throwDelay);
						new BukkitRunnable() {
							@Override
							public void run() {
								p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
										ChatColor.AQUA + "You threw your spear!"));
								p.launchProjectile(Arrow.class).setVelocity(p.getLocation().getDirection().multiply(throwVelocity));
							}
						}.runTaskLater(Main.plugin, throwDelay);
					} else {
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
								ChatColor.DARK_RED + "" + ChatColor.BOLD + "You can't throw your spear yet."));
					}
				}
			}
		}
	}

	/**
	 * Set the thrown spear's damage
	 * @param e The event called when an arrow hits a player
	 */
	@EventHandler (priority = EventPriority.LOW)
	public void changeSpearDamage(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();

			if(arrow.getShooter() instanceof Player){
				Player damages = (Player) arrow.getShooter();

				if (Objects.equals(Kit.equippedKits.get(damages.getUniqueId()).name, name)) {
					arrow.setDamage(throwDamage);
				}
			}
		}
	}

	/**
	 * Activate the spearman ability of destroying all ladders directly below the broken one
	 * @param e The event called when breaking a ladder
	 */
	@EventHandler (priority = EventPriority.LOWEST)
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

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Horse) {
			if (Objects.equals(Kit.equippedKits.get(e.getDamager().getUniqueId()).name, name)) {
				e.setDamage(e.getDamage() * HORSE_MULTIPLIER);
			}
		}
	}
}
