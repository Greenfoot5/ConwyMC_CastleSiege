package me.greenfoot5.castlesiege.kits.kits.coin_kits;

import me.greenfoot5.castlesiege.Main;
import me.greenfoot5.castlesiege.database.UpdateStats;
import me.greenfoot5.castlesiege.events.combat.InCombat;
import me.greenfoot5.castlesiege.events.gameplay.Explosion;
import me.greenfoot5.castlesiege.events.timed.BarCooldown;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.CoinKit;
import me.greenfoot5.castlesiege.kits.kits.Kit;
import me.greenfoot5.castlesiege.maps.Team;
import me.greenfoot5.castlesiege.maps.TeamController;
import me.greenfoot5.castlesiege.misc.CSNameTag;
import me.greenfoot5.conwymc.data_types.Tuple;
import me.greenfoot5.conwymc.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The engineer kit
 */
public class Engineer extends CoinKit implements Listener {

    private static final int health = 210;
    private static final double regen = 10.5;
    private static final int ladderCount = 4;
    private static final int meleeDamage = 38;
    private static final int cobwebCount = 16;
    private static final int planksCount = 20;
    private static final int cobblestoneCount = 20;
    private static final int trapCount = 8;

    private static final double TRAP_DAMAGE = 60;
    private static final int BALLISTA_COOLDOWN_TICKS = 80;

    private final LinkedHashSet<Block> traps = new LinkedHashSet<>();
    private Location ballistaLocation;
    private long ballistaCooldown;

