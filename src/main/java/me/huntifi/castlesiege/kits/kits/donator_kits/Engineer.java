package me.huntifi.castlesiege.kits.kits.donator_kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.database.UpdateStats;
import me.huntifi.castlesiege.events.chat.Messenger;
import me.huntifi.castlesiege.events.combat.AssistKill;
import me.huntifi.castlesiege.events.combat.HurtAnimation;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.events.death.DeathEvent;
import me.huntifi.castlesiege.events.gameplay.Explosion;
import me.huntifi.castlesiege.events.timed.BarCooldown;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.kits.kits.DonatorKit;
import me.huntifi.castlesiege.kits.kits.Kit;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
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

import java.util.*;

/**
 * The engineer kit
 */
public class Engineer extends DonatorKit implements Listener {

    private static final HashMap<Player, ArrayList<Block>> traps = new HashMap<>();
    private static final HashMap<Player, Tuple<Location, Boolean>> ballista = new HashMap<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Engineer() {
        super("Engineer", 210, 10.5, Material.COBWEB);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Shortsword", null, null, 38);
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Shortsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOT_BONUS_MOBS, 0)), 40),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(128, 129, 129));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(129, 129, 129));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(129, 129, 129));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider II"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(129, 129, 129));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Traps
        es.hotbar[2] = new ItemStack(Material.STONE_PRESSURE_PLATE, 8);

        // Cobwebs
        es.hotbar[3] = new ItemStack(Material.COBWEB, 16);

        // Wood
        es.hotbar[4] = new ItemStack(Material.OAK_PLANKS, 20);

        // Stone
        es.hotbar[5] = new ItemStack(Material.COBBLESTONE, 20);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
        super.potionEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));
        super.potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
    }

    /**
     * Activate the engineer ability for placing traps, cobwebs, wood, or stone
     * @param e The event called when placing a block
     */
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        // Prevent using in lobby
        if (InCombat.isPlayerInLobby(uuid)) {
            return;
        }

        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

            Material block = e.getBlockPlaced().getType();
            if (block == Material.STONE_PRESSURE_PLATE) {
                placeTrap(e, p);
            } else if (block == Material.OAK_PLANKS) {
                placeWood(e);
            } else if (block == Material.COBBLESTONE) {
                placeStone(e);
            }
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

            // Check if the trap should trigger
            Player p = e.getPlayer();
            Player t = getTrapper(trap);
            if (t != null && TeamController.getTeam(p.getUniqueId())
                    != TeamController.getTeam(t.getUniqueId())) {

                // Trigger the trap
                e.setCancelled(true);
                traps.get(t).remove(trap);
                trap.setType(Material.AIR);
                p.sendMessage(ChatColor.RED + "You stepped on " + NameTag.color(t) + t.getName() + ChatColor.RED + "'s trap.");
                t.sendMessage(NameTag.color(p) + p.getName() + ChatColor.GREEN + " stepped on your trap.");

                // Deal damage
                double damage = Math.min(p.getHealth(), 60);
                if (damage == p.getHealth()) {
                    DeathEvent.setKiller(p, t);
                }
                AssistKill.addDamager(p.getUniqueId(), t.getUniqueId(), damage);
                HurtAnimation.trigger(p);
                p.setHealth(p.getHealth() - damage);
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
        if (trap.getType() == Material.STONE_PRESSURE_PLATE && e.getEntity() instanceof Horse
                && !e.getEntity().getPassengers().isEmpty()
                && e.getEntity().getPassengers().get(0) instanceof Player) {

            // Check if the trap should trigger
            Horse h = (Horse) e.getEntity();
            Player p = (Player) h.getPassengers().get(0);
            Player t = getTrapper(trap);
            if (t != null && TeamController.getTeam(p.getUniqueId())
                    != TeamController.getTeam(t.getUniqueId())) {

                // Trigger the trap
                e.setCancelled(true);
                traps.get(t).remove(trap);
                trap.setType(Material.AIR);
                p.sendMessage(ChatColor.RED + "Your horse stepped on " + NameTag.color(t) + t.getName() + ChatColor.RED + "'s trap.");
                t.sendMessage(NameTag.color(p) + p.getName() + ChatColor.GREEN + "'s horse stepped on your trap.");
                h.damage(60);
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
            if (!traps.containsKey(p) || !traps.get(p).contains(e.getClickedBlock())) {
                Messenger.sendWarning("You can't pick up traps that are not your own.", p);
                return;
            }

            // Pick up trap and give to player if not at max
            traps.get(p).remove(e.getClickedBlock());
            e.getClickedBlock().setType(Material.AIR);

            PlayerInventory inv = p.getInventory();
            ItemStack offHand = inv.getItemInOffHand();
            int trapsOffHand = offHand.getType() == Material.STONE_PRESSURE_PLATE ? offHand.getAmount() : 0;

            if (!inv.contains(Material.STONE_PRESSURE_PLATE, 8 - trapsOffHand)) {
                inv.addItem(new ItemStack(Material.STONE_PRESSURE_PLATE, 1));
                p.sendMessage(ChatColor.GREEN + "You took this trap.");
            } else {
                p.sendMessage(ChatColor.DARK_RED + "You took this trap and put it away.");
            }
        }
    }

    /**
     * Set a player to be operating a ballista
     * @param e The event called when a player enters a minecart
     */
    @EventHandler
    public void onEnterBallista(VehicleEnterEvent e) {
        if (e.getVehicle() instanceof Minecart && e.getEntered() instanceof Player) {

            // Ensure that the entered minecart is a ballista and the player is an engineer
            Location dispenserFace = getDispenserFace(e.getVehicle().getLocation().add(0, 2, 0));
            if (dispenserFace == null) {
                return;
            } else if (!Objects.equals(Kit.equippedKits.get(e.getEntered().getUniqueId()).name, name)) {
                Messenger.sendError("Only engineers can use a ballista!", e.getEntered());
                e.setCancelled(true);
                return;
            }

            ballista.put((Player) e.getEntered(), new Tuple<>(dispenserFace, false));
        }
    }

    /**
     * Fire arrow from ballista
     * @param e The event called when a ballista operator left-clicks
     */
    @EventHandler
    public void onFireBallista(PlayerInteractEvent e) {
        if (ballista.containsKey(e.getPlayer()) &&
                (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            Tuple<Location, Boolean> ballista = Engineer.ballista.get(p);

            // On cooldown
            if (ballista.getSecond()) {
                return;
            }

            // Shoot arrow
            p.getWorld().playSound(ballista.getFirst(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 3);
            Arrow a = p.getWorld().spawnArrow(ballista.getFirst(), p.getLocation().getDirection(), 4, 0);
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
    @EventHandler (priority = EventPriority.LOWEST)
    public void onHitBallista(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player
                && ballista.containsKey((Player) e.getEntity().getShooter())) {

            Player shooter = (Player) e.getEntity().getShooter();
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
            ballista.remove((Player) e.getExited());
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
        destroyAllTraps(e.getEntity());
        ballista.remove(e.getEntity());
    }

    /**
     * Destroy all player's traps
     * Remove the player from their ballista
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyAllTraps(e.getPlayer());
        ballista.remove(e.getPlayer());
    }

    /**
     * Place a trap
     * @param e The event called when placing a stone pressure plate
     * @param p The player who placed the trap
     */
    private void placeTrap(BlockPlaceEvent e, Player p) {
        // Ensure that the player has a corresponding list
        traps.putIfAbsent(p, new ArrayList<>());
        ArrayList<Block> trapList = traps.get(p);

        // Already placed max amount of traps
        if (trapList.size() == 8) {
            Block firstPlaced = trapList.get(0);
            firstPlaced.setType(Material.AIR);
            trapList.remove(firstPlaced);
        }

        // Place the trap
        e.setCancelled(false);
        Block trap = e.getBlockPlaced();
        trapList.add(trap);
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
        UpdateStats.addSupports(e.getPlayer().getUniqueId(), 1);

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
     * @param p The player whose traps to destroy
     */
    private void destroyAllTraps(Player p) {
        if (traps.containsKey(p)) {
            for (Block trap : traps.get(p)) {
                trap.setType(Material.AIR);
            }
            traps.remove(p);
        }
    }

    /**
     * Get the placer of a trap
     * @param trap The trap whose placer to find
     * @return The placer of the trap, null if not placed by an engineer
     */
    private Player getTrapper(Block trap) {
        return traps.entrySet().stream()
                .filter(entry -> entry.getValue().contains(trap))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
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
        switch (facing) {
            case NORTH:
                return dispenser.getLocation().add(0.5, 0.4, -0.1);
            case EAST:
                return dispenser.getLocation().add(1.1, 0.4, 0.5);
            case SOUTH:
                return dispenser.getLocation().add(0.5, 0.4, 1.1);
            case WEST:
                return dispenser.getLocation().add(-0.1, 0.4, 0.5);
            default:
                return null;
        }
    }

    /**
     * Set the player's ballista cooldown to 100 ticks
     * @param p The player
     */
    private void ballistaCooldown(Player p) {
        Tuple<Location, Boolean> ballista = Engineer.ballista.get(p);
        ballista.setSecond(true);
        BarCooldown.add(p.getUniqueId(), 80);

        new BukkitRunnable() {
            @Override
            public void run() {
                ballista.setSecond(false);
            }
        }.runTaskLater(Main.plugin, 80);
    }
}
