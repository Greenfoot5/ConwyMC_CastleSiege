package me.huntifi.castlesiege.kits.kits;

import me.huntifi.castlesiege.data_types.Tuple;
import me.huntifi.castlesiege.kits.EquipmentSet;
import me.huntifi.castlesiege.kits.Kit;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.tags.NametagsEvent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Engineer extends Kit implements Listener, CommandExecutor {

    public static HashMap<Player, ArrayList<Block>> traps = new HashMap<>();

    public Engineer() {
        super("Engineer");
        super.baseHeath = 110;


        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();
        super.heldItemSlot = 0;

        // Weapon
        ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.GREEN + "Shortsword");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
        itemMeta.setLore(new ArrayList<>());
        item.setItemMeta(itemMeta);
        es.hotbar[0] = item;
        // Voted Weapon
        item.getItemMeta().addEnchant(Enchantment.DAMAGE_ALL, 18, true);
        item.getItemMeta().setLore(Arrays.asList("", ChatColor.AQUA + "- voted: +2 damage"));
        es.votedWeapon = new Tuple<>(item, 0);

        // Chestplate
        item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Chestplate");
        leatherItemMeta.setColor(Color.fromRGB(57, 75, 57));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.chest = item;

        // Leggings
        item = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Leggings");
        leatherItemMeta.setColor(Color.fromRGB(57, 75, 57));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.legs = item;

        // Boots
        item = new ItemStack(Material.LEATHER_BOOTS);
        leatherItemMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        leatherItemMeta.setDisplayName(ChatColor.GREEN + "Leather Boots");
        leatherItemMeta.setColor(Color.fromRGB(57, 75, 57));
        leatherItemMeta.setUnbreakable(true);
        leatherItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(leatherItemMeta);
        es.feet = item;
        item.getItemMeta().addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
        es.votedFeet = item;

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
        super.potionEffects = new PotionEffect[1];
        super.potionEffects[0] = new PotionEffect(PotionEffectType.JUMP, 999999, 0);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        super.addPlayer(((Player) commandSender).getUniqueId());
        return true;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (Objects.equals(Kit.equippedKits.get(uuid).name, name)) {

            Material block = e.getBlockPlaced().getType();
            if (block == Material.STONE_PRESSURE_PLATE) {
                placeTrap(e, p);
            } else if (block == Material.COBWEB) {
                e.setCancelled(false);
            } else if (block == Material.OAK_PLANKS) {
                placeWood(e);
            } else if (block == Material.COBBLESTONE) {
                placeStone(e);
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onDestroy(BlockBreakEvent e) {
        Material block = e.getBlock().getType();
        if (block == Material.COBWEB) {
            e.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onWalkOverTrap(PlayerInteractEvent e) {
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
                p.sendMessage("You stepped on " + NametagsEvent.color(t) + t.getName() + ChatColor.RESET + "'s trap.");
                t.sendMessage(NametagsEvent.color(p) + p.getName() + ChatColor.RESET + " stepped on your trap.");

                // damage() for damage animation and granting kill
                // setHealth() for precise damage
                if (p.getHealth() > 25) {
                    p.damage(1); // trapper not included to prevent knockback
                    p.setHealth(p.getHealth() - 24);
                } else {
                    p.damage(1, t);
                    p.setHealth(0);
                }
            }
        }
    }

    @EventHandler
    public void onPickUpTrap(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

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

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        super.onDeath(e);
        destroyAllTraps(e.getEntity());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        destroyAllTraps(e.getPlayer());
    }

    private void placeTrap(BlockPlaceEvent e, Player p) {
        // Ensure that the player has a corresponding list
        if (!traps.containsKey(p)) {
            traps.put(p, new ArrayList<>());
        }
        ArrayList<Block> trapList = traps.get(p);

        // Already placed max amount of traps
        if (trapList.size() == 8) {
            p.sendMessage(ChatColor.RED + "You have placed your maximum amount of traps.");
            p.sendMessage(ChatColor.RED + "Pick your traps up if you need them or destroy them and restock.");
            return;
        }

        // Place the trap
        e.setCancelled(false);
        Block trap = e.getBlockPlaced();
        trapList.add(trap);
    }

    private void placeWood(BlockPlaceEvent e) {
        // TODO - Implement
    }

    private void placeStone(BlockPlaceEvent e) {
        // TODO - Implement
    }

    private void destroyAllTraps(Player p) {
        if (traps.containsKey(p)) {
            for (Block trap : traps.get(p)) {
                trap.setType(Material.AIR);
            }
            traps.remove(p);
        }
    }

    private Player getTrapper(Block trap) {
        return traps.entrySet().stream()
                .filter(entry -> entry.getValue().contains(trap))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }
}