    /**
     * Set the equipment and attributes of this kit
     */
    public Engineer() {
        super("Engineer", health, regen, Material.COBWEB);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Short-sword", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text("38 Melee Damage", NamedTextColor.DARK_GREEN)),
                null, meleeDamage);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Short-sword", NamedTextColor.GREEN),
                        List.of(Component.empty(),
                                Component.text("40 Melee Damage", NamedTextColor.DARK_GREEN),
								Component.text("⁎ Voted: +2 Melee Damage", NamedTextColor.DARK_AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Chestplate", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(128, 129, 129));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(129, 129, 129));

        // Boots
        es.feet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN)), null,
                Color.fromRGB(129, 129, 129));
        // Voted Boots
        es.votedFeet = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                Component.text("Leather Boots", NamedTextColor.GREEN),
                List.of(Component.empty(),
                        Component.text(health + " HP", NamedTextColor.DARK_GREEN),
                        Component.text(regen + " Regen", NamedTextColor.DARK_GREEN),
                        Component.empty(),
                        Component.text("⁎ Voted: Depth Strider II", NamedTextColor.DARK_AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(129, 129, 129));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        // Traps
        es.hotbar[2] = new ItemStack(Material.STONE_PRESSURE_PLATE, trapCount);

        // Cobwebs
        es.hotbar[3] = new ItemStack(Material.COBWEB, cobwebCount);

        // Wood
        es.hotbar[4] = new ItemStack(Material.OAK_PLANKS, planksCount);

        // Stone
        es.hotbar[5] = new ItemStack(Material.COBBLESTONE, cobblestoneCount);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.HASTE, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
    }

    /**
     * Activate the engineer ability for placing traps, cobwebs, wood, or stone
     * @param e The event called when placing a block
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid))
            return;
        if (e.getPlayer() != equippedPlayer)
            return;

        Material block = e.getBlockPlaced().getType();
        if (block == Material.STONE_PRESSURE_PLATE
                && equippedPlayer.getCooldown(Material.STONE_PRESSURE_PLATE) == 0) {

            placeTrap(e);

        } else if (block == Material.OAK_PLANKS) {
            placeWood(e);
        } else if (block == Material.COBBLESTONE) {
            placeStone(e);
        }
    }

    /**
     * Activate the engineer ability for opponents walking on their traps
     * @param e The event called when a player steps on a stone pressure plate
     */
    @EventHandler
    public void onWalkOverTrap(PlayerInteractEvent e) {
        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(e.getPlayer().getUniqueId())) {
            return;
        }

        // Check if the player stepped on a trap
        Block trap = e.getClickedBlock();
        if (e.getAction() == Action.PHYSICAL && trap != null &&
                trap.getType() == Material.STONE_PRESSURE_PLATE) {

            if (!traps.contains(trap))
                return;

            // Check if the trap should trigger
            Player p = e.getPlayer();
            if (TeamController.getTeam(p.getUniqueId()) != TeamController.getTeam(equippedPlayer.getUniqueId())) {

                // Trigger the trap
                e.setCancelled(true);
                traps.remove(trap);
                trap.setType(Material.AIR);
                Messenger.sendWarning("You stepped on " + CSNameTag.mmUsername(equippedPlayer) + "'s trap.", p);
                Messenger.sendSuccess(CSNameTag.mmUsername(p) + " stepped on your trap.", equippedPlayer);

                // Deal damage
                p.damage(TRAP_DAMAGE, equippedPlayer);
            }
        }
    }

    /**
     * Activate the engineer ability for opponents walking on their traps
     * @param e The event called when a player steps on a stone pressure plate
     */
    @EventHandler
    public void onHorseWalkOverTrap(EntityInteractEvent e) {
        // Check if the player stepped on a trap
        Block trap = e.getBlock();
        if (trap.getType() == Material.STONE_PRESSURE_PLATE && e.getEntity() instanceof Horse h
                && !e.getEntity().getPassengers().isEmpty()
                && e.getEntity().getPassengers().getFirst() instanceof Player) {

            // Check if the trap should trigger
            Player p = (Player) h.getPassengers().getFirst();
            if (equippedPlayer != null && TeamController.getTeam(p.getUniqueId())
                    != TeamController.getTeam(equippedPlayer.getUniqueId())) {

                // Trigger the trap
                e.setCancelled(true);
                traps.remove(trap);
                trap.setType(Material.AIR);
                Messenger.sendWarning("Your horse on " + CSNameTag.mmUsername(equippedPlayer) + "'s trap.", p);
                Messenger.sendSuccess(CSNameTag.mmUsername(p) + "'s horse stepped on your trap.", equippedPlayer);
                h.damage(60, equippedPlayer);
            }
        }
    }

    /**
     * Activate the engineer ability for picking up their own traps
     * @param e The event called when a player left-clicks a stone pressure plate
     */
    @EventHandler
    public void onPickUpTrap(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        // Check if engineer tries to pick up
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name) &&
                e.getAction() == Action.LEFT_CLICK_BLOCK &&
                Objects.requireNonNull(e.getClickedBlock()).getType() == Material.STONE_PRESSURE_PLATE) {

            // Check if player is the trap's owner
            if (p != equippedPlayer || !traps.contains(e.getClickedBlock())) {
                Messenger.sendWarning("You can't pick up traps that are not your own.", p);
                return;
            }

            // Pick up trap and give to player if not at max
            traps.remove(e.getClickedBlock());
            e.getClickedBlock().setType(Material.AIR);

            PlayerInventory inv = p.getInventory();
            ItemStack offHand = inv.getItemInOffHand();
            int trapsOffHand = offHand.getType() == Material.STONE_PRESSURE_PLATE ? offHand.getAmount() : 0;

            if (!inv.contains(Material.STONE_PRESSURE_PLATE, 8 - trapsOffHand)) {
                inv.addItem(new ItemStack(Material.STONE_PRESSURE_PLATE, 1));
                Messenger.sendActionInfo("You picked up the trap", p);
            } else {
                Messenger.sendActionInfo("You picked up the trap and put it away.", p);
            }
        }
    }

    /**
     * Set a player to be operating a ballista
     * @param e The event called when a player enters a minecart
     */
    @EventHandler
    public void onEnterBallista(VehicleEnterEvent e) {
        if (e.getVehicle() instanceof Minecart && e.getEntered() instanceof Player p) {

            // Ensure that the entered minecart is a ballista and the player is an engineer
            Location dispenserFace = getDispenserFace(e.getVehicle().getLocation().add(0, 2, 0));
            if (dispenserFace == null) {
                return;
            } else if (!Objects.equals(Kit.equippedKits.get(e.getEntered().getUniqueId()).name, name)) {
                Messenger.sendActionError("Only engineers can use a ballista!", p);
                e.setCancelled(true);
                return;
            }

            ballistaLocation = dispenserFace;
            ballistaCooldown = 0;
        }
    }

    /**
     * Fire arrow from ballista
     * @param e The event called when a ballista operator left-clicks
     */
    @EventHandler
    public void onFireBallista(PlayerInteractEvent e) {
        if (e.getPlayer() == equippedPlayer &&
                (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            Player p = e.getPlayer();

            // On cooldown
            if (ballistaCooldown > System.currentTimeMillis()) {
                return;
            }

            // Shoot arrow
            p.getWorld().playSound(ballistaLocation, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 3);
            Arrow a = p.getWorld().spawnArrow(ballistaLocation, p.getLocation().getDirection(), 4, 0);
            a.setShooter(p);
            a.setDamage(5);

            // Set cooldown
            ballistaCooldown(p);
        }
    }

    /**
     * Damage all enemy players within a 1 block radius
     * @param e The event called when an arrow hits a block or entity
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onHitBallista(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player shooter
                && equippedPlayer == e.getEntity().getShooter()) {

            Team team = TeamController.getTeam(shooter.getUniqueId());
            int hitCount = 0;
            for (Entity hit : e.getEntity().getNearbyEntities(2.5, 2.5, 2.5)) {
                if (hit instanceof Player && TeamController.getTeam(hit.getUniqueId()) != team) {
                    ((Player) hit).damage(125, shooter);
                    hitCount++;
                }
            }

            Messenger.sendActionInfo(String.format("You hit %d players", hitCount), shooter);
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 4, 1);
        }
    }

    /**
     * Remove the player from their ballista
     * @param e The event called when a player exits a minecart
     */
    @EventHandler
    public void onExitBallista(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            ballistaLocation = null;
            BarCooldown.remove(e.getExited().getUniqueId());
        }
    }

    /**
     * Destroy all player's traps
     * Remove the player from their ballista
     * @param e The event called when a player dies
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity() != equippedPlayer)
            return;

        destroyAllTraps();
        ballistaLocation = null;
    }

    /**
     * Destroy all player's traps
     * Remove the player from their ballista
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (e.getPlayer() != equippedPlayer)
            return;

        destroyAllTraps();
        ballistaLocation = null;
    }

    /**
     * Place a trap
     * @param e The event called when placing a stone pressure plate
     */
    private void placeTrap(BlockPlaceEvent e) {
        // Already placed max amount of traps
        if (traps.size() == 8) {
            Block firstPlaced = traps.getFirst();
            firstPlaced.setType(Material.AIR);
            traps.remove(firstPlaced);
        }

        // Place the trap
        e.setCancelled(false);
        Block trap = e.getBlockPlaced();
        traps.add(trap);

        // Add cooldown if in combat
        if (InCombat.isPlayerInCombat(equippedPlayer.getUniqueId()))
            equippedPlayer.setCooldown(Material.STONE_PRESSURE_PLATE, 60);
    }

    /**
     * Repair a wooden block
     * @param e The event called when placing a wooden plank
     */
    private void placeWood(BlockPlaceEvent e) {
        BlockState blockState = Explosion.getWood(e.getBlock().getLocation());
        if (blockState != null) {
            placeBlock(e, blockState);
        }
    }

    /**
     * Repair a stone block
     * @param e The event called when placing stone
     */
    private void placeStone(BlockPlaceEvent e) {
        BlockState blockState = Explosion.getStone(e.getBlock().getLocation());
        if (blockState != null) {
            placeBlock(e, blockState);
        }
    }

    /**
     * Finish the repair process
     * @param e The original place event
     * @param blockState The state of the block to place
     */
    private void placeBlock(BlockPlaceEvent e, BlockState blockState) {
        // Remove one of the used item from the player and award a support point
        ItemStack item = e.getItemInHand();
        item.setAmount(item.getAmount() - 1);
        UpdateStats.addSupports(equippedPlayer.getUniqueId(), 1);

        // Place the block on the next tick to prevent it being overwritten by the original place event
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockData data = blockState.getBlockData();
                blockState.getBlock().setBlockData(data);
            }
        }.runTask(Main.plugin);
    }

    /**
     * Destroy all traps that were placed by the specified player
     */
    private void destroyAllTraps() {
        for (Block trap : traps) {
            trap.setType(Material.AIR);
        }
    }

    /**
     * Get the location of the face of the dispenser
     * @param loc The location of the dispenser
     * @return The dispenser's face location, or null if no dispenser location was provided
     */
    private Location getDispenserFace(Location loc) {
        Block dispenser = loc.getBlock();
        if (dispenser.getType() != Material.DISPENSER) {
            return null;
        }

        BlockFace facing = ((Directional) dispenser.getBlockData()).getFacing();
        return switch (facing) {
            case NORTH -> dispenser.getLocation().add(0.5, 0.4, -0.1);
            case EAST -> dispenser.getLocation().add(1.1, 0.4, 0.5);
            case SOUTH -> dispenser.getLocation().add(0.5, 0.4, 1.1);
            case WEST -> dispenser.getLocation().add(-0.1, 0.4, 0.5);
            default -> null;
        };
    }

    /**
     * Set the player's ballista cooldown to 100 ticks
     * @param p The player
     */
    private void ballistaCooldown(Player p) {
        ballistaCooldown = System.currentTimeMillis() + (BALLISTA_COOLDOWN_TICKS * 20);
        BarCooldown.add(p.getUniqueId(), BALLISTA_COOLDOWN_TICKS);
    }

    /**
     * @return The lore to add to the kit gui item
     */
    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("A support/debuff kit that can lay trap", NamedTextColor.GRAY));
        kitLore.add(Component.text("an operate various machines of war", NamedTextColor.GRAY));
        kitLore.add(Component.text("or repair broken structures", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.text(cobwebCount, color)
                .append(Component.text(" Cobwebs", NamedTextColor.GRAY)));
        kitLore.add(Component.text(planksCount, color)
                .append(Component.text(" Planks", NamedTextColor.GRAY)));
        kitLore.add(Component.text(cobblestoneCount, color)
                .append(Component.text(" Cobblestone", NamedTextColor.GRAY)));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Speed I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Haste II", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Passive:", NamedTextColor.DARK_GREEN));
        kitLore.add(Component.text("- Can place down traps and cobwebs", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can fire ballista", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can refill catapults with cobblestone", NamedTextColor.GRAY));
        kitLore.add(Component.text("- Can repair stone and wood blocks", NamedTextColor.GRAY));
        return kitLore;
    }
}
