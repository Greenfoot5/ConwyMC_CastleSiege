package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.Main;
import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.events.combat.InCombat;
import me.huntifi.castlesiege.kits.items.EquipmentSet;
import me.huntifi.castlesiege.kits.items.ItemCreator;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.NameTag;
import me.huntifi.castlesiege.maps.Team;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The engineer kit
 */
public class Engineer extends Kit implements Listener, CommandExecutor {

    private static final HashMap<Player, ArrayList<Block>> traps = new HashMap<>();
    private static final HashMap<Player, Tuple<Location, Boolean>> ballistae = new HashMap<>();

    /**
     * Set the equipment and attributes of this kit
     */
    public Engineer() {
        super("Engineer", 110);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        es.hotbar[0] = ItemCreator.item(new ItemStack(Material.STONE_SWORD),
                ChatColor.GREEN + "Shortsword", null,
                Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 16)));
        // Voted weapon
        es.votedWeapon = new Tuple<>(
                ItemCreator.item(new ItemStack(Material.STONE_SWORD),
                        ChatColor.GREEN + "Shortsword",
                        Collections.singletonList(ChatColor.AQUA + "- voted: +2 damage"),
                        Collections.singletonList(new Tuple<>(Enchantment.DAMAGE_ALL, 18))),
                0);

        // Chestplate
        es.chest = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                ChatColor.GREEN + "Leather Chestplate", null, null,
                Color.fromRGB(57, 75, 57));

        // Leggings
        es.legs = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                ChatColor.GREEN + "Leather Leggings", null, null,
                Color.fromRGB(57, 75, 57));

        // Boots
        es.feet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots", null, null,
                Color.fromRGB(57, 75, 57));
        // Voted Boots
        es.votedFeet = ItemCreator.leatherArmor(new ItemStack(Material.LEATHER_BOOTS),
                ChatColor.GREEN + "Leather Boots",
                Collections.singletonList(ChatColor.AQUA + "- voted: Depth Strider +2"),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)),
                Color.fromRGB(57, 75, 57));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, 4);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, 6), 1);

        // Traps
        es.hotbar[2] = new ItemStack(Material.STONE_PRESSURE_PLATE, 8);

        // Cobwebs
        es.hotbar[3] = new ItemStack(Material.COBWEB, 16);

        // Wood
        es.hotbar[4] = new ItemStack(Material.OAK_PLANKS, 16);

        // Stone
        es.hotbar[5] = new ItemStack(Material.COBBLESTONE, 16);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP, 999999, 0));
    }

    /**
     * Register the player as using this kit and set their items
     * @param commandSender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param strings Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Console cannot select kits!");
            return true;
        }

        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
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
            if (t != null && MapController.getCurrentMap().getTeam(p.getUniqueId())
                    != MapController.getCurrentMap().getTeam(t.getUniqueId())) {

                // Trigger the trap
                e.setCancelled(true);
                traps.get(t).remove(trap);
                trap.setType(Material.AIR);
                p.sendMessage(ChatColor.RED + "You stepped on " + NameTag.color(t) + t.getName() + ChatColor.RED + "'s trap.");
                t.sendMessage(NameTag.color(p) + p.getName() + ChatColor.GREEN + " stepped on your trap.");

                // Prevent damage reduction from armor and resistance
                PotionEffect resistance = p.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                AttributeInstance armor = p.getAttribute(Attribute.GENERIC_ARMOR);
                assert armor != null;
                armor.setBaseValue(-armor.getValue());

                // Prevent knockback indicating the trapper's position
                AttributeInstance kb = p.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                assert kb != null;
                double kbValue = kb.getBaseValue();
                kb.setBaseValue(2);

                // Deal damage
                p.damage(25, t);

                // Revert knockback, armor, and resistance changes
                kb.setBaseValue(kbValue);
                armor.setBaseValue(0);
                if (resistance != null) {
                    p.addPotionEffect(resistance);
                }
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
                e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {

            // Check if player is the trap's owner
            if (!traps.containsKey(p) || !traps.get(p).contains(e.getClickedBlock())) {
                p.sendMessage(ChatColor.DARK_RED + "You can't pick up traps that are not your own.");
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
                e.getEntered().sendMessage(ChatColor.DARK_RED + "Only engineers can use a ballista!");
                e.setCancelled(true);
                return;
            }

            ballistae.put((Player) e.getEntered(), new Tuple<>(dispenserFace, false));
        }
    }

    /**
     * Fire arrow from ballista
     * @param e The event called when a ballista operator left-clicks
     */
    @EventHandler
    public void onFireBallista(PlayerInteractEvent e) {
        if (ballistae.containsKey(e.getPlayer()) &&
                (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
            Player p = e.getPlayer();
            Tuple<Location, Boolean> ballista = ballistae.get(p);

            // On cooldown
            if (ballista.getSecond()) {
                return;
            }

            // Shoot arrow
            p.getWorld().playSound(ballista.getFirst(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
            Arrow a = p.getWorld().spawnArrow(ballista.getFirst(), p.getLocation().getDirection(), 4, 0);
            a.setShooter(p);
            a.setDamage(50);

            // Set cooldown
            ballista.setSecond(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ballista.setSecond(false);
                }
            }.runTaskLater(Main.plugin, 100);
        }
    }

    /**
     * Damage all enemy players within a 1 block radius
     * @param e The event called when an arrow hits a block or entity
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onHitBallista(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player
                && ballistae.containsKey((Player) e.getEntity().getShooter())) {

            Player shooter = (Player) e.getEntity().getShooter();
            Team team = MapController.getCurrentMap().getTeam(shooter.getUniqueId());
            for (Entity hit : e.getEntity().getNearbyEntities(1, 1, 1)) {
                if (hit instanceof Player && MapController.getCurrentMap().getTeam(hit.getUniqueId()) != team) {
                    ((Player) hit).damage(20, shooter);
                }
            }

            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1, 1);
        }
    }

    /**
     * Remove the player from their ballista
     * @param e The event called when a player exits a minecart
     */
    @EventHandler
    public void onExitBallista(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            ballistae.remove((Player) e.getExited());
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
        ballistae.remove(e.getEntity());
    }

    /**
     * Destroy all player's traps
     * Remove the player from their ballista
     * @param e The event called when a player leaves the game
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyAllTraps(e.getPlayer());
        ballistae.remove(e.getPlayer());
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
        // TODO - Implement
    }

    /**
     * Repair a stone block
     * @param e The event called when placing stone
     */
    private void placeStone(BlockPlaceEvent e) {
        // TODO - Implement
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
                return loc.add(0, 0, -0.5);
            case EAST:
                return loc.add(0.5, 0, 0);
            case SOUTH:
                return loc.add(0, 0, 0.5);
            case WEST:
                return loc.add(-0.5, 0, 0);
            default:
                return null;
        }
    }
}
