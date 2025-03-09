package me.greenfoot5.castlesiege.kits.kits.level_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.LevelKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The spearman kit
 */
public class Spearman extends LevelKit implements Listener {
	private static final int health = 300;
	private static final double regen = 10.5;
	private static final int spearCount = 4;
	private static final double meleeDamage = 36;
	private static final int ladderCount = 4;

	// Spear Throw
	private static final int throwCooldown = 160;
	private static final double throwVelocity = 2.0;
	private static final int throwDelay = 5;
	private static final double throwDamage = 65;

	// Damage multiplier when hitting horses
	private static final double HORSE_MULTIPLIER = 1.5;

	/**
	 * Set the equipment and attributes of this kit
	 */
	public Spearman() {
		super("Spearman", health, regen, Material.STICK, 4);

		// Equipment Stuff
		EquipmentSet es = new EquipmentSet();

		// Weapon
		es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STICK, spearCount),
				Component.text("Spear", NamedTextColor.GREEN),
				Collections.singletonList(Component.text("Right-click to throw a spear.", NamedTextColor.AQUA)), null, meleeDamage);
		// Voted Weapon
		es.votedWeapon = new Tuple<>(
				CSItemCreator.weapon(new ItemStack(Material.STICK, 4),
						Component.text("Spear", NamedTextColor.GREEN),
						Arrays.asList(Component.text("Right-click to throw a spear.", NamedTextColor.AQUA),
								Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
						Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 1)), meleeDamage + 2),
				0);

		// Chestplate
		es.chest = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_CHESTPLATE),
				Component.text("Chainmail Chestplate", NamedTextColor.GREEN), null, null);

		// Leggings
		es.legs = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_LEGGINGS),
				Component.text("Chainmail Leggings", NamedTextColor.GREEN), null, null);

		// Boots
		es.feet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
				Component.text("Chainmail Boots", NamedTextColor.GREEN), null, null);
		// Voted Boots
		es.votedFeet = CSItemCreator.item(new ItemStack(Material.CHAINMAIL_BOOTS),
				Component.text("Chainmail Boots", NamedTextColor.GREEN),
				Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
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
		if (e.getPlayer() != equippedPlayer)
			return;

		// Prevent using in lobby
		if (InCombat.isPlayerInLobby(equippedPlayer.getUniqueId())) {
			return;
		}

		ItemStack stick = equippedPlayer.getInventory().getItemInMainHand();
		int cooldown = equippedPlayer.getCooldown(Material.STICK);

        if (!stick.getType().equals(Material.STICK))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (cooldown == 0) {
            Messenger.sendActionInfo("Preparing to throw your spear...", equippedPlayer);
            stick.setAmount(stick.getAmount() - 1);
            equippedPlayer.setCooldown(Material.STICK, throwCooldown);
            BarCooldown.add(equippedPlayer.getUniqueId(), throwDelay);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Messenger.sendActionInfo("You threw your spear!", equippedPlayer);
                    equippedPlayer.launchProjectile(Arrow.class).setVelocity(equippedPlayer.getLocation().getDirection().multiply(throwVelocity));
                }
            }.runTaskLater(Main.plugin, throwDelay);
        } else {
            Messenger.sendActionError("You can't throw your spear yet", equippedPlayer);
        }
    }

	/**
	 * Set the thrown spear's damage
	 * @param e The event called when an arrow hits a player
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void changeSpearDamage(ProjectileHitEvent e) {
		if (e.getEntity().getShooter() != equippedPlayer || !InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
			return;

		if (e.getEntity() instanceof AbstractArrow arrow)
			arrow.setDamage(throwDamage);
	}

	/**
	 * Activate the spearman ability of destroying all ladders directly below the broken one
	 * @param e The event called when breaking a ladder
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakLadder(BlockBreakEvent e) {
		if (e.getPlayer() != equippedPlayer || InCombat.isPlayerInLobby(equippedPlayer.getUniqueId()))
			return;

        Location loc = e.getBlock().getLocation();
        while (loc.getBlock().getType() == Material.LADDER) {
            loc.getBlock().setType(Material.AIR);
            loc.setY(loc.getBlockY() - 1);
        }
    }

	/**
	 * @param e When a player hits a horse, grants bonus damage
	 */
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() == equippedPlayer && e.getEntity() instanceof Horse) {
            e.setDamage(e.getDamage() * HORSE_MULTIPLIER);
        }
	}

	/**
	 * @return The lore to add to the kit gui item
	 */
	@Override
    public ArrayList<Component> getGuiDescription() {
		ArrayList<Component> kitLore = new ArrayList<>();
		kitLore.add(Component.text("A mid-ranged unit with a melee weapon", NamedTextColor.GRAY));
		kitLore.add(Component.text("that can be thrown", NamedTextColor.GRAY));
		kitLore.addAll(getBaseStats(health, regen, meleeDamage, throwDamage, ladderCount, spearCount));
		kitLore.add(Component.empty());
		kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
		kitLore.add(Component.text("- Can throw their spears", NamedTextColor.GRAY));
		kitLore.add(Component.empty());
		kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
		kitLore.add(Component.text("- Deals bonus damage to horses", NamedTextColor.GRAY));
		kitLore.add(Component.text("- Can break entire columns of ladders instantly", NamedTextColor.GRAY));
		return kitLore;
	}
}
